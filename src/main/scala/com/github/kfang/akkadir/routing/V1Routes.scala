package com.github.kfang.akkadir.routing

import akka.http.scaladsl.server._
import com.github.kfang.akkadir.AppPackage

class V1Routes(App: AppPackage) extends Directives {

  val routes: Route = pathPrefix("v1"){
    complete("Not Complete")
  }

}
