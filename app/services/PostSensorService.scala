package services

import Utils.ExceptionLogger
import models.Sensor
import play.api.libs.json.Json
import play.api.{Configuration, Logger}
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class PostSensorService @Inject()(ws: WSClient, conf: Configuration) {

  val log = Logger(this.getClass.getName)

  def postSensor(sensor: Sensor) = {
    log.warn(s"posting Sensor: id: ${sensor.key}, value: ${sensor.value}")

    conf.getOptional[String]("sensorAPIURL")
      .map(url => {
        ws.url(url)
          .post(Json.toJson(sensor))
          .flatMap(response => {
            log.warn(s"posted sensor to : $url with response code: ${response.status} and value: ${response.body}")
            Future.successful(true)
          })
      })
      .getOrElse(Future.failed(ExceptionLogger.newException("Url Configuration Missing!")))
  }

}
