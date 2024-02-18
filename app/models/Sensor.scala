package models

import play.api.libs.json.{Json, Reads, Writes}

case class Sensor(key: Int, value: String)
object Sensor {
  implicit val jsonReads: Reads[Sensor] = Json.reads[Sensor]
  implicit val jsonWrites: Writes[Sensor] = Json.writes[Sensor]
}
