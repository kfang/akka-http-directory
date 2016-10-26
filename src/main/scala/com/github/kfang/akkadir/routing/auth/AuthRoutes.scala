package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.server.Route
import com.github.kfang.akkadir.AppPackage
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.directives.Credentials
import com.github.kfang.akkadir.models.user.User
import com.github.kfang.akkadir.utils.AppRoutes

import scala.concurrent.Future

class AuthRoutes(implicit App: AppPackage) extends AppRoutes(App){

  private def dbAuth(cred: Credentials): Future[Option[User]] = cred match {
    case Credentials.Missing => Future.successful(None)
    case Credentials.Provided(identifier) =>
      //TODO: do something with the identifier
      Future.successful(None)
  }

  private val register: Route = (
    post &
    path("register") &
    entity(as[AuthRegisterRequest])
  ){(request) => request.getResponse}

  private val login: Route = (
    post &
    path("login") &
    authenticateBasicAsync("", dbAuth)
  ){(user) => complete("")}



  val routes: Route = pathPrefix("auth"){
    register ~        //=> POST /v1/auth/register
    login             //=> POST /v1/auth/login
  }

}
