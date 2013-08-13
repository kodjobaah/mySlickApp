package com.valtech.actors.supervisors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.util.Timeout
import akka.actor.{ ActorInitializationException,ActorKilledException,OneForOneStrategy}
import akka.actor.SupervisorStrategy._
import akka.actor.actorRef2Scala
import akka.pattern.{ask, pipe}

import scala.concurrent.Future
import scala.concurrent.duration._

import play.api.libs.concurrent.Execution.Implicits._

import com.valtech.actors.supervisors._
import children.UpdateCoffeesAndRelationsActor
import children.UpdateCoffeesAndRelationsActor._
import OptimisticLockSupervisor._
import models.{Coffee}

class OptimisticLockSupervisor extends  Actor with ActorLogging {
  
	//Used by ?(ask)
   implicit val timeout = Timeout(5 seconds)
   val child = context.actorOf(UpdateCoffeesAndRelationsActor.props, name = "update-coffees-relations")
   override def receive: Receive = {  
      
      case PerformOperation => sender ! OperationCompleted("Should be seeing this")
      case up: UpdateCoffee => {
        val response: Future[UpdateResults] = ask(child,up).mapTo[UpdateResults]
        response pipeTo sender
      }
    
    }
   
   override def supervisorStrategy = OneForOneStrategy() {
		case _: ActorInitializationException => Stop
		case _: ActorKilledException => Stop
		case _: Exception => Restart
   
   }
   
   


}

object OptimisticLockSupervisor {

  def props : Props =
    Props(new OptimisticLockSupervisor)
    
  case object PerformOperation
  case class OperationCompleted(message: String)
  case class UpdateCoffee(coffee: Coffee)

  
}