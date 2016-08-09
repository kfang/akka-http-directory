package com.github.kfang.akkadir.models.user

import java.util.UUID

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import org.joda.time.DateTime
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.Macros

case class User(
  email: String,    //needs to be 'cleaned'
  password: String, //should be bcrypted
  sessions: List[Session] = Nil,
  createdOn: DateTime = DateTime.now,
  _id: String = UUID.randomUUID.toString
)

object User {
  implicit val bsf = Macros.handler[User]

  val indexes: Seq[Index] = Seq(
    Index(key = Seq("email" -> IndexType.Descending), unique = true),
    Index(key = Seq("sessions._id" -> IndexType.Descending))
  )
}
