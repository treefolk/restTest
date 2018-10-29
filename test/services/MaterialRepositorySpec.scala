package services

import entertainment.{Material, MaterialBasic, MaterialPerson, MaterialPrincipal}
import org.specs2.mutable.Specification

class MaterialRepositorySpec extends Specification {

  val materialBasic_mov1 = MaterialBasic("tt0000005", "movie", "Blacksmith Scene", "Blacksmith Scene", isAdult = false, "1893", "N/A", "1", Seq("Comedy", "Short"))
  val materialPerson_mov1_1 = MaterialPerson("nm1335271", "Marlon Brando", "1924", "2004", Seq("actor", "soundtrack", "director"), Seq("tt0068646", "tt0047296", "tt0078788", "tt0070849"))
  val materialPrincipal_mov1_1 = MaterialPrincipal("tt0000005", Some(3), "nm1335271", "composer", "\\N", "\\N")
  val materialPerson_mov1_2 = MaterialPerson("nm5442200", "Richard Burton", "1925", "1984", Seq("actor", "producer", "soundtrack"), Seq("tt0057877", "tt0059749", "tt0087803", "tt0061184"))
  val materialPrincipal_mov1_2 = MaterialPrincipal("tt0000005", Some(4), "nm5442200", "editor", "\\N", "\\N")

  val materialBasic_mov2 = MaterialBasic("tt0000007", "movie", "Blacksmith Scene", "Blacksmith Scene", isAdult = false, "1209", "N/A", "1", Seq("Comedy", "Short"))
  val materialPerson_mov2_1 = MaterialPerson("nm0721526", "Ingrid Bergman", "1915", "1982", Seq("actress", "soundtrack", "producer"), Seq("tt0034583", "tt0038109", "tt0071877", "tt0038787"))
  val materialPrincipal_mov2_1 = MaterialPrincipal("tt0000007", Some(1), "nm0721526", "director", "\\N", "\\N")
  val materialPerson_mov2_2 = MaterialPerson("nm1335277", "John Belushi", "1949", "1982", Seq("actor", "writer", "soundtrack"), Seq("tt0080455", "tt0078723", "tt0077975", "tt0072562"))
  val materialPrincipal_mov2_2 = MaterialPrincipal("tt0000007", Some(2), "nm1335277", "composer", "\\N", "\\N")

  val expectedMaterialResult = Seq(
    Material(materialBasic_mov1, Seq((materialPerson_mov1_1, materialPrincipal_mov1_1),
      (materialPerson_mov1_2, materialPrincipal_mov1_2))),
    Material(materialBasic_mov2, Seq((materialPerson_mov2_1, materialPrincipal_mov2_1),
      (materialPerson_mov2_2, materialPrincipal_mov2_2)))
  )

  "MaterialRepository#searchMovieMaterialByTitle" should {
    "return correct result" in {
      val materialRepo = new MaterialRepository()
      val result = materialRepo.searchMovieMaterialByTitle("Blacksmith Scene")
      assert(result.length === 2)
      result === expectedMaterialResult
    }
  }
}