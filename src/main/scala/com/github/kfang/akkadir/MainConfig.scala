package com.github.kfang.akkadir

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

case class MainConfig(){
  val CONFIG = ConfigFactory.load
  val DIRECTORY_CONFIG = CONFIG.getConfig("directory")

  val MONGO_AUTH_DB = DIRECTORY_CONFIG.getString("mongo.auth.db")
  val MONGO_AUTH_USER = DIRECTORY_CONFIG.getString("mongo.auth.user")
  val MONGO_AUTH_PASS = DIRECTORY_CONFIG.getString("mongo.auth.pass")
  val MONGO_DBNAME = DIRECTORY_CONFIG.getString("mongo.dbname")
  val MONGO_NODES = DIRECTORY_CONFIG.getStringList("mongo.nodes").toList

  val SYSTEM_BINDADDRESS = DIRECTORY_CONFIG.getString("system.bindAddress")
  val SYSTEM_BINDPORT = DIRECTORY_CONFIG.getInt("system.bindPort")
  val SYSTEM_NAME = DIRECTORY_CONFIG.getString("system.name")

  val USER_COOKIE_NAME = "dir_sess"
  val USER_PASSWORD_MIN_LENGTH = 8

  println(
    s"""
      |********************************************************************************
      |MONGOD_NODES: ${MONGO_NODES.mkString(", ")}
      |********************************************************************************
    """.stripMargin)
}
