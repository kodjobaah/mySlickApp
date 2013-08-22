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
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import play.api.libs.iteratee.Enumerator

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
	
	
def comet = Action {
	
//	val events = Enumerator(
//     """<script>console.log('kiki')</script>""",
//     """<script>console.log('foo')</script>""",
//     """<script>console.log('bar')</script>"""
//  )
//  Ok.stream(events >>> Enumerator.eof).as(HTML)
  
 import play.api.libs.Comet
  val events = Enumerator("kiki", "foo", "bar")
  Ok.stream(events &> Comet(callback = "parent.cometMessage"))

}



 
}