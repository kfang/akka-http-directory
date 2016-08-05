package com.github.kfang.akkadir

import reactivemongo.api.MongoDriver
import scala.concurrent.ExecutionContext

case class MainDBDriver(implicit config: MainConfig) {

  private val driver: MongoDriver = MongoDriver()
  private implicit val ctx: ExecutionContext = driver.system.dispatcher
  private val connection = driver.connection(config.MONGO_NODES)
  private val db = connection.database(config.MONGO_DBNAME)

}
