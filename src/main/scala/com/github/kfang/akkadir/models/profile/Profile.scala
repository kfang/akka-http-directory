package com.github.kfang.akkadir.models.profile

import java.util.UUID

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import reactivemongo.bson.Macros

//TODO: check if works with chinese
case class Profile(
  name: String,//TODO: split to first, last, middle, etc...
  email: String,  //TODO: don't strip unless its default.
  phone: Option[String], //TODO: split this to cell, home, work etc...
  address: Option[String], //TODO: split this to street, city, zip, etc...

  flags: Option[List[ProfileFlag]],
  user: Option[String],
  organization: Option[String],

  _id: String = UUID.randomUUID().toString
)

object Profile {
  implicit val bsf = Macros.handler[Profile]
  implicit val jsf = jsonFormat8(Profile.apply)
}
