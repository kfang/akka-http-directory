package com.github.kfang.akkadir

import com.github.kfang.akkadir.models.user.User
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.core.nodeset.Authenticate

import scala.concurrent.{ExecutionContext, Future}

case class MainDBDriver(driver: MongoDriver, connection: MongoConnection, defaultDB: DefaultDB) {
  private implicit val __ctx: ExecutionContext = driver.system.dispatcher

  val Users: BSONCollection = defaultDB("users")
  User.indexes.foreach(index => Users.indexesManager.ensure(index))

}

object MainDBDriver {

  /** Connects to MongoDB using passed in configuration */
  def connect(config: MainConfig): Future[MainDBDriver] = {
    val auth = Seq(Authenticate(config.MONGO_AUTH_DB, config.MONGO_AUTH_USER, config.MONGO_AUTH_PASS))
    val connOpts = MongoConnectionOptions(authMode = ScramSha1Authentication)

    val driver: MongoDriver = MongoDriver(config.CONFIG)
    implicit val ctx: ExecutionContext = driver.system.dispatcher
    val connection = driver.connection(config.MONGO_NODES, authentications = auth, options = connOpts)
    connection.database(config.MONGO_DBNAME).map(ddb => {
      MainDBDriver(driver, connection, ddb)
    })
  }

}
