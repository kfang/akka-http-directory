package com.github.kfang.akkadir

import reactivemongo.api.MongoDriver

case class MainDBDriver(implicit config: MainConfig) {

  private val driver = MongoDriver()
  private val connection = driver.connection(config.MONGO_NODES)
  private val db = connection.database(config.MONGO_DBNAME)

}
