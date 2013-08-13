package com.valtech.actors.supervisors

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase
import play.api.test.Helpers.running

import org.scalatest.WordSpec
import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory

import scala.concurrent.duration._

import com.valtech.actors.services._
import OptimisticLockSupervisor.UpdateCoffee
import children.UpdateCoffeesAndRelationsActor.UpdateResults
import models.{Coffee}

class OptimisticLockSupervisorSpec(_system: ActorSystem)  extends TestKit(_system) 
													      with WordSpec 
													      with ImplicitSender 
													      with MockFactory
													      with BeforeAndAfterAll {

  def this() = this(ActorSystem("test-system"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination()
  }

  
  "when UpdateCoffee message is sent to OptimsticLockSupervisor it" should {
    "send a UpdateResult message" in {
      import play.api.test.Helpers.running
      import play.api.test.FakeApplication
      import play.api.test.Helpers.inMemoryDatabase
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val coffee = Coffee("coffee",0,0.0,0,0,0)
        val accessDatabaseService = mock[AccessDatabaseService]
        val updatedFields = Seq("version").toList
        (accessDatabaseService.updateDatabase _).expects(coffee).returns(updatedFields)
        
        val optimisticLockSupervisor = system.actorOf(OptimisticLockSupervisor.props(accessDatabaseService))
        optimisticLockSupervisor ! UpdateCoffee(coffee)
        
        expectMsg(UpdateResults("Updated Coffee [coffee] Attributes changed ["+updatedFields.toString+"]"))
        
      }
    }

  }


}