package com.valtech.actors.supervisors.children

import akka.actor.Actor
import akka.actor.Props
import play.api.db._
import play.api.Play.current
import play.api.cache.Cache
import models.{Coffee, Supplier, CoffeeVersion}
import com.valtech.utils.CacheVariables
import com.valtech.actors.services._
import com.valtech.actors.supervisors._
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import akka.actor.actorRef2Scala

class FetchCoffeesAndSuppliers extends Actor {

  import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers

  override def receive: Receive = {
    case ReadCoffeesAndSuppliers => {

      val coffeesAndServices: Tuple3[List[Coffee],List[Supplier], List[CoffeeVersion]] = AccessDatabaseService().fetchCoffeesAndSuppliers
      Cache.set(CacheVariables.coffees, (coffeesAndServices._1, coffeesAndServices._2))

      import FetchCoffeesAndSuppliers.CoffeesAndSuppliers
      sender ! CoffeesAndSuppliers(coffeesAndServices)

    }
  }
}

object FetchCoffeesAndSuppliers {

  def props: Props =
    Props(new FetchCoffeesAndSuppliers)

  case class CoffeesAndSuppliers(coffeesAndSuppliers: Tuple3[List[Coffee], List[Supplier], List[CoffeeVersion]])

}