package com.github.kfang.akkadir.models.organization

import java.util.UUID

import com.github.kfang.akkadir.MainDBDriver
import reactivemongo.api.indexes.{Index, IndexType}
import spray.json.DefaultJsonProtocol._
import reactivemongo.bson.{BSONArray, BSONDocument, Macros}

import scala.concurrent.{ExecutionContext, Future}

case class Organization(
  name: String,
  slug: List[String],
  owner: List[String],
  _id: String = UUID.randomUUID().toString
)

object Organization {
  implicit val __bsf = Macros.handler[Organization]
  implicit val __jsf = jsonFormat4(Organization.apply)

  val indexes: Seq[Index] = Seq(
    Index(key = Seq("slug" -> IndexType.Descending)),
    Index(key = Seq("owner" -> IndexType.Descending))
  )

  def findByIdOrSlug(idOrSlug: String)(implicit db: MainDBDriver, ctx: ExecutionContext): Future[Option[Organization]] = {
    db.Organizations.find(BSONDocument("$or" -> BSONArray(
      BSONDocument("_id" -> idOrSlug),
      BSONDocument("slug" -> idOrSlug)
    ))).one[Organization]
  }
}
