package com.github.kfang.akkadir.models.user

import java.util.UUID

import com.github.kfang.akkadir.utils.JsonBsonHandlers
import org.joda.time.DateTime
import reactivemongo.bson.Macros

case class Session(
  expiresOn: DateTime = DateTime.now.plusMonths(3),
  _id: String = UUID.randomUUID().toString
)

object Session extends JsonBsonHandlers {
  implicit val bsf = Macros.handler[Session]
}
