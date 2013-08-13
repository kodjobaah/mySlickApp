package com.valtech.actors.supervisors.children

import play.api.db._
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import com.typesafe.slick.driver.oracle.OracleDriver.simple._
import scala.reflect.runtime.universe._
import com.valtech.actors.services._
import akka.actor.actorRef2Scala
import com.valtech.actors.supervisors.OptimisticLockSupervisor.UpdateCoffee
import com.valtech.actors.supervisors.children.UpdateCoffeesAndRelationsActor.UpdateResults

class UpdateCoffeesAndRelationsActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case UpdateCoffee(coffee) => {
      val actualFieldsToUpdate = AccessDatabaseService().updateDatabase(coffee)
      sender ! UpdateResults("Updated Coffee [" + coffee.name + "] Attributes changed [" + actualFieldsToUpdate + "]")
    }

  }

}

object UpdateCoffeesAndRelationsActor {

  def props: Props =
    Props(new UpdateCoffeesAndRelationsActor)

  case class UpdateResults(results: String)
}