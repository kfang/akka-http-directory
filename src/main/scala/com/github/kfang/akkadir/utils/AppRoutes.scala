package com.github.kfang.akkadir.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{HttpCookie, HttpCookiePair}
import akka.http.scaladsl.server.{Directive1, Directives, Route}
import com.github.kfang.akkadir.{AppPackage, MainDBDriver}
import com.github.kfang.akkadir.models.user.User
import spray.json.DefaultJsonProtocol._
import spray.json.JsValue

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
import scala.util.{Failure, Success}

abstract class AppRoutes(App: AppPackage) extends Directives {

  private implicit val __ctx: ExecutionContext = App.system.dispatcher
  private implicit val __db: MainDBDriver = App.db


  private def getUserDirective(pair: HttpCookiePair): Directive1[User] = {
    onComplete(User.findBySession(pair.value)).flatMap({
      case Success(Some(user)) => provide(user)
      case Success(None) => reject
      case Failure(e) => failWith(new Throwable("unable to check for authentication"))
    })
  }

  val requiredAuth: Directive1[User] = cookie(App.config.USER_COOKIE_NAME).flatMap(getUserDirective)
  val optionalAuth: Directive1[Option[User]] = optionalCookie(App.config.USER_COOKIE_NAME).flatMap({
    case None => provide(None)
    case Some(pair) => getUserDirective(pair)
      .flatMap(u => provide(Some(u)))
      .recover((_) => provide(None))
  })

  implicit def futureToRoute[T](f: Future[T]): Route = {
    onComplete(f){
      case Success(js: JsValue) => complete(js)
      case Success((c: HttpCookie, js: JsValue)) => setCookie(c)(complete(js))
      case Success(err: ERROR) => complete(err.code -> err.getResponse)
      case Success(t) => complete(StatusCodes.InternalServerError -> s"Unknown Type Response: ${t.getClass}")
      case Failure(err: ERROR) => complete(err.code -> err.getResponse)
      case Failure(err: Throwable) => complete(ERROR(err))
    }
  }

  val routes: Route

}
