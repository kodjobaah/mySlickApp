package controllers.whatamidoing

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.Logger

object WhatAmIDoingController extends Controller {

  
  def whatAmIdoing = Action { implicit request =>
  	Ok(views.html.whatamidoing.whatamidoing())
    
  }
  
  def invite(email: String) = Action { implicit request =>
  	
  	
  import com.valtech.mail._
  import com.valtech.mail.mail._
 
  send a new Mail (
    from = ("kodjobaah@gmail.com", "John Smith"),
    to = email,
    subject = "What Am I Doing",
    message = "Click on the link http://5.79.24.141:9000/whatamidoing "
  )
   Logger("MyApp").info("sending email to =:" + email)
  
  	Ok("done")
  }
  
}