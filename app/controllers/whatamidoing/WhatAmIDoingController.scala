package controllers.whatamidoing

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.Logger
import scala.concurrent._
import scala.concurrent.future

object WhatAmIDoingController extends Controller {

  
  def whatAmIdoing = Action { implicit request =>
  	Ok(views.html.whatamidoing.whatamidoing())
    
  }
  
  def invite(email: String) = Action { implicit request =>
  	
  	
  import com.valtech.mail._
  import com.valtech.mail.mailer._
 
  send a new Mail (
    from = ("kodjobaah@gmail.com", "What Am I doing!!"),
    to = email,
    subject = "What Am I Doing",
    message = "Click on the link http://5.79.24.141:9000/whatamidoing "
  )
   Logger("MyApp").info("sending email to =:" + email)
  
  	Ok("done")
  }
  
  import ExecutionContext.Implicits.global
  def registerLogin(email: Option[String], password: Option[String], repeatPassword: Option[String], firsName: Option[String], lastName: Option[String]) =    
  Action.async { implicit request =>
  
  	import org.anormcypher._
  	
  	
    Cypher("""create (user {email:email,password: password, firstName: firstName, lastName: lastName })""").execute()
    
    future(Ok("loggedIn")) 
  }
  
}
