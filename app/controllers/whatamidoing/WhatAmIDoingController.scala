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
  	import com.valtech.utils.CypherBuilder
  	
    val em = email.get
    val p = password.get
    val rp = repeatPassword.get
    val fn = firstName.get
    val ln = lastName.get
    
    var res = Cypher(CypherBuilder.searchForUser(em))
    val response = res.apply().map(row => 	row[String]("password")).toList
 	
 	Logger("My App").info("response:"+response)
 	
 	import org.mindrot.jbcrypt.BCrypt
 
 	var stuff = "Not Logged In"
 	if (response.size < 1) {
 		val pw_hash = BCrypt.hashpw(p, BCrypt.gensalt())
 		val newRes = Cypher(CypherBuilder.createUser(fn,ln,em,pw_hash)).execute();
 		
        val token = java.util.UUID.randomUUID.toString  
        val valid = "true"
 		
 		val createToken = Cypher(CypherBuilder.createToken(token,valid)).execute();
     	val linkToken = Cypher(CypherBuilder.linkUserToToken(em,token)).execute();
	   
		Logger("MyApp").info("this is one: "+newRes)
		Logger("MyApp").info("this is two: "+createToken)
		Logger("MyApp").info("this is three: "+linkToken)
    	stuff = "New User"
    	
    } else {
      
      	val dbhash = response.head
      	if (BCrypt.checkpw(p, dbhash)) {
      		val tokens = Cypher(CypherBuilder.getTokenForUser(em)).apply().map(row => (row[String]("token"),row[String]("status"))).toList
      	
      		Logger("MyApp").info("this is the authentication token"+tokens)
      		stuff = "Logged In"
      	} else {
        	stuff = "Wrong Password"
      	}
    }
    
    future(Ok(stuff)) 
  }
  
}
