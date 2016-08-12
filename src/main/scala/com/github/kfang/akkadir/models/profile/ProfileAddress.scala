package com.github.kfang.akkadir.models.profile

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import reactivemongo.bson.Macros

case class ProfileAddress(
  street: Option[String],
  city: Option[String],
  zip: Option[String]
)

object ProfileAddress {
  implicit val jsf = jsonFormat3(ProfileAddress.apply)
  implicit val bsf = Macros.handler[ProfileAddress]
}
