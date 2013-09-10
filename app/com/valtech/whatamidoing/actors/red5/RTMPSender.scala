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

import com.googlecode.javacpp._
import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_imgproc._
import com.googlecode.javacv.cpp.opencv_highgui._

import com.valtech.whatamidoing.actors.red5.services._

import java.awt.image.BufferedImage

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

         Logger("MyApp").info("Message length base64 before decode %d".format(message.length()))
         val bytes64: Array[Byte] = base64.decodeBuffer(message);
         Logger("MyApp").info("Message length base64 after decode %d".format(bytes64.size))
	 import java.nio.ByteBuffer
         import java.nio.ByteOrder
         var wrappedData = ByteBuffer.wrap(bytes64)
	Logger("wrapped data").info("--wrapped datea:"+wrappedData)
         var mat = cvMat(1, bytes64.length,CV_BGRA2BGR, new BytePointer(wrappedData))
         Logger("mata data").info("-- mat dat--"+mat)
         var image = cvDecodeImage(mat)
         Logger("MyApp").info("---- image--:"+image)
	if (image != null) {
         val c: BufferedImage  = image.getBufferedImage()
             xuggler.transmitBufferedImage(c)
         }

         //xuggler.transmitFrame(bytes64)
    }
  }
}

object RTMPSender {
  
  def props(username: String) = Props(new RTMPSender(username))

  case class RTMPMessage(val message: String)

}

