package com.github.kfang.akkadir.routing

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.routing.auth.AuthRoutes
import com.github.kfang.akkadir.routing.organizations.OrganizationsRoutes

class V1Routes(App: AppPackage) {
  private implicit val __app: AppPackage = App

  val routes: Route = pathPrefix("v1"){
    new AuthRoutes().routes ~
    new OrganizationsRoutes().routes
  }

}
