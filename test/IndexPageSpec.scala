

import org.fluentlenium.adapter.FluentTest
import org.scalatest.ShouldMatchers
import org.scalatest.WordSpec
import play.api.test.WithBrowser
import play.api.test.TestBrowser
import play.api.test.Helpers.running
import play.api.test.Helpers.FIREFOX
import org.scalatest.FunSuite

class IndexPageSpec extends FunSuite with ShouldMatchers {

	"running in browser" in new WithBrowser{
	    browser.goTo("/")
	    
	}
}