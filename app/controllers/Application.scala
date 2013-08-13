package controllers

import play.api._

import play.api.mvc._

import play.api.data.Form

import play.api.data._
import play.api.data.Forms._



import scala.slick.session.Session
import play.api.libs.json._

import models.{Bar, Bars, Coffee, Coffees, Suppliers}

import play.api.db._
import play.api.Play.current

// Use M

import scala.slick.driver.MySQLDriver.simple._
//import com.typesafe.slick.driver.oracle.OracleDriver.simple._
// Use the implicit threadLocalSession

import Database.threadLocalSession

object Application extends Controller {

  //lazy val database = Database.forDataSource(DB.getDataSource())
 lazy val database = Database.forDataSource(DB.getDataSource("oracle"))

	val barForm = Form (
		mapping(
		  "name" -> text
		)
		 ((name) => Bar(None, name))

		 ((bar: Bar) => Some(bar.name))
	)

	def index = Action {
		Ok(views.html.index(barForm))

	}
	
  
	def listCoffees = Action {
	  val coffees = database withSession {
	    val cof = for(c <- Coffees)
	      yield c
	    cof.list
	       
	  }
	 
	  var isolationLevel: String = ""
	  val coffeeSuppliers = database withTransaction {
	      import java.sql.Connection
	    isolationLevel = threadLocalSession.conn.getTransactionIsolation() match {
	     case Connection.TRANSACTION_NONE => "NO Transaction"
	     case Connection.TRANSACTION_READ_COMMITTED => "Read Commited"
	     case Connection.TRANSACTION_REPEATABLE_READ => "Repeatable Read"
	     case Connection.TRANSACTION_SERIALIZABLE => "Serializable"
	   }
	    
	    val q2 = for {
	      c <- Coffees if c.price < 9.0
	      s <- Suppliers if s.id === c.supID
	    } yield (c.name, s.name)
	    
	    q2.list
	  }
	  
	  val q = Query(Coffees)
	  
	  val stmnt = q.selectStatement
	  
	  Ok(views.html.coffees(coffees, coffeeSuppliers,isolationLevel,stmnt))
	}

	def addBar = Action {
		implicit request =>
		barForm.bindFromRequest.value map {
			bar =>
				database withSession {
					(Bars insert bar)
				}
			    val bars = database withSession { 
			      var tempBars = for( b <- Bars) yield b
			      tempBars.list
			    }
			  
				Ok(views.html.bar(bars))
				//Redirect(routes.Application.getBars)
		} getOrElse BadRequest
	}

	def getBars = Action {
	  
	  val json = database withSession {
			val bars = for (b <- Bars) yield b.name
			Json.toJson(bars.list)
		}
		Ok(json).as(JSON)

	}

 
}