package models

import javax.inject.Inject

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

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


class TaskHandler @Inject()(
    ws: WSClient)(implicit ec: ExecutionContext) {

  val url = "https://todoist.com/API/v7/sync"

  def uuid: String = java.util.UUID.randomUUID.toString

  def completeItem(id: Long, token: String): Future[WSResponse] = {
    val data = Map("token" -> Seq(token),
        "commands" -> Seq(s"""[{\"type\": \"item_complete\", \"uuid\": \"$uuid\", \"args\": {\"ids\": [$id]}}]"""))

    ws.url(url).post(data)
  }

  def findTasks(token: String): Future[JsResult[Seq[Task]]] = {
    val data = Map("token" -> Seq(token),
      "sync_token" -> Seq("*"),
      "resource_types" -> Seq("[\"items\"]"))

    ws.url(url).post(data).map {
      response =>
        (response.json \ "items").validate[Seq[Task]]
    }
  }
}
