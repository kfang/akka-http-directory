package com.github.kfang.akkadir.models.profile

import com.github.kfang.akkadir.utils.JsonBsonEnum
import enumeratum._
import reactivemongo.bson.BSONValue
import spray.json.JsValue

sealed abstract class ProfileFlag(override val entryName: String) extends EnumEntry {
  override def toString: String = entryName
  def bson: BSONValue = ProfileFlag.bsf.write(this)
  def json: JsValue = ProfileFlag.jsf.write(this)
}

object ProfileFlag extends Enum[ProfileFlag] with JsonBsonEnum[ProfileFlag]{
  val values: Seq[ProfileFlag] = findValues

  case object Default extends ProfileFlag("default")
}
