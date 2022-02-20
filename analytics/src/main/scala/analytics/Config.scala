package analytics

object Config {

  val outcomesDataPath: String = "src/main/resources/csv/outcomes"
  val streetDataPath: String = "src/main/resources/csv/street"

  val csvReadOptions: Map[String, String] = Map("header" -> "true")

}