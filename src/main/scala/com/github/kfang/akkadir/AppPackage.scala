package com.github.kfang.akkadir

import akka.actor.ActorSystem

case class AppPackage(
  config: MainConfig,
  db: MainDBDriver,
  system: ActorSystem
)
