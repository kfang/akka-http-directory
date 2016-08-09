package com.github.kfang.akkadir

import akka.actor.ActorSystem

case class AppPackage(
  db: MainDBDriver,
  system: ActorSystem
)
