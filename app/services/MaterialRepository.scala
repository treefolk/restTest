package services

import com.typesafe.config.ConfigFactory
import entertainment.{Material, MaterialBasic, MaterialPerson, MaterialPrincipal}
import play.api.Logger

import scala.io.Source
import scala.util.Try

class MaterialRepository {

  def searchMovieMaterialByTitle(title: String): Seq[Material] = {
    val movieBasics = getMaterialBasicsFromCsv(title, "movie").toList
    Logger.info(s"Found ${movieBasics.length} movie(s)")
    val materialPrincipals = movieBasics.map(_.id).flatMap(getMaterialPrincipalsFromCsv)
    Logger.info(s"Found ${materialPrincipals.length} materialPrincipal(s)")
    val materialPersons = materialPrincipals.map(_.nameId).flatMap(getMaterialPersonFromCsv)
    Logger.info(s"Found ${materialPersons.length} materialPerson(s)")
    for (mBasic <- movieBasics) yield materialComposer(mBasic, materialPrincipals, materialPersons)
  }

  private def materialComposer(basic: MaterialBasic, mPrincipals: Seq[MaterialPrincipal], mPerson: Seq[MaterialPerson]): Material = {
    Logger.info("Composing material result")
    val materialPrincipals = mPrincipals.filter(p => p.id.equals(basic.id))
    val materialPrincipalNameId = materialPrincipals.map(_.nameId)
    val materialPersons = mPerson.filter(p => materialPrincipalNameId.contains(p.id))
    val personPrincipalSeq = materialPersons zip materialPrincipals
    Material(basic, personPrincipalSeq)
  }

  private def getMaterialBasicsFromCsv(title: String, titleType: String): Iterator[MaterialBasic] = {
    val movieBasicsSource = ConfigFactory.load().getString("movieBasicsSource")
    Logger.info(s"movieBasicsSource: $movieBasicsSource")
    getBodyFromCsv(movieBasicsSource)
      .filter(x => x.contains(titleType) && x.contains(title))
      .map(_.split("\t").map(_.trim).toList).collect {
      case id :: tType :: primaryTitle :: originalTitle :: isAdult :: startYear :: endYear :: runtimeMinutes :: genres :: Nil =>
        val genresSeq = genres.split(",").toList
        MaterialBasic(id, tType, primaryTitle, originalTitle, isAdultBool(isAdult), validateYear(startYear),
          validateYear(endYear), runtimeMinutes, genresSeq)
    }
  }

  private def getMaterialPrincipalsFromCsv(itemId: String): Iterator[MaterialPrincipal] = {
    val materialPrincipalsSource = ConfigFactory.load().getString("materialPrincipalsSource")
    Logger.info(s"materialPrincipalsSource: $materialPrincipalsSource")
    getBodyFromCsv(materialPrincipalsSource)
      .filter(_.startsWith(itemId))
      .map(_.split("\t").map(_.trim).toList).collect {
      case id :: ordering :: nId :: category :: job :: characters :: Nil =>
        val orderingInt = Try(ordering.toInt).toOption
        MaterialPrincipal(id, orderingInt, nId, category, job, characters)
    }
  }

  private def getMaterialPersonFromCsv(nameId: String): Iterator[MaterialPerson] = {
    val materialPersonsSource = ConfigFactory.load().getString("materialPersonsSource")
    Logger.info(s"materialPersonsSource: $materialPersonsSource")
    getBodyFromCsv(materialPersonsSource)
      .filter(_.startsWith(nameId))
      .map(_.split("\t").map(_.trim).toList).collect {
      case id :: primaryName :: birthYear :: deathYear :: primaryProfession :: knownForTitles :: Nil =>
        val primaryProfessions = primaryProfession.split(",").toList
        val knownForTitlesSeq = knownForTitles.split(",").toList
        MaterialPerson(id, primaryName, validateYear(birthYear), validateYear(deathYear), primaryProfessions, knownForTitlesSeq)
    }
  }

  private def getBodyFromCsv(source: String, skipHeader: Boolean = true): Iterator[String] = {
    val lines = Source.fromFile(source).getLines
    if (skipHeader) lines.drop(1) else lines
  }

  private def validateYear(year: String): String = year match {
    case s if s.matches("\\d{4}") => year
    case _ => "N/A"
  }

  private def isAdultBool(isAdult: String): Boolean = isAdult match {
    case "0" => false
    case "1" => true
  }
}
