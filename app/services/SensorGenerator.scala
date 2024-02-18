package services

import akka.actor.{Actor, Props, Timers}
import com.google.inject.AbstractModule
import models.Sensor
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.libs.json.Json
import play.api.{Configuration, Logger}
import services.SensorGenerator.GenerateSensors

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

object SensorGenerator {
  def props = Props[SensorGeneratorActor]()
  case object GenerateSensors
}

/*
This Actor runs every 5 seconds.
It generates a list of sensors with basic int IDs for keys and random strings as values.
It the posts those sensors to the url given in the application.conf
 */
class SensorGeneratorActor @Inject()(conf: Configuration, postSensorService: PostSensorService) extends Actor with Timers {

  val log = Logger(this.getClass.getName)
  val generate = GenerateSensors

  timers.startSingleTimer(generate, generate, 5.second)

  override def receive: Receive = {
    case GenerateSensors =>

      log.warn("Generating Sensors!")

      val postResult: Seq[Boolean] = for {
        id: Int <- 1 to 10
        sensor = new Sensor(id, UUID.randomUUID().toString)
      } yield {
        Try(Await.result(postSensorService.postSensor(sensor), 30.second)) match {
          case Success(result) => {
            log.warn("sensor successfully sent")
            result
          }
          case Failure(ex) => {
            false
          }
        }
      }

      timers.startSingleTimer(generate, generate, 5.second)
  }
}

class SensorGeneratorModule extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[SensorGeneratorActor]("SensorGeneratorModule")
  }
}