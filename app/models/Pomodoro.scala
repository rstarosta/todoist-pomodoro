package models

import java.sql.Timestamp

import play.api.libs.json.{JsValue, Json, Writes}

case class Pomodoro(id: Long, time: Timestamp, taskId: Long)

object Pomodoro {
  implicit val implicitWrites = new Writes[Pomodoro] {
    def writes(pomodoro: Pomodoro): JsValue = {
      Json.obj(
        "id" -> pomodoro.id,
        "time" -> pomodoro.time,
        "taskId" -> pomodoro.taskId
      )
    }
  }
}
