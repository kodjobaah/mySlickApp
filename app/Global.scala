import play.api.db.DB
import play.api.GlobalSettings
// Use H2Driver to connect to an H2 database
import scala.slick.driver.MySQLDriver._

// Use the implicit threadLocalSession
//import Database.threadLocalSession

import play.api.Application
import play.api.Play.current
import models.Bars
import org.h2.tools.Server


object Global extends GlobalSettings {

   override def onStart(app: Application) {

     /*
		 val server: Server = Server.createWebServer(
				"-trace").start();
		 
		 Console.printf("---------------------------------"+server.getPort().toString)
	
	* 
	
     lazy val database = Database.forDataSource(DB.getDataSource())

        database.withSession {
           Bars.ddl.create
        }
        * 
        */
   }

}
