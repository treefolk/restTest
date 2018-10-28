package services

import entertainment.{Material, MaterialBasic, MaterialPerson, MaterialPrincipal}

import scala.io.Source
import scala.util.Try

class MaterialRepository {

  val PathToFile = "public/inputData/"
  val MovieBasicsSource: String = PathToFile + "title_basics.tsv"
  val MaterialPrincipalsSource: String = PathToFile + "title_principals.tsv"
  val MaterialPersonsSource: String = PathToFile + "name_basics.tsv"

  def searchMovieMaterialByTitle(title: String): Seq[Material] = {
    val movieBasics = getMaterialBasicsFromCsv(title, "movie").toList
    val materialPrincipals = movieBasics.map(_.id).flatMap(getMaterialPrincipalsFromCsv)
    val materialPersons = materialPrincipals.map(_.nameId).flatMap(getMaterialPersonFromCsv)
    for (mBasic <- movieBasics) yield materialComposer(mBasic, materialPrincipals, materialPersons)
  }

  private def materialComposer(basic: MaterialBasic, mPrincipals: Seq[MaterialPrincipal], mPerson: Seq[MaterialPerson]): Material = {
    val materialPrincipals = mPrincipals.filter(p => p.id.equals(basic.id))
    val materialPrincipalNameId = materialPrincipals.map(_.nameId)
    val materialPersons = mPerson.filter(p => materialPrincipalNameId.contains(p.id))
    val personPrincipalSeq = materialPersons zip materialPrincipals
    Material(basic, personPrincipalSeq)
  }

  private def getMaterialBasicsFromCsv(title: String, titleType: String): Iterator[MaterialBasic] = {
    getBodyFromCsv(MovieBasicsSource)
      .filter(x => x.contains(titleType) && x.contains(title))
      .map(_.split("\t").map(_.trim).toList).collect {
      case id :: tType :: primaryTitle :: originalTitle :: isAdult :: startYear :: endYear :: runtimeMinutes :: genres :: Nil =>
        val genresSeq = genres.split(",").toList
        MaterialBasic(id, tType, primaryTitle, originalTitle, isAdultBool(isAdult), validateYear(startYear),
          validateYear(endYear), runtimeMinutes, genresSeq)
    }
  }

  private def getMaterialPrincipalsFromCsv(itemId: String): Iterator[MaterialPrincipal] = {
    getBodyFromCsv(MaterialPrincipalsSource)
      .filter(_.startsWith(itemId))
      .map(_.split("\t").map(_.trim).toList).collect {
      case id :: ordering :: nId :: category :: job :: characters :: Nil =>
        val orderingInt = Try(ordering.toInt).toOption
        MaterialPrincipal(id, orderingInt, nId, category, job, characters)
    }
  }

  private def getMaterialPersonFromCsv(nameId: String): Iterator[MaterialPerson] = {
    getBodyFromCsv(MaterialPersonsSource)
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
