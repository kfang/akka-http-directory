package com.github.kfang.akkadir.models.profile

import java.util.UUID

import com.github.kfang.akkadir.MainDBDriver
import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import org.joda.time.DateTime
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, Macros}

import scala.concurrent.{ExecutionContext, Future}

case class Profile(
  name: ProfileName,
  email: String,
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

  /** MongoDB Indexes */
  val indexes: Seq[Index] = Seq(
    Index(key = Seq("user" -> IndexType.Descending)),
    Index(key = Seq("organization" -> IndexType.Descending, "name.first" -> IndexType.Descending)),
    Index(key = Seq("organization" -> IndexType.Descending, "name.last" -> IndexType.Descending))
  )

  /** Find a Profile by User ID */
  def findDefaultForUser(user: String)(implicit db: MainDBDriver, ctx: ExecutionContext): Future[Option[Profile]] = {
    db.Profiles.find(BSONDocument("user" -> user, "flags" -> ProfileFlag.Default.bson)).one[Profile]
  }

}
