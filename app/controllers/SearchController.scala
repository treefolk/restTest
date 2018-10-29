package controllers

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.MaterialRepository

@Singleton
class SearchController @Inject()(cc: ControllerComponents,
                                 materialRepository: MaterialRepository) extends AbstractController(cc) with I18nSupport {

  import materialRepository._

  def searchMovieMaterialBy(title: String) = Action {
    Logger.info(s"Request received. Search movie material by title [$title].")
    val materials = searchMovieMaterialByTitle(title)
    val json = Json.toJson(materials)
    Logger.info(s"Response: \n $json")
    Ok(json)
  }

}
