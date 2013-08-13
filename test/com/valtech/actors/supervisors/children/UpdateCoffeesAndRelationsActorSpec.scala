package com.valtech.actors.supervisors.children

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase
import play.api.test.Helpers.running

import scala.concurrent.duration._

import org.scalatest.WordSpec
import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory

import com.valtech.actors.supervisors._
import OptimisticLockSupervisor.UpdateCoffee
import com.valtech.actors.supervisors.children._
import UpdateCoffeesAndRelationsActor.UpdateResults
import models.{Coffee, Supplier, CoffeeVersion}
import com.valtech.actors.services._


class UpdateCoffeesAndRelationsActorSpec(_system: ActorSystem) extends TestKit(_system) 
						with WordSpec 
						with ImplicitSender
						with MockFactory
						with BeforeAndAfterAll {

  def this() = this(ActorSystem("test-system"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination()
  }
  
  
  "when UpdateCoffee message is sent to UpdateCoffeesAndRelationsActor it" should {
    "send a UpdateResult message" in {
      import play.api.test.Helpers.running
      import play.api.test.FakeApplication
      import play.api.test.Helpers.inMemoryDatabase
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val coffee = Coffee("coffee",0,0.0,0,0,0)
        
        val accessDatabaseService = mock[AccessDatabaseService]
        val updatedFields = Seq("version").toList
        (accessDatabaseService.updateDatabase _).expects(coffee).returns(updatedFields)
             
        val updateCoffeesAndSuppliers = system.actorOf(UpdateCoffeesAndRelationsActor.props(accessDatabaseService))
        
        updateCoffeesAndSuppliers ! UpdateCoffee(coffee)
        
        expectMsg(UpdateResults("Updated Coffee [coffee] Attributes changed [List(version)]"))
      
      }
    }

  }



}