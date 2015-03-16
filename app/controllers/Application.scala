package controllers

import models.LabelSet._
import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {

  def printLabelSet(count: Int) = Action(BodyParsers.parse.json) { request =>
    val l = request.body.validate[LabelSet]
    l.fold (
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> ("Invalid input: " + JsError.toFlatJson(errors))))
      },
      labelSet => {
        try         {
          output(labelSet, count)
          Ok(Json.obj("status" -> "OK"))
        } catch {
          case e: Exception => BadRequest(Json.obj("status" -> "NOK", "message" -> e.getMessage))
        }
      }
    )
  }

}