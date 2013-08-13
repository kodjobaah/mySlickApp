
import org.specs2.mutable._

import org.scalatest.WordSpec

import play.api.test.Helpers.running
import play.api.test.Helpers.FIREFOX
import play.api.test._
import play.api.test.Helpers.await
import play.api.test.Helpers.wsUrl
import play.api.libs.ws.Response

class IndexPageSpec extends WordSpec {

  "Application" should {
    
    	"work from within a browser" in  {
    		running(TestServer(3333)) {
    			await(wsUrl("http://localhost")(3333).get).statusText contains("OK")

    		}
    	}
      /*
	"work from within a browser" in new WithBrowser(webDriver = FIREFOX, app = FakeApplication(), port = 9000) {
	    browser.goTo("/")
	    
	}

	*/
	
  }
	  
}