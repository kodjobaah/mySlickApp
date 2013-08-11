package controllers


import play.api.mvc.Controller
import play.api.libs.concurrent.Akka
import play.api.db._
import play.api.Play
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.cache.Cache
import play.api.mvc.Action

import com.typesafe.slick.driver.oracle.OracleDriver.simple._
import Database.threadLocalSession

import akka.util.Timeout
import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.pattern.ask

import scala.util.Success
import scala.util.Failure
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.future
import scala.slick.session.Session
import scala.concurrent.duration._

import com.valtech.actors.OptimisticLockSupervisor
import com.valtech.actors.OptimisticLockSupervisor._
import com.valtech.actors.UpdateCoffeesAndRelationsActor._
import com.valtech.utils.CacheVariables
import models._

object ActorController extends Controller {

	val system = ActorSystem("optimistic-lock-supervisor-system")
	val optimisticLockSupervisor = system.actorOf(OptimisticLockSupervisor.props, "optimistic-lock-supervisor")

	
	//Used by ?(ask)
	implicit val timeout = Timeout(5 seconds)
	
	
	val coffeeForm = Form(
       mapping(
         "name" -> text,
         "supID" -> number,
         "price" -> of[Double],
         "sales" -> number,
          "total" -> number,
          "version" -> number
       )(Coffee.apply)(Coffee.unapply)
     )
     
	def performOperation = Action { implicit request =>
	   var coffee = coffeeForm.bindFromRequest.get 
		
	   import play.api.libs.concurrent.Execution.Implicits._
	   val response: Future[UpdateResults] = ask(optimisticLockSupervisor,UpdateCoffee(coffee)).mapTo[UpdateResults]
	   Async {
		       response flatMap { case UpdateResults(results) => {
		         val allSupplierCoffees = getAllCoffeesWithSuppliers()
		         future(Ok(views.html.updateCoffees(results,coffeeForm,allSupplierCoffees._1,allSupplierCoffees._2, allSupplierCoffees._3))
	   			.withHeaders(CACHE_CONTROL -> "no-cache"))
	   	       }
     
		   }
	   } 
			
	}
     
     
     lazy val database = Database.forDataSource(DB.getDataSource(Play.configuration.getString("mySlickApp.database").get))
     def getCoffees = Action {
	   val allSupplierCoffees = getAllCoffeesWithSuppliers();	
       Ok(views.html.updateCoffees("none",coffeeForm,allSupplierCoffees._1,allSupplierCoffees._2, allSupplierCoffees._3))
	   			.withHeaders(CACHE_CONTROL -> "no-cache")

	}
     
    def getAllCoffeesWithSuppliers (): Tuple3[List[Coffee], List[Supplier], List[CoffeeVersion]] = {
       val coffees = database withSession {
		   val cofs = for(c <- Coffees) yield c
		   cofs.list
	  }
	   val suppliers = database withSession {
	       val sups = for(s <- Suppliers) yield s
	       sups.list
	   }
	   
	   val coffeeVersions = database withSession {
	     val coffVersions = for(c <- CoffeeVersions) yield c
	      coffVersions.list.sortBy(_.version).reverse
	   }
	   
	   Cache.set(CacheVariables.coffees, (coffees,suppliers))
	   (coffees,suppliers,coffeeVersions)
    } 

}
