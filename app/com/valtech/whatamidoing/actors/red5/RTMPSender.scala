package com.valtech.whatamidoing.actors.red5

import akka.actor._
import scala.concurrent.duration._

import play.api._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.pattern.ask

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger

import com.valtech.whatamidoing.actors.red5.services._

class RTMPSender(username:String) extends Actor {

  import RTMPSender._
  val xuggler = Xuggler(username)
  override def receive: Receive = {
    case RTMPMessage(message) => {
         //import org.apache.commons.codec.binary.Base64
          //import com.Ostermiller.util.Base64
          import sun.misc.BASE64Decoder
//         Logger("MyApp").info("Message Beigin decoded %s".format(message))

         val messageDecoded: Array[Byte] = message.getBytes()
 //        Logger("MyApp").info("Message After decoded %s".format(messageDecoded))

         //val bytes64 = Base64.decodeBase64(messageDecoded)
         //val bytes64 = Base64.decodeToBytes(message)
         val base64: BASE64Decoder = new BASE64Decoder();

         Logger("MyApp").info("Message lenght base64 before decode %d".format(message.length()))
         val bytes64 = base64.decodeBuffer(message);
         Logger("MyApp").info("Message lenght base64 after decode %d".format(bytes64.size))

         xuggler.transmitFrame(bytes64)
    }
  }
}

object RTMPSender {
  
  def props(username: String) = Props(new RTMPSender(username))

  case class RTMPMessage(val message: String)

}

