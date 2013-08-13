package com.valtech.actors.supervisors.children

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import org.scalamock.scalatest.MockFactory
import scala.concurrent.duration._

import play.api.test.Helpers.running
import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase

import com.valtech.actors.supervisors.{children, DatabaseReaderSupervisor}
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import children.FetchCoffeesAndSuppliers.CoffeesAndSuppliers
import com.valtech.actors.services.AccessDatabaseService
import models._

class FetchCoffeesAndSuppliersSpec(_system: ActorSystem)  extends TestKit(_system) 
														  with WordSpec 
														  with ImplicitSender 
														  with MockFactory
														  with BeforeAndAfterAll {

  def this() = this(ActorSystem("test-system"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination()
  }

  "When FetchCoffeesAndSuppliers is sent message ReadCoffeesAndSuppliers it" should {
    "respond with message CoffeesAndSuppliers" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    	
        val accessDatabaseService = mock[com.valtech.actors.services.AccessDatabaseService]
        val listOfCoffees = Seq(Coffee("h",0,0.0,0,0,0))
        val listOfSuppliers = Seq(Supplier(0,"","","","",""))
        val listOfCoffeeVersions = Seq(CoffeeVersion(0,"h"))
         val coffeWithSuppliers = ((listOfCoffees.toList,listOfSuppliers.toList, listOfCoffeeVersions.toList))
        (accessDatabaseService.fetchCoffeesAndSuppliers _).expects().returns((listOfCoffees.toList,listOfSuppliers.toList, listOfCoffeeVersions.toList))
        
        val fetchCoffeesAndSupplierSupervisor = system.actorOf(FetchCoffeesAndSuppliers.props(accessDatabaseService))
        fetchCoffeesAndSupplierSupervisor ! ReadCoffeesAndSuppliers
        expectMsg(CoffeesAndSuppliers(coffeWithSuppliers))
      }
    }

  }


}