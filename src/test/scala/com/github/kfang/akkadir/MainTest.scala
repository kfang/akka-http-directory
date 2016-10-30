package com.github.kfang.akkadir

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object MainTest {

  private val config: MainConfig = MainConfig()
  private implicit val __system: ActorSystem = ActorSystem(config.SYSTEM_NAME, config.CONFIG)
  private implicit val __materializer: ActorMaterializer = ActorMaterializer()
  private implicit val __ctx: ExecutionContext = __system.dispatcher

  lazy val _app = for {
    db <- MainDBDriver.connect(config)
    app = AppPackage(config = config, db = db, system = __system)
  } yield {
    println("Connected Main DB")
    app
  }

  lazy val app = Await.result(_app, Duration.Inf)

}
