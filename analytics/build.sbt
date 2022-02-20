ThisBuild / scalaVersion := "2.12.15"


lazy val root = (project in file("."))
  .settings(
    name := "analytics",
    version := "0.1",
    libraryDependencies ++= Seq(
      Dependencies.sparkCore,
      Dependencies.sparkSql,
      Dependencies.mongo,
      Dependencies.mongoScala
    )
  )
