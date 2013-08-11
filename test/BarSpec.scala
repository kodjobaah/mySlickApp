import models.{Bars, Bar}

import org.scalatest.FlatSpec
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
//Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._

// Use the implicit threadLocalSession
import Database.threadLocalSession

import play.api.test._
import play.api.test.Helpers._

class BarSpec extends FlatSpec with ShouldMatchers {

      "A Bar" should 
         "be creatable" in {
       Database.forURL("jdbc:h2:mem:test1", driver="org.h2.Driver")
       	.withSession({
          
          Bars.ddl.create
          Bars.insert(Bar(None,"foo"))
	  val b = for(b <- Bars) yield b
          b.first.id.get should equal(1)

         })
       }
}