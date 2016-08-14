package com.github.kfang.akkadir.models.user

import java.util.UUID

import akka.http.scaladsl
import com.github.kfang.akkadir.MainDBDriver
import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import org.joda.time.DateTime
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, Macros}

import scala.concurrent.{ExecutionContext, Future}

case class User(
  email: String,    //needs to be 'cleaned'
  emailRaw: String, //what the user put in, use in profile.
  password: String, //should be bcrypted
  sessions: List[Session] = Nil,
  createdOn: DateTime = DateTime.now,
  _id: String = UUID.randomUUID.toString
)

object User {
  implicit val bsf = Macros.handler[User]
  implicit val jsf = jsonFormat6(User.apply)

  val indexes: Seq[Index] = Seq(
    Index(key = Seq("email" -> IndexType.Descending), unique = true),
    Index(key = Seq("sessions._id" -> IndexType.Descending))
  )

  /** Cleans an email, strips plus and dots */
  def cleanEmail(email: String): Option[String] = {
    email.toLowerCase.split('@').toList match {
      case username :: domain :: Nil =>
        val usernameNoPlus = username.indexOf('+') match {
          case i if i > 0 => username.substring(0, i)
          case i => username
        }
        Some(usernameNoPlus.replaceAllLiterally(".", "") + "@" + domain)
      case _ => None
    }
  }

  /** Retrieves User by 'cleaned' email */
  def find(email: String)(implicit db: MainDBDriver, ctx: ExecutionContext): Future[Option[User]] = {
    db.Users.find(BSONDocument("email" -> email)).one[User]
  }

  /** Validates a string against an email regex */
  def isMatchesEmailRegex(str: String): Boolean = {
    str.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
      "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)" +
      "+(?:[A-Za-z]{2}|com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)\\b")
  }

  /** Generates session and attaches it to a user, returns it as HttpCookie */
  def login(user: User)(implicit db: MainDBDriver, ctx: ExecutionContext): Future[scaladsl.model.headers.HttpCookie] = {
    val session = Session()
    val cookie = scaladsl.model.headers.HttpCookie(
      name = db.config.USER_COOKIE_NAME,
      value = session._id,
      expires = Some(scaladsl.model.DateTime(session.expiresOn.getMillis))
    )
    val sel = BSONDocument("_id" -> user._id)
    val upd = BSONDocument("$push" -> BSONDocument("sessions" -> session))
    db.Users.update(sel, upd).map(wr => cookie)
  }

}


