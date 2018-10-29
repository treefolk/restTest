import play.api.libs.json.{Json, OWrites}

package object entertainment {

  case class MaterialBasic(id: String, titleType: String, primaryTitle: String, originalTitle: String, isAdult: Boolean,
                           startYear: String, endYear: String, runtimeMinutes: String, genres: Seq[String])

  case class MaterialPrincipal(id: String, ordering: Option[Int], nameId: String, category: String, job: String, characters: String)

  case class MaterialPerson(id: String, primaryName: String, birthYear: String, deathYear: String, primaryProfession: Seq[String], knownForTitles: Seq[String])

  case class Material(materialBasic: MaterialBasic, crew: Seq[(MaterialPerson, MaterialPrincipal)])

  object MaterialBasic {
    implicit val materialBasicWrites: OWrites[MaterialBasic] = Json.writes[MaterialBasic]
  }

  object MaterialPrincipal {
    implicit val materialPrincipalWrites: OWrites[MaterialPrincipal] = Json.writes[MaterialPrincipal]
  }

  object MaterialPerson {
    implicit val materialPersonWrites: OWrites[MaterialPerson] = Json.writes[MaterialPerson]
  }

  object Material {
    implicit val materialWrites: OWrites[Material] = Json.writes[Material]
  }

}
