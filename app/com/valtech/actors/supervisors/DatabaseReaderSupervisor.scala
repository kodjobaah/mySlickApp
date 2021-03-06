package com.valtech.actors.supervisors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.util.Timeout
import akka.pattern.{ask, pipe}

import scala.concurrent.duration._
import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits._

import com.valtech.actors.supervisors._
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import children.FetchCoffeesAndSuppliers
import children.FetchCoffeesAndSuppliers._
import com.valtech.actors.services.AccessDatabaseService

class DatabaseReaderSupervisor(accessDatabaseService: AccessDatabaseService) extends Actor with ActorLogging {

  //Used by ?(ask)
  implicit val timeout = Timeout(5 seconds)
  val child = context.actorOf(FetchCoffeesAndSuppliers.props(accessDatabaseService), name = "fetch-coffees-and-suppliers")
  override def receive: Receive = {
    case ReadCoffeesAndSuppliers => {
      val response: Future[CoffeesAndSuppliers] = ask(child, ReadCoffeesAndSuppliers).mapTo[CoffeesAndSuppliers]
     response pipeTo sender
    }

  }

}

object DatabaseReaderSupervisor {

  def props(accessDatabaseService: AccessDatabaseService): Props =
    Props(new DatabaseReaderSupervisor(accessDatabaseService))

  case class ReadCoffeesAndSuppliers()

}