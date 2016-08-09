package com.github.kfang.akkadir

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.github.kfang.akkadir.routing.V1Routes

import scala.concurrent.ExecutionContext

object Main extends App {

  private val config: MainConfig = MainConfig()
  private implicit val __system: ActorSystem = ActorSystem(config.SYSTEM_NAME, config.CONFIG)
  private implicit val __materializer: ActorMaterializer = ActorMaterializer()
  private implicit val __ctx: ExecutionContext = __system.dispatcher

  (for {
    db <- MainDBDriver.connect(config)
    app = AppPackage(db = db, system = __system)
    routing = new V1Routes(app).routes
    serverBinding <- Http().bindAndHandle(routing, config.SYSTEM_BINDADDRESS, config.SYSTEM_BINDPORT)
  } yield {
    __system.log.info(s"Binding Successful: ${serverBinding.localAddress}")
  }).recover({
    case e =>
  })
}
