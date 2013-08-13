package com.valtech.actors.supervisors.children


import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props

import com.valtech.actors.services._
import com.valtech.actors.supervisors.OptimisticLockSupervisor.UpdateCoffee
import com.valtech.actors.supervisors.children.UpdateCoffeesAndRelationsActor.UpdateResults

class UpdateCoffeesAndRelationsActor(accessDatabaseService: AccessDatabaseService) extends Actor with ActorLogging {
  override def receive: Receive = {
    case UpdateCoffee(coffee) => {
      val actualFieldsToUpdate = accessDatabaseService.updateDatabase(coffee)
      sender ! UpdateResults("Updated Coffee [" + coffee.name + "] Attributes changed [" + actualFieldsToUpdate + "]")
    }

  }

}

object UpdateCoffeesAndRelationsActor {

  def props(accessDatabaseService: AccessDatabaseService): Props =
    Props(new UpdateCoffeesAndRelationsActor(accessDatabaseService))

  case class UpdateResults(results: String)
}