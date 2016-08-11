package com.github.kfang.akkadir.models.profile

import java.util.UUID

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import reactivemongo.bson.Macros

case class Profile(
  name: String,
  email: String,
  phone: Option[String],
  address: Option[String],

  flags: Option[List[ProfileFlag]],
  user: Option[String],
  organization: Option[String],

  _id: String = UUID.randomUUID().toString
)

object Profile {
  implicit val bsf = Macros.handler[Profile]
  implicit val jsf = jsonFormat8(Profile.apply)
}
