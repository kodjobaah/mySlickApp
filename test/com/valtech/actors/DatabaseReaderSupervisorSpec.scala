package com.valtech.actors

import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import FetchCoffeesAndSuppliers.CoffeesAndSuppliers
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import play.api.test.FakeApplication
import play.api.test.Helpers.inMemoryDatabase
import play.api.test.Helpers.running
import com.valtech.actors.supervisors.DatabaseReaderSupervisor
import com.valtech.actors.supervisor.children.FetchCoffeesAndSuppliers

class DatabaseReaderSupervisorSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender with BeforeAndAfterAll {

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
        val databaseReaderSupervisor = system.actorOf(DatabaseReaderSupervisor.props)
        databaseReaderSupervisor ! ReadCoffeesAndSuppliers
        expectMsgType[CoffeesAndSuppliers](1 second)
      }
    }

  }

}