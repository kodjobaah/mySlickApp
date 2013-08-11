package com.valtech.actors

import akka.actor.Actor
import akka.actor.Terminated
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import OptimisticLockSupervisor.PerformOperation
import OptimisticLockSupervisor.OperationCompleted
import OptimisticLockSupervisor.UpdateCoffee
import UpdateCoffeesAndRelationsActor.UpdateResults
import com.valtech.actors._
import models.{Coffee}
import akka.actor.{ ActorInitializationException,ActorKilledException,OneForOneStrategy}
import akka.actor.SupervisorStrategy._

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