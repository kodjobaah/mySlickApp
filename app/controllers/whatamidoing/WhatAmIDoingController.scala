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
    
    val search = "match a:User where a.email = \""+em+"\" return a.password as password, a.email as email"
    var res = Cypher(search)
 
 	val response = res.apply().map(row => 	row[String]("password")).toList
 	Logger("My App").info("response:"+response)
 	
 	import org.mindrot.jbcrypt.BCrypt
 	
 	
 	for(i <- response) {
 		Logger("List Members").info(i)
 	}
 	
 	
 	var stuff = "Not Logged In"
 	if (response.size < 1) {
 		val pw_hash = BCrypt.hashpw(p, BCrypt.gensalt())
 	
  		val s= "create ("+fn+":User {email:\""+em+"\",password:\""+pw_hash+"\",firstName:\""+fn+"\",lastName:\""+ln+"\"})"
  		Logger("MyApp").info("this is: "+s)
    	val newRes = Cypher("""
    	
    	create ({fn}:User {email:"{em}",password:"{pw_hash}",firstName:"{fn}",lastName:"{ln}"},
    			{fn}:AuthenticationToken {token="{token}",valid={valid}},
    			{fn}User-[:HAS_TOKEN]->{fn}:AuthenticationToken)
    	""").on("fn"->fn, "em"->em, "pw_hash"->pw_hash,"ln"->ln,"token"->"token", "valid"->"valid").execute()
    	stuff = "New User"
    } else {
      
      val dbhash = response.head
      if (BCrypt.checkpw(p, dbhash)) {
      	stuff = "Logged In"
      } else {
        stuff = "Wrong Password"
      }
    }
    
    future(Ok(stuff)) 
  }
  
}
