package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class Task(id: Long, title: String)

object Task {
  implicit val implicitWrites = new Writes[Task] {
    def writes(task: Task): JsValue = {
      Json.obj(
        "id" -> task.id,
        "title" -> task.title
      )
    }
  }

  implicit val implicitReads: Reads[Task] = (
    (JsPath \ "id").read[Long] and
    (JsPath \ "content").read[String]
  )(Task.apply _)
}


