package com.github.kfang.akkadir.models.profile

import java.util.UUID

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import org.joda.time.DateTime
import reactivemongo.bson.Macros

//TODO: check if works with chinese
case class Profile(
  name: ProfileName,
  email: String,  //TODO: don't strip unless its default.
  phone: Option[ProfilePhone],
  address: Option[ProfileAddress],

  user: Option[String],
  flags: Option[List[ProfileFlag]],
  organization: Option[String],

  updatedOn: DateTime = DateTime.now,
  createdOn: DateTime = DateTime.now,
  _id: String = UUID.randomUUID().toString
)

object Profile {
  implicit val bsf = Macros.handler[Profile]
  implicit val jsf = jsonFormat10(Profile.apply)
}
