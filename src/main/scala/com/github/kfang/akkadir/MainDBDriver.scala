package com.github.kfang.akkadir

import reactivemongo.api._
import reactivemongo.core.nodeset.Authenticate

import scala.concurrent.{ExecutionContext, Future}

case class MainDBDriver(driver: MongoDriver, connection: MongoConnection, defaultDB: DefaultDB) {

}

object MainDBDriver {
  def connect(config: MainConfig): Future[MainDBDriver] = {
    val auth = Seq(Authenticate(config.MONGO_AUTH_DB, config.MONGO_AUTH_USER, config.MONGO_AUTH_PASS))
    val connOpts = MongoConnectionOptions(authMode = ScramSha1Authentication)

    val driver: MongoDriver = MongoDriver()
    implicit val ctx: ExecutionContext = driver.system.dispatcher
    val connection = driver.connection(config.MONGO_NODES, authentications = auth, options = connOpts)
    connection.database(config.MONGO_DBNAME).map(ddb => {
      MainDBDriver(driver, connection, ddb)
    })
  }
}
