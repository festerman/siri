package models

import java.io.{FileWriter, BufferedWriter, File}
import java.text.SimpleDateFormat
import java.util.Calendar

import play.api.libs.json.Json
import com.typesafe.config.ConfigFactory

/**
 * Created by staffan on 2015-03-12.
 */
object LabelSet {
  val df = new SimpleDateFormat("yyyyMMdd_HHmmss")

  case class LabelSet (labels: List[String], describer: String, data: List[List[String]])

  implicit val labelSetReads = Json.reads[LabelSet]

  def dateStamp = df.format(Calendar.getInstance().getTime())

  def output(l: LabelSet, count: Int) = {
    val config = ConfigFactory.load()
    val outputfolder = config.getString("siri.output-folder")
    val file = new File(outputfolder + "/" + dateStamp + ".cmd")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(toStrings(l, count) mkString "")
    bw.close()
  }

  def toStrings(l: LabelSet, count: Int): List[String] = {
    val config = ConfigFactory.load()
    val conffolder = config.getString("siri.config-folder")
    val dsc = Describer(conffolder + "/" + l.describer + ".dsc")
    for {label <- l.labels
         row <- l.data}
    yield {
      "LABELNAME = \"" + conffolder + "/" + label + ".lab\"\n" +
        (dsc.reformat(row) mkString "\n") + "\n" +
        "LABELQUANTITY = \"" + count + "\"\n"
    }


  }

}
