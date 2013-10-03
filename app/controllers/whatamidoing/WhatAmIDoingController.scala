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
  def registerLogin(email: Option[String], password: Option[String], repeatPassword: Option[String], firstName: Option[String], lastName: Option[String]) =    
  Action.async { implicit request =>
  
  	import org.anormcypher._
  	
    val em = email.get
    val p = password.get
    val rp = repeatPassword.get
    val fn = firstName.get
    val ln = lastName.get
    
    val search = "match a:User where a.email = \""+em+"\" return a.password as pasword, a.email as email"
    var res = Cypher(search)
 
 	val response = res.apply().map(row => 	row[String]("password")).toList
 	Logger("My App").info("response:"+res)
 	if (response.size > 0) {
  		val s= "create ("+em+":User {email:\""+em+"\",password:\""+p+"\",firstName:\""+fn+"\",lastName:\""+ln+"\"})"
  		Logger("MyApp").info("this is: "+s)
    	val newRes = Cypher(s).execute()
    }
    Logger("MyApp").info("isponse:"+res)
    
    future(Ok("loggedIn")) 
  }
  
}
