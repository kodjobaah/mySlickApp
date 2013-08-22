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

import com.valtech.whatamidoing.actors.red5.services._

class RTMPSender(username:String) extends Actor {

  import RTMPSender._
  val xuggler = Xuggler(username)
  override def receive: Receive = {
    case RTMPMessage(message) => {
         import org.apache.commons.codec.binary.Base64
         println("decoding sending message:"+message.length())
         val messageDecoded: Array[Byte] = message.getBytes()
         println("decoded message:"+messageDecoded.length)
         val bytes64 = Base64.decodeBase64(messageDecoded)
         xuggler.transmitFrame(bytes64)
    }
  }
}

object RTMPSender {
  
  def props(username: String) = Props(new RTMPSender(username))

  case class RTMPMessage(val message: String)

}

