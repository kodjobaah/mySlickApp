package com.valtech.actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.util.Timeout
import akka.pattern.{ ask, pipe }
import scala.concurrent.duration._
import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits._

import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import FetchCoffeesAndSuppliers.CoffeesAndSuppliers
  
class DatabaseReaderSupervisor extends Actor with ActorLogging {

  //Used by ?(ask)
  implicit val timeout = Timeout(5 seconds)
  val child = context.actorOf(FetchCoffeesAndSuppliers.props, name = "fetch-coffees-and-suppliers")
  override def receive: Receive = {
    case ReadCoffeesAndSuppliers => {
      val response: Future[CoffeesAndSuppliers] = ask(child, ReadCoffeesAndSuppliers).mapTo[CoffeesAndSuppliers]
     response pipeTo sender
    }

  }

}

object DatabaseReaderSupervisor {

  def props: Props =
    Props(new DatabaseReaderSupervisor)

  case class ReadCoffeesAndSuppliers()

}