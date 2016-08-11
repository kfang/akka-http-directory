package com.github.kfang.akkadir.utils

import enumeratum._
import JsonBsonHandlers._
import reactivemongo.bson.{BSONHandler, BSONString}
import spray.json.{JsString, JsValue, RootJsonFormat}

trait JsonBsonEnum[T <: EnumEntry] {
  this: Enum[T] =>

  implicit val jsf = new RootJsonFormat[T] {
    override def read(json: JsValue): T = withName(json.convertTo[String])
    override def write(obj: T): JsValue = JsString(obj.entryName)
  }

  implicit val bsf = new BSONHandler[BSONString, T] {
    override def write(t: T): BSONString = BSONString(t.entryName)
    override def read(bson: BSONString): T = withName(bson.value)
  }

}
