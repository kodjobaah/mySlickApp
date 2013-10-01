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
import java.io.IOException
import com.googlecode.javacpp._
import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_imgproc._
import com.googlecode.javacv.cpp.opencv_highgui._

import com.valtech.whatamidoing.actors.red5.services._

import java.awt.image.BufferedImage

class RTMPSender(username:String) extends Actor {

  import RTMPSender._
  val xuggler = Xuggler(username)
System.load("/usr/local/lib/cv2.so")                    
System.load("/usr/local/lib/libopencv_gpuarithm.so")      
System.load("/usr/local/lib/libopencv_gpu.so")        
System.load("/usr/local/lib/libopencv_ml.so")           
System.load("/usr/local/lib/libopencv_superres.so")
System.load("/usr/local/lib/libopencv_bioinspired.so")  
System.load("/usr/local/lib/libopencv_gpubgsegm.so")      
System.load("/usr/local/lib/libopencv_gpustereo.so")   
System.load("/usr/local/lib/libopencv_nonfree.so")      
System.load("/usr/local/lib/libopencv_video.so")
System.load("/usr/local/lib/libopencv_calib3d.so")     
System.load("/usr/local/lib/libopencv_gpucodec.so")      
System.load("/usr/local/lib/libopencv_gpuwarping.so")  
System.load("/usr/local/lib/libopencv_objdetect.so")   
System.load("/usr/local/lib/libopencv_videostab.so")
System.load("/usr/local/lib/libopencv_contrib.so")     
System.load("/usr/local/lib/libopencv_gpufeatures2d.so") 
System.load("/usr/local/lib/libopencv_highgui.so")    
System.load("/usr/local/lib/libopencv_optim.so")
System.load("/usr/local/lib/libopencv_core.so")        
System.load("/usr/local/lib/libopencv_gpufilters.so")    
System.load("/usr/local/lib/libopencv_imgproc.so")    
System.load("/usr/local/lib/libopencv_photo.so")
System.load("/usr/local/lib/libopencv_features2d.so")  
System.load("/usr/local/lib/libopencv_gpuimgproc.so")    
System.load("/usr/local/lib/libopencv_softcascade.so")
System.load("/usr/local/lib/libopencv_flann.so")        
System.load("/usr/local/lib/libopencv_gpuoptflow.so")    
System.load("/usr/local/lib/libopencv_legacy.so")     
System.load("/usr/local/lib/libopencv_stitching.so")
import com.googlecode.javacv._
import com.googlecode.javacv.cpp.avcodec
import com.googlecode.javacv.cpp.avcodec.AV_CODEC_ID_FLV1
import com.googlecode.javacv.cpp.avutil
import com.googlecode.javacv.cpp.avutil.AV_PIX_FMT_YUV420P 
import com.googlecode.javacv.cpp.opencv_core
import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_core.IplImage
Loader.load(classOf[opencv_core])
//Loader.load(classOf[avcodec])
//Loader.load(classOf[avutil])

  override def receive: Receive = {
    case RTMPMessage(message) => {
         import sun.misc.BASE64Decoder
         val base64: BASE64Decoder = new BASE64Decoder();
         val bytes64: Array[Byte] = base64.decodeBuffer(message);
         Logger("MyApp").info("Message length base64 after decode %d".format(bytes64.size))

         Logger("MyApp").info("Message length base64 after decode %d".format(bytes64.size))
	 import java.io.ByteArrayInputStream
	 val bais : ByteArrayInputStream  = new ByteArrayInputStream(bytes64)

	import javax.imageio.ImageIO
        try {
        var bufferedImage = ImageIO.read(bais);
/*
	 import java.nio.ByteBuffer
         import java.nio.ByteOrder
         var wrappedData = ByteBuffer.wrap(bytes64)
	 val bytePointer = new BytePointer(wrappedData)
         val image = cvCreateImageHeader(cvSize(352,288), IPL_DEPTH_8U, 1)
         image.imageData(bytePointer)

	val imgb = image.getBufferedImage()
        Logger("MyApp").info("--buffered image:"+imgb)
        import com.xuggle.xuggler.video.ConverterFactory
	val nimg = ConverterFactory.convertToType(imgb,BufferedImage.TYPE_3BYTE_BGR)
        Logger("MyApp").info("--converted buffered image:"+nimg)
*/
        Logger("MyApp").info("--converted buffered image:"+bufferedImage)
        xuggler.transmitBufferedImage(bufferedImage);
    	} catch {
		case ex: Throwable =>{
            		println(ex)
          	}
    	}

    }
  }
}

object RTMPSender {
  
  def props(username: String) = Props(new RTMPSender(username))

  case class RTMPMessage(val message: String)

}

