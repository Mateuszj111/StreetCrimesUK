import sbt._

object Dependencies {
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % "3.2.1"
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % "3.2.1"
  lazy val mongo = "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.1"
  lazy val mongoScala = "org.mongodb.scala" %% "mongo-scala-driver" % "4.5.0"
}
