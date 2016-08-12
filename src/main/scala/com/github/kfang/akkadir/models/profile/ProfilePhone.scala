package com.github.kfang.akkadir.models.profile

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import reactivemongo.bson.Macros

case class ProfilePhone(
  home: Option[String],
  cell: Option[String],
  work: Option[String]
)

object ProfilePhone {
  implicit val jsf = jsonFormat3(ProfilePhone.apply)
  implicit val bsf = Macros.handler[ProfilePhone]
}
