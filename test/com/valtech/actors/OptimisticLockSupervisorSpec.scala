package com.valtech.actors

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import scala.concurrent.duration._
import com.valtech.actors._
import OptimisticLockSupervisor.UpdateCoffee
import UpdateCoffeesAndRelationsActor.UpdateResults
import models.{Coffee}
import com.valtech.actors.supervisors.OptimisticLockSupervisor
import com.valtech.actors.supervisor.children.UpdateCoffeesAndRelationsActor

class OptimisticLockSupervisorSpec(_system: ActorSystem)  extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender with BeforeAndAfterAll {

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
        val optimisticLockSupervisor = system.actorOf(OptimisticLockSupervisor.props)
        optimisticLockSupervisor ! UpdateCoffee(Coffee("",0,0,0,0,0))
        expectMsgType[UpdateResults](3 second)
      }
    }

  }


}