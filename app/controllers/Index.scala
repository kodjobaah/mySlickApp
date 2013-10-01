package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.Logger

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import com.xuggle.mediatool.IMediaReader
import com.xuggle.mediatool.MediaListenerAdapter
import com.xuggle.mediatool.ToolFactory
import com.xuggle.mediatool.event.IVideoPictureEvent
import com.xuggle.xuggler.Global

import akka.actor.ActorSystem

import com.valtech.whatamidoing.actors.red5._
object Index extends Controller {
    val system = ActorSystem("rtmpsender-system")
  
  //NOTE: Should we just be passing one database access service..or should each actor get a copy of their own
  val rtmpSender = system.actorOf(RTMPSender.props("hey"), "rtmpsender")

  
  

  def page = Action {implicit request =>
  /* 
    val response = Assets.at("/public/test", "comet.html" )(request)
    response.withHeaders(CONTENT_DISPOSITION -> s"""attachment; filename=comet.html """)
  
  * 
  */
  //val adminPageFileString: String = {
  // In prod builds, the file is embedded in a JAR, and accessing it via
  // an URI causes an IllegalArgumentException: "URI is not hierarchical".
  // So use a stream instead.
	//	  val adminPageStream: java.io.InputStream =
		//		  this.getClass().getResourceAsStream("/public/test/comet.html")
			//	  io.Source.fromInputStream(adminPageStream).mkString("")
//  }
//  Ok(adminPageFileString) as HTML
  	Ok(views.html.comet())
  }
 
  import play.api.mvc.WebSocket
  import play.api.libs.iteratee.Iteratee
  import play.api.libs.iteratee.Enumerator
  import play.api.libs.concurrent.Execution.Implicits._
  import scala.concurrent.Future
  var v = 0
  def publishVideo(username: String) = WebSocket.async[String] { request => 
  // Log events to the console
    v = v + 1
    import com.valtech.whatamidoing.actors.red5.RTMPSender._
    val in = Iteratee.foreach[String](s => {
      //Logger("MyApp").info("Log established %d".format(username.length()))
      rtmpSender ! RTMPMessage(s)
     
    }).mapDone { _ =>
        println("Disconnected")
    }
  
  // Send a single 'Hello!' message
  val out = Enumerator("Hello!")
  
  Future((in, out))
}
  
}
