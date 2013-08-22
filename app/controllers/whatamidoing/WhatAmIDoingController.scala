package controllers.whatamidoing

import play.api.mvc.Controller
import play.api.mvc.Action

object WhatAmIDoingController extends Controller {

  
  def whatAmIdoing = Action { implicit request =>
  	Ok(views.html.whatamidoing.whatamidoing())
    
  }
  
}