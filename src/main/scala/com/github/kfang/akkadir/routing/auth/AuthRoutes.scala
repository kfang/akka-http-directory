package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.server.Route
import com.github.kfang.akkadir.AppPackage
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.github.kfang.akkadir.utils.AppRoutes


class AuthRoutes(implicit App: AppPackage) extends AppRoutes(App){

  private val register: Route = (
    post &
    path("register") &
    entity(as[AuthRegisterRequest])
  ){(request) => request.getResponse}

  private val login: Route = (
    post &
    path("login") &
    entity(as[AuthLoginRequest])
  ){(request) => request.getResponse}

  private val check: Route = (
    get &
    pathEnd
  ){ complete("TODO") }



  val routes: Route = pathPrefix("auth"){
    register ~        //=> POST /v1/auth/register
    login             //=> POST /v1/auth/login
  }

}
