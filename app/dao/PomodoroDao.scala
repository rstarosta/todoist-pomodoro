package dao


import java.sql.{Date, Timestamp}

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

import models.Pomodoro
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class PomodoroDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Pomodoros = TableQuery[PomodorosTable]

  def all(): Future[Seq[Pomodoro]] = db.run(Pomodoros.result)

  def insert(pomodoro: Pomodoro): Future[Unit] = db.run(Pomodoros += pomodoro).map { _ => () }

  private class PomodorosTable(tag: Tag) extends Table[Pomodoro](tag, "POMODORO") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def time = column[Timestamp]("TIME")
    def taskId = column[Long]("TASK_ID")

    def * = (id, time, taskId) <> ((Pomodoro.apply _).tupled, Pomodoro.unapply)
  }
}
