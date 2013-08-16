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

import com.valtech.actors.services._
import com.valtech.actors.supervisors._
import children.UpdateCoffeesAndRelationsActor
import children.UpdateCoffeesAndRelationsActor._
import OptimisticLockSupervisor._
import models.{Coffee}

class OptimisticLockSupervisor(accessDatabaseService: AccessDatabaseService) extends  Actor with ActorLogging {
  
   // Map of pending children and orginal sender
  import akka.actor.ActorRef
  var pendingChildren = Map[ActorRef, ActorRef]()
  
	//Used by ?(ask)
   implicit val timeout = Timeout(5 seconds)
   val child = context.actorOf(UpdateCoffeesAndRelationsActor.props(accessDatabaseService), name = "update-coffees-relations")
   override def receive: Receive = {  
      case PerformOperation => sender ! OperationCompleted("Should be seeing this")
      case up: UpdateCoffee => {
        //val response: Future[UpdateResults] = ask(child,up).mapTo[UpdateResults]
        //response pipeTo sender
        pendingChildren += child -> sender
        child forward up
      }
    
    }
   
   override def supervisorStrategy = OneForOneStrategy() {
		case _: ActorInitializationException => Stop
		case _: ActorKilledException => Stop
		case _: RuntimeException => { 
					log.info("**************************** caught runtime exception*****************")
					log.info("******************* sender *******"+sender.toString)
					log.info(pendingChildren.get(sender).get.toString)
					pendingChildren.get(sender).get ! ProblemsWithUpdate
					Restart}
		case _: Exception => Restart
   
   }
   
   


}

object OptimisticLockSupervisor {

  def props(accessDatabaseService: AccessDatabaseService) : Props =
    Props(new OptimisticLockSupervisor(accessDatabaseService))
    
  case object PerformOperation
  case class OperationCompleted(message: String)
  case class UpdateCoffee(coffee: Coffee)
  case object ProblemsWithUpdate

  
}