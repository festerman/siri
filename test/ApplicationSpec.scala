import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.Json

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "send 200 when input is valid" in new WithApplication {
      val jsonString = """{"describer":"SPRätt", "labels":["label"],"data":[["data","data"]]}"""
      val json = Json.parse(jsonString)
      val home = route(FakeRequest(POST, "/labels").withJsonBody(json)).get

      status(home) must equalTo(OK)
    }

    "send 400 when input is invalid" in new WithApplication {
      val jsonString = """{"study":"test","describer":"test"}"""
      val json = Json.parse(jsonString)
      val home = route(FakeRequest(POST, "/labels").withJsonBody(json)).get

      status(home) must equalTo(BAD_REQUEST)
      contentAsString(home) must contain ("Invalid input")
    }
  }
}
