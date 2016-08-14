package com.github.kfang.akkadir.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.{Directives, Route}
import com.github.kfang.akkadir.AppPackage
import spray.json.DefaultJsonProtocol._
import spray.json.JsValue

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
import scala.util.{Failure, Success}

abstract class AppRoutes(App: AppPackage) extends Directives {

  private implicit val __ctx: ExecutionContext = App.system.dispatcher

  implicit def futureToRoute[T](f: Future[T]): Route = {
    onComplete(f){
      case Success(js: JsValue) => complete(js)
      case Success((c: HttpCookie, js: JsValue)) => setCookie(c)(complete(js))
      case Success(err: ERROR) => complete(err.getResponse)
      case Success(t) => complete(StatusCodes.InternalServerError -> s"Unknown Type Response: ${t.getClass}")
      case Failure(err: ERROR) => complete(err.getResponse)
      case Failure(err: Throwable) => complete(ERROR(err))
    }
  }

  val routes: Route

}
