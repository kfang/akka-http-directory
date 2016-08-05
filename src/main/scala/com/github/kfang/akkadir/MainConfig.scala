package com.github.kfang.akkadir

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

case class MainConfig(env: String = sys.env.getOrElse("ENVIRONMENT", "local").toLowerCase){
  val CONFIG = ConfigFactory.load.getConfig(env)
  val DIRECTORY_CONFIG = CONFIG.getConfig("directory")

  val MONGO_DBNAME = DIRECTORY_CONFIG.getString("mongo.dbname")
  val MONGO_NODES = DIRECTORY_CONFIG.getStringList("mongo.nodes").toList

  val SYSTEM_BINDADDRESS = DIRECTORY_CONFIG.getString("system.bindAddress")
  val SYSTEM_BINDPORT = DIRECTORY_CONFIG.getInt("system.bindPort")
  val SYSTEM_NAME = DIRECTORY_CONFIG.getString("system.name")

  println(
    s"""
      |********************************************************************************
      |MONGOD_NODES: ${MONGO_NODES.mkString(", ")}
      |********************************************************************************
    """.stripMargin)
}
