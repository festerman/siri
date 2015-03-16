package models

import scala.io.BufferedSource

/**
 * Created by staffan on 2015-03-12.
 */
class Describer {
  private var fields: List[String] = List()
  private var fileType: String = "ASCII_DELIMITED"
  private var recordDelimiter: String = "\\013"
  private var fieldDelimiter:String = ";"
  private var charSet:Int = 0
  private var fieldCount:Int = 0

  def reformat (data: List[String]): List[String] = fields zip data map (s => s._1 + " = \"" + s._2 + "\"")

}

object Describer {

  val fileTypeRE = "^FileType=(.*)$".r
  val recordDelimRE = "^RecordDelimiter=(.*)$".r
  val fieldDelimRE = "^FieldDelimiter=(.*)$".r
  val charSetRE = "^CharSet=(\\d)$".r
  val fieldCountRE = "^Fields=(\\d+)$".r
  val fieldNameRE = "^Field\\d+=(.*)$".r

  def apply(file: String): Describer = {
    val src: BufferedSource = load(file)
    val dsc = new Describer()
    for (line <- src.getLines) {
      line match {
        case "[CS_DataFileDesc]" => // header, do nothing
        case fileTypeRE(t) => dsc.fileType = t
        case recordDelimRE(t) => dsc.recordDelimiter = t
        case fieldDelimRE(t) => dsc.fieldDelimiter = t
        case fieldCountRE(t) => dsc.fieldCount = t.toInt
        case charSetRE(t) => dsc.charSet = t.toInt
        case fieldNameRE(t) => dsc.fields = t :: dsc.fields
        case "" => // empty line, do nothing
        case something => throw new IllegalArgumentException(file + " does not look like a correct describer file: " + something)
      }
    }

    if (dsc.fields.length != dsc.fieldCount) {
      throw new IllegalArgumentException(file + " has the wrong number of fields")
    }
    dsc.fields = dsc.fields.reverse
    dsc
  }

  def load(file: String): BufferedSource = scala.io.Source.fromFile(file)

}
