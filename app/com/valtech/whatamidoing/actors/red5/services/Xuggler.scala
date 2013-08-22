package com.valtech.whatamidoing.actors.red5.services

import com.xuggle.xuggler.IStreamCoder
import com.xuggle.xuggler.IContainer
import com.xuggle.xuggler.IContainerFormat
import com.xuggle.xuggler.IStream
import com.xuggle.xuggler.IVideoResampler
import com.xuggle.xuggler.IAudioResampler
import com.xuggle.xuggler.ICodec
import com.xuggle.xuggler.IPixelFormat
import com.xuggle.xuggler.IRational
import com.xuggle.xuggler.IPacket
import com.xuggle.xuggler.IVideoPicture
import com.xuggle.xuggler.IAudioSamples
import com.xuggle.mediatool.IMediaWriter
import com.xuggle.mediatool.ToolFactory

import play.Logger

class Xuggler(outputUrl: String) {

  //Accessing the constants
  import Xuggler._

  val mediaWriter: IMediaWriter =
               ToolFactory.makeWriter("rtmp://localhost:1935/oflaDemo/kodjo.flv")
      mediaWriter.addVideoStream(0, 0, ICodec.ID.CODEC_ID_FLV1,640, 480);
	 
	  val startTime = System.nanoTime();
	 
	  def transmitFrame(frame: Array[Byte]) = {

	    // convert byte array back to BufferedImage
	    import java.io.InputStream
	    import java.io.ByteArrayInputStream
	    import java.awt.image.BufferedImage
	    import javax.imageio.ImageIO
	    import java.util.concurrent.TimeUnit
		val in: InputStream  = new ByteArrayInputStream(frame);
		
	    val bImageFromConvert: BufferedImage  = ImageIO.read(in);
	    println("buffered image:"+bImageFromConvert)
 	 
	    mediaWriter.encodeVideo(0, bImageFromConvert, System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
	  
	  }

}

object Xuggler {
  
  def apply(username: String) = new Xuggler(username)

}