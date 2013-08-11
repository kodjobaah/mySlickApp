package com.valtech.actors

import play.api.db._
import play.api.Play
import play.api.Play.current
import play.api.cache.Cache
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import akka.actor.Terminated
import com.typesafe.slick.driver.oracle.OracleDriver.simple._
import Database.threadLocalSession
import models.{ Coffees, Coffee, Supplier, CoffeeVersions, CoffeeVersion }
import com.valtech.utils.CacheVariables
import services.CoffeesService
import scala.slick.session.Session
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import scala.reflect.runtime.universe._
import play.Configuration

class UpdateCoffeesAndRelationsActor extends Actor with ActorLogging {

  import com.valtech.actors.OptimisticLockSupervisor.UpdateCoffee
  import UpdateCoffeesAndRelationsActor.UpdateResults
  override def receive: Receive = {
    case UpdateCoffee(coffee) => {

      
      
      lazy val database = Database.forDataSource(DB.getDataSource(Play.configuration.getString("mySlickApp.database").get))
      val fieldsToUpdate = CoffeesService().getFieldsToUpdate(coffee)
      val actualFieldsToUpdate = fieldsToUpdate :+ "version"
      database withSession {
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
        
        //Updating the coffeeversions
        val coffeeVersion = CoffeeVersion(newVersion, coffee.name)
        CoffeeVersions insert coffeeVersion

      }
      sender ! UpdateResults("Updated Coffee ["+coffee.name+"] Attributes changed ["+actualFieldsToUpdate+"]")
    }
    
     }
    
}

object UpdateCoffeesAndRelationsActor {

  def props: Props =
    Props(new UpdateCoffeesAndRelationsActor)

  case class UpdateResults(results: String)
}