package com.valtech.actors.services

import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
//import com.typesafe.slick.driver.oracle.OracleDriver.simple._
//import Database.threadLocalSession
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import play.api.db._
import play.api.Play
import play.api.Play.current

import models.{Supplier, Suppliers, Coffee, Coffees, CoffeeVersion, CoffeeVersions }

case class AccessDatabaseService(database: Database) {

  def getCoffeesService() = CoffeesService()
  def this() = this(null)
  def updateDatabase(coffee: Coffee): List[String] = {
    val fieldsToUpdate = getCoffeesService().getFieldsToUpdate(coffee)
    val actualFieldsToUpdate = fieldsToUpdate :+ "version"
    database withTransaction {
      val version = Q.queryNA[Int]("select max(version) from coffees")
      val newVersion = version.first() + 1;
      actualFieldsToUpdate foreach {
        case "name" => {
          val q = for { c <- Coffees if c.name === coffee.name }
            yield (c.name)
          q.update(coffee.name)
        }

        case "price" => {
          val q = for { c <- Coffees if c.name === coffee.name }
            yield (c.price)
          q.update(coffee.price)
        }

        case "sales" => {
          val q = for { c <- Coffees if c.name === coffee.name }
            yield (c.sales)
          q.update(coffee.sales)
        }

        case "supID" => {
          val q = for { c <- Coffees if c.name === coffee.name }
            yield (c.supID)
          q.update(coffee.supID)
        }

        case "total" => {
          val q = for { c <- Coffees if c.name === coffee.name }
            yield (c.total)
          q.update(coffee.total)
        }

        case "version" => {
          val q = for { c <- Coffees if c.name === coffee.name }
            yield (c.version)
          q.update(newVersion)

        }
      }
      println("********************************************* sleeping**********************")
      //Thread.sleep(5000)
      //throw new RuntimeException()
      println("********************************************* woken up ***********************")
      //Updating the coffeeversions
      val coffeeVersion = CoffeeVersion(newVersion, coffee.name)
      CoffeeVersions insert coffeeVersion

    }
    actualFieldsToUpdate
  }
  
  
  def fetchCoffeesAndSuppliers: Tuple3[List[Coffee],List[Supplier],  List[CoffeeVersion]] = {
    
    val coffees = database withTransaction {
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
      (coffees,suppliers,coffeeVersions)
  }
}

object AccessDatabaseService {
  lazy val database = Database.forDataSource(DB.getDataSource(Play.configuration.getString("mySlickApp.database").get))

  def apply() = new AccessDatabaseService(database);
}