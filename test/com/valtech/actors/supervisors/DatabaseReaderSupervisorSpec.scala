package com.valtech.actors.supervisors

import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit

import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase
import play.api.test.Helpers.running

import scala.concurrent.duration.DurationInt

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec

import org.scalamock.scalatest.MockFactory

import com.valtech.actors._
import services.AccessDatabaseService
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import children.FetchCoffeesAndSuppliers.CoffeesAndSuppliers
import models._

class DatabaseReaderSupervisorSpec(_system: ActorSystem) extends TestKit(_system) 
														 with WordSpec 
														 with ImplicitSender
														 with MockFactory
														 with BeforeAndAfterAll {

  def this() = this(ActorSystem("test-system"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination()
  }

  
  "DatabaseReaderSupervisor" should {
    "receive Message ReadCoffeesAndSuppliers" in {
      import play.api.test.Helpers.running
      import play.api.test.FakeApplication
      import play.api.test.Helpers.inMemoryDatabase
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val accessDatabaseServices = mock[com.valtech.actors.services.AccessDatabaseService]
        
        val listOfCoffees = Seq(Coffee("h",0,0.0,0,0,0))
        val listOfSuppliers = Seq(Supplier(0,"","","","",""))
        val listOfCoffeeVersions = Seq(CoffeeVersion(0,"h"))
        val coffeWithSuppliers = ((listOfCoffees.toList,listOfSuppliers.toList, listOfCoffeeVersions.toList))
        (accessDatabaseServices.fetchCoffeesAndSuppliers _).expects().returns(coffeWithSuppliers)
        
        val databaseReaderSupervisor = system.actorOf(DatabaseReaderSupervisor.props(accessDatabaseServices))
        databaseReaderSupervisor ! ReadCoffeesAndSuppliers
        
        expectMsg(CoffeesAndSuppliers(coffeWithSuppliers))
        
      }
    }

  }

}