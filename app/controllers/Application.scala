package controllers

import java.sql.Timestamp
import java.util.Calendar
import javax.inject.Inject

import dao.PomodoroDao
import models.{Pomodoro, Task}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import service.TaskService

import scala.concurrent.{ExecutionContext, Future}

class Application @Inject()(taskService: TaskService, pomodoroDao: PomodoroDao)(implicit ec: ExecutionContext) extends Controller {

  def index = Action {
    Redirect(routes.Application.tasks()).withSession("token" -> "14ead441b2a3c4faa2258c20ed1adecf605a44f0")
    //Ok(views.html.index("Your new application is ready."))
  }

  def showAllPomodoros = Action.async {
    pomodoroDao.all().map(pomodoros => Ok(Json.toJson(pomodoros)))
  }

  def showPomodorosForTask(taskId: Long) = Action.async {
    pomodoroDao.forTask(taskId).map(pomodoros => Ok(Json.toJson(pomodoros)))
  }

  def addPomodoro(taskId: Long) = Action.async {
    val pomodoro = Pomodoro(0, new Timestamp(Calendar.getInstance.getTimeInMillis), taskId)
    pomodoroDao.insert(pomodoro).map(_ => Ok(Json.toJson(pomodoro)))
  }

  def tasks = Action.async { request =>
    request.session.get("token").map { token =>
      taskService.findTasks(token).map {
        case s: JsSuccess[Seq[Task]] =>
          Ok(views.html.tasks(s.get))
        case e: JsError => NotFound(JsError.toJson(e))
      }
    }.getOrElse(Future.successful(Redirect(routes.Application.index())))
  }

  def completeTask(id: Long) = Action.async { request =>
    request.session.get("token").map { token =>
      taskService.completeItem(id, token).map { response =>
        if(response.status == 200) {
          Ok(response.body)
        } else {
          BadRequest(response.body)
        }
      }
    }.getOrElse(Future.successful(NotFound("Invalid token")))
  }

}