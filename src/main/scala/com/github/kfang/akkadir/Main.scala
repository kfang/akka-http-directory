package com.github.kfang.akkadir

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.util.{Failure, Success}

object Main extends App {

  private implicit val config = MainConfig()
  private implicit val db = MainDBDriver()
  private implicit val system = ActorSystem(config.SYSTEM_NAME, config.CONFIG)
  private implicit val materializer = ActorMaterializer()
  private implicit val executionContext = system.dispatcher

  private val route = new MainRoutes().routes
  private val _serverBinding = Http().bindAndHandle(route, config.SYSTEM_BINDADDRESS, config.SYSTEM_BINDPORT)

  _serverBinding.andThen({
    case Success(sb) => system.log.info(s"Binding Successful: ${sb.localAddress}")
    case Failure(er) => system.log.error(er, "Binding Failure")
  })
}
