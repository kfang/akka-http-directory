package com.github.kfang.akkadir

import com.typesafe.config.ConfigFactory

case class MainConfig(env: String = sys.env.getOrElse("ENVIRONMENT", "local").toLowerCase){
  val CONFIG = ConfigFactory.load.getConfig(env)
  val DIRECTORY_CONFIG = CONFIG.getConfig("directory")


  val SYSTEM_NAME = DIRECTORY_CONFIG.getString("system.name")
}
