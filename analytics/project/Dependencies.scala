import sbt._

object Dependencies {
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % "3.2.1" % "provided"
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % "3.2.1" % "provided"
  lazy val mongo = "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.1"
}
