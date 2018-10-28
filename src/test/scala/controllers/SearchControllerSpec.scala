package controllers

import play.api.mvc.{Result, Results}
import play.api.test.{FakeRequest, Helpers, PlaySpecification}
import services.MaterialRepository

import scala.concurrent.Future

class SearchControllerSpec extends PlaySpecification with Results {

  "SearchController#searchMovieMaterialBy" should {
    "be valid" in {
      val controller = new SearchController(Helpers.stubControllerComponents(), new MaterialRepository())
      val result: Future[Result] = controller.searchMovieMaterialBy("title").apply(FakeRequest())
      status(result) must_== OK
    }
  }
}
