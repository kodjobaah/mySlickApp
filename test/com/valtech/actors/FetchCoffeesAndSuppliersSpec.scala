package com.valtech.actors

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase
import scala.concurrent.duration._
import com.valtech.actors._
import FetchCoffeesAndSuppliers.CoffeesAndSuppliers
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import com.valtech.actors.supervisors.DatabaseReaderSupervisor
import com.valtech.actors.supervisor.children.FetchCoffeesAndSuppliers
class FetchCoffeesAndSuppliersSpec(_system: ActorSystem)  extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender with BeforeAndAfterAll {

  def this() = this(ActorSystem("test-system"))

  override def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination()
  }

  "When FetchCoffeesAndSuppliers is sent message ReadCoffeesAndSuppliers it" should {
    "respond with message CoffeesAndSuppliers" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val fetchCoffeesAndSupplierSupervisor = system.actorOf(FetchCoffeesAndSuppliers.props)
        fetchCoffeesAndSupplierSupervisor ! ReadCoffeesAndSuppliers
        expectMsgType[CoffeesAndSuppliers](1 second)
      }
    }

  }


}