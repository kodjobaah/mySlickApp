package controllers

import play.api._

import play.api.mvc._

import play.api.data.Form

import play.api.data._
import play.api.data.Forms._



import scala.slick.session.Session
import play.api.libs.json._

import models.{Bar, Bars}

import play.api.db._
import play.api.Play.current

// Use M

import scala.slick.driver.MySQLDriver.simple._

// Use the implicit threadLocalSession

import Database.threadLocalSession

object Application extends Controller {

	lazy val database = Database.forDataSource(DB.getDataSource())

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
	
   def listSuppliers = Action {
     Ok(views.html.suppliers())
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