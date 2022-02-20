package analytics

import org.mongodb.scala.model.Indexes
import org.mongodb.scala.{Document, MongoClient, MongoCollection, SingleObservable}
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import com.mongodb.spark._
import com.mongodb.spark.config._


object Main extends App{
  val spark: SparkSession = SparkSession
    .builder()
    .appName("AnalyticsEngine")
    .master("local[*]")
    .config("spark.sql.shuffle.partitions", 100)
    .config("spark.driver.memory", "10g")
    .config("spark.mongodb.output.uri", "mongodb://spark:spark@mongo/admin.final")
    .getOrCreate()

  import spark.implicits._

  val outcomesDataframe: DataFrame = spark.read
    .options(Config.csvReadOptions)
    .csv(Config.outcomesDataPath)
    .withColumn("Longitude", $"Longitude".cast(DoubleType))
    .withColumn("Latitude", $"Latitude".cast(DoubleType))

  val outcomesNamesLookup: Map[String, String] = Map(
    "Crime ID" -> "crimeID",
    "Month" -> "month",
    "Outcome type" -> "outcomeType"
  )

  val outcomeNames: Seq[Column] = outcomesNamesLookup.map {case (name, newname) => col(name).as(newname)}.toSeq

  val windowSpec: WindowSpec = Window.partitionBy("crimeID").orderBy($"month".desc)

  val lastOutcomePerCrime: DataFrame = outcomesDataframe.select(outcomeNames: _*).withColumn("rank", rank.over(windowSpec)).filter($"rank" === 1).groupBy($"crimeID").agg(collect_set($"outcomeType").alias("outcomeType"))




  val streetDataframe: DataFrame = spark.read
    .options(Config.csvReadOptions)
    .csv(Config.streetDataPath)
    .withColumn("Longitude", $"Longitude".cast(DoubleType))
    .withColumn("Latitude", $"Latitude".cast(DoubleType))
    .withColumn("districtName", regexp_extract(element_at(split(input_file_name, "/"), -1), "\\d{4}-\\d{2}-(.*)-street\\.csv", 1))

  val streetNamesLookup: Map[String, String] = Map(
    "Crime ID" -> "crimeID",
    "districtName" -> "districtName",
    "Longitude" -> "longitude",
    "Latitude" -> "latitude",
    "Crime type" -> "crimeType",
    "Last outcome category" -> "lastOutcome"
  )

  val streetNames: Seq[Column] = streetNamesLookup.map {case (name, newname) => col(name).as(newname)}.toSeq


  val streetWithLastOutcome: DataFrame = streetDataframe.select(streetNames: _*).drop("lastOutcome").distinct.join(lastOutcomePerCrime, Seq("crimeID"), "left")

  val lastOutcomeFromStreetDataframe: DataFrame = streetDataframe.select(streetNames: _*).groupBy($"crimeId").agg(collect_set($"lastOutcome").alias("outcomeTypeStreet"))

  val outputAggregate: DataFrame = streetWithLastOutcome
    .join(lastOutcomeFromStreetDataframe, Seq("crimeId"), "left")
    .withColumn("outcomeType", when($"outcomeType".isNull, $"outcomeTypeStreet").otherwise($"outcomeType"))
    .drop("outcomeTypeStreet")
    .filter($"crimeID".isNotNull)

  outputAggregate.persist()
  outputAggregate.count()

    outputAggregate
    .write
    .option("collection", "final")
    .mode("overwrite")
    .format("mongo")
    .save()

  //KPIS

  outputAggregate
    .groupBy($"crimeType")
    .agg(countDistinct($"crimeID").alias("count"))
    .withColumn("fraction", $"count"/sum("count").over())
    .write
    .option("collection", "crimesShare")
    .mode("overwrite")
    .format("mongo")
    .save()

  outputAggregate
    .groupBy($"crimeType", $"districtName")
    .agg(countDistinct($"crimeID").alias("count"))
    .write
    .option("collection", "crimesByLocation")
    .mode("overwrite")
    .format("mongo")
    .save()


  outputAggregate
    .filter($"outcomeType".isNotNull)
    .select(explode($"outcomeType").alias("outcomeType"), $"crimeType", $"crimeID")
    .groupBy($"outcomeType")
    .pivot($"crimeType")
    .agg(countDistinct($"crimeID"))
    .write
    .option("collection", "crimesByOutcome")
    .mode("overwrite")
    .format("mongo")
    .save()

  outputAggregate.unpersist()


  val mongoClient = MongoClient("mongodb://spark:spark@mongo")
  val collection: MongoCollection[Document] = mongoClient.getDatabase("admin").getCollection("final")

  val observable: SingleObservable[String] =  collection.createIndex(Indexes.ascending("crimeID"))
  observable.subscribe(
    (str: String) => println(str),
    (e: Throwable) => println(s"There was an error: $e"),
    () => println("Completed!")
  )


}
