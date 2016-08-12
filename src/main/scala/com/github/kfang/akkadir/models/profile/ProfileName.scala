package com.github.kfang.akkadir.models.profile

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import reactivemongo.bson.Macros

case class ProfileName(
  first: String,
  middle: Option[String],
  last: String
)

object ProfileName {
  implicit val jsf = jsonFormat3(ProfileName.apply)
  implicit val bsf = Macros.handler[ProfileName]
}
