package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.server.Route
import com.github.kfang.akkadir.AppPackage
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.github.kfang.akkadir.utils.AppRoutes

class AuthRoutes(implicit App: AppPackage) extends AppRoutes(App){

  private val registerRoute: Route = (
    post &
    path("register") &
    entity(as[AuthRegisterRequest])
  ){(request) => complete("BLAH")}

  val routes: Route = pathPrefix("auth"){
    registerRoute             //=> POST /v1/auth/register
  }

}
