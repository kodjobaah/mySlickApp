package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers.running
import play.api.test.Helpers.FIREFOX
import org.scalatest.WordSpec
/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
class IntegrationSpec extends WordSpec {
  
  "Application" should {
    
    "work from within a browser" in {
      running(TestServer(3333), FIREFOX) { browser =>

        browser.goTo("http://localhost:3333/")
        play.api.test.TestBrowser
        browser.pageSource contains("name")
       
      }
    }
    
  }
  
}