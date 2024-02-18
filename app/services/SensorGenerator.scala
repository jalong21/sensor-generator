package services

import akka.actor.{Actor, Props, Timers}
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.{Configuration, Logger}
import services.SensorGenerator.GenerateSensors

import javax.inject.Inject
import scala.concurrent.duration.DurationInt

object SensorGenerator {
  def props = Props[SensorGeneratorActor]()
  case object GenerateSensors
}

/*
This Actor runs every 5 seconds.
It generates a list of sensors with basic int IDs for keys and random strings as values.
It the posts those sensors to the url given in the application.conf
 */
class SensorGeneratorActor @Inject()(conf: Configuration) extends Actor with Timers {

  val log = Logger(this.getClass.getName)
  val generate = GenerateSensors

  timers.startSingleTimer(generate, generate, 5.second)

  override def receive: Receive = {
    case GenerateSensors => {

      log.warn("Generating Sensors!")

      timers.startSingleTimer(generate, generate, 5.second)
    }
  }
}

class SensorGeneratorModule extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[SensorGeneratorActor]("SensorGeneratorModule")
  }
}