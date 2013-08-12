package com.valtech.actors

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import play.api.Play.current
import play.api.test._
import play.core.StaticApplication
import play.test.WithApplication
import play.test.Helpers.inMemoryDatabase

import scala.concurrent.duration._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike
import org.scalatest.Matchers

import com.valtech.actors._
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers

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
        expectMsg(ReadCoffeesAndSuppliers)
      }
    }

  }

}