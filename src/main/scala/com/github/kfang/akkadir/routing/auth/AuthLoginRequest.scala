package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.model.headers.HttpCookie
import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.models.profile.Profile
import com.github.kfang.akkadir.models.user.User
import com.github.kfang.akkadir.utils.{ERROR, Errors}
import spray.json.DefaultJsonProtocol._
import spray.json._
import com.github.t3hnar.bcrypt._

import scala.concurrent.Future

case class AuthLoginRequest(
  email: String,
  password: String
)

object AuthLoginRequest {
  implicit val __jsf = jsonFormat2(AuthLoginRequest.apply)

  implicit class AuthLogin(request: AuthLoginRequest)(implicit App: AppPackage){
    private implicit val __ctx = App.system.dispatcher
    private implicit val __db = App.db

    private def verify(user: User): Option[User] = {
      val isValidPassword = user.password.isBcrypted(user.password)
      if(isValidPassword) Some(user) else None
    }

    def getResponse: Future[(HttpCookie, JsObject)] = {
      User.findByEmailRaw(request.email).map(_.flatMap(verify)).flatMap({
        case None => throw ERROR.badRequest(Errors.BAD_LOGIN)
        case Some(user) => User.login(user).map(_ -> user)
      }).flatMap({ case (cookie, user) =>
        for {
          profiles <- Profile.findDefaultForUser(user._id).map(p => List(p).flatten)
          users = List(user.responseFormat)
          response = JsObject("users" -> users.toJson, "profiles" -> profiles.toJson)
        } yield {
          cookie -> response
        }
      })
    }
  }
}
