package com.github.kfang.akkadir.utils

import org.joda.time.DateTime
import reactivemongo.bson.{BSONDateTime, BSONHandler}

/**
  * JSON and BSON Handlers for marshalling data into case classes
  * either from Mongo or from Http Requests
  */
trait JsonBsonHandlers {

  implicit object DateTimeBSONHandler extends BSONHandler[BSONDateTime, DateTime] {
    override def write(t: DateTime): BSONDateTime = BSONDateTime(t.getMillis)
    override def read(bson: BSONDateTime): DateTime = new DateTime(bson.value)
  }

}

object JsonBsonHandlers extends JsonBsonHandlers
