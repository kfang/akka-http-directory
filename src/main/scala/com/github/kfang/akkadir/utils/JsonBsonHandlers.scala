package com.github.kfang.akkadir.utils

import org.joda.time.DateTime
import reactivemongo.bson.{BSONDateTime, BSONHandler}
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat}

/**
  * JSON and BSON Handlers for marshalling data into case classes
  * either from Mongo or from Http Requests
  */
trait JsonBsonHandlers extends DefaultJsonProtocol {

  implicit object DateTimeBSONHandler extends BSONHandler[BSONDateTime, DateTime] {
    override def write(t: DateTime): BSONDateTime = BSONDateTime(t.getMillis)
    override def read(bson: BSONDateTime): DateTime = new DateTime(bson.value)
  }

  implicit object DateTimeJSONFormat extends RootJsonFormat[DateTime] {
    override def read(json: JsValue): DateTime =
    override def write(obj: DateTime): JsValue = ???
  }

}

object JsonBsonHandlers extends JsonBsonHandlers
