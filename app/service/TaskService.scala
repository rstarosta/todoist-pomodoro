package service

import javax.inject.Inject

import models.Task
import play.api.libs.json.JsResult
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

class TaskService @Inject()(ws: WSClient)(implicit ec: ExecutionContext) {

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
