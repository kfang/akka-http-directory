package com.github.kfang.akkadir.utils

import org.joda.time.DateTime
import reactivemongo.bson.{BSONDateTime, BSONHandler, BSONLong}
import spray.json.{DefaultJsonProtocol, JsNumber, JsValue, RootJsonFormat}

/**
  * JSON and BSON Handlers for marshalling data into case classes
  * either from Mongo or from Http Requests
  */
trait JsonBsonHandlers extends DefaultJsonProtocol {

  implicit object DateTimeBSONHandler extends BSONHandler[BSONLong, DateTime] {
    override def write(t: DateTime): BSONDateTime = BSONDateTime(t.getMillis)
    override def read(bson: BSONLong): DateTime = new DateTime(bson.value)
  }

  implicit object DateTimeJSONFormat extends RootJsonFormat[DateTime] {
    override def read(json: JsValue): DateTime = new DateTime(json.convertTo[Long])
    override def write(obj: DateTime): JsValue = JsNumber(obj.getMillis)
  }

}

object JsonBsonHandlers extends JsonBsonHandlers
