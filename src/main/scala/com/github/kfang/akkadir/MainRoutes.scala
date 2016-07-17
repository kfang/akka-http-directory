package com.github.kfang.akkadir

import akka.http.scaladsl.server.{Directives, Route}

class MainRoutes() extends Directives {

  val routes: Route = pathPrefix("v1"){
    complete("Not Complete")
  }

}
