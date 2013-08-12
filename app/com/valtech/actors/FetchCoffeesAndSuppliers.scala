package com.valtech.actors

import akka.actor.Actor
import akka.actor.Props


import scala.slick.session.Session

import play.api.db._
import play.api.Play
import play.api.Play.current
import play.api.cache.Cache

import models.{ Coffees, Coffee, Supplier, Suppliers, CoffeeVersions, CoffeeVersion }
import com.valtech.utils.CacheVariables

class FetchCoffeesAndSuppliers extends Actor {

  import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers

  override def receive: Receive = {
    case ReadCoffeesAndSuppliers => {

      
      import com.typesafe.slick.driver.oracle.OracleDriver.simple._
      import Database.threadLocalSession
	
      lazy val database = Database.forDataSource(DB.getDataSource(Play.configuration.getString("mySlickApp.database").get))
      val coffees = database withSession {
        val cofs = for (c <- Coffees) yield c
        cofs.list
      }
      val suppliers = database withSession {
        val sups = for (s <- Suppliers) yield s
        sups.list
      }
      val coffeeVersions = database withSession {
        val coffVersions = for (c <- CoffeeVersions) yield c
        coffVersions.list.sortBy(_.version).reverse
      }


      Cache.set(CacheVariables.coffees, (coffees, suppliers))

      import FetchCoffeesAndSuppliers.CoffeesAndSuppliers

      sender ! CoffeesAndSuppliers(coffees, suppliers, coffeeVersions)

    }
  }
}

object FetchCoffeesAndSuppliers {

  def props: Props =
    Props(new FetchCoffeesAndSuppliers)

  case class CoffeesAndSuppliers(coffeesAndSuppliers: Tuple3[List[Coffee], List[Supplier], List[CoffeeVersion]])

}