package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.kfang.akkadir.AppPackage
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

class AuthRoutes(implicit App: AppPackage) {

  private val registerRoute: Route = (
    post &
    path("register") &
    entity(as[AuthRegisterRequest])
  ){(request) => complete("BLAH")}

  val routes: Route = pathPrefix("auth"){
    registerRoute             //=> POST /v1/auth/register
  }

}
