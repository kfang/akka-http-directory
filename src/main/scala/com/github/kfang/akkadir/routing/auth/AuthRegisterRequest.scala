package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.model.headers.HttpCookie
import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import com.github.kfang.akkadir.models.profile._
import com.github.kfang.akkadir.models.user.User
import com.github.kfang.akkadir.utils.{ERROR, Errors}
import spray.json.JsObject

import scala.concurrent.Future
import com.github.t3hnar.bcrypt._

case class AuthRegisterRequest(
  email: String,
  password: String,
  name: ProfileName,
  phone: Option[ProfilePhone],
  address: Option[ProfileAddress]
)

object AuthRegisterRequest {
  implicit val jsf = jsonFormat5(AuthRegisterRequest.apply)

  implicit class AuthRegister(request: AuthRegisterRequest)(implicit App: AppPackage){
    private implicit val __ctx = App.system.dispatcher
    private implicit val __db = App.db

    private val email = User.cleanEmail(request.email)

    private def validate: Future[Option[ERROR]] = {
      //cleaned email is unique
      val _emailExists = email.map(e => {
        User.find(e).map(o => if(o.isEmpty) None else Some(Errors.EMAIL_EXISTS))
      }).getOrElse(Future.successful(Some(Errors.INVALID_EMAIL)))

      //no empty strings
      val _emptyStrings = Future.successful({
        val isValid = request.email.nonEmpty &&
          request.password.nonEmpty &&
          request.name.first.nonEmpty &&
          request.name.last.nonEmpty &&
          !request.name.middle.contains("") &&
          !request.phone.flatMap(_.cell).contains("") &&
          !request.phone.flatMap(_.work).contains("") &&
          !request.phone.flatMap(_.home).contains("") &&
          !request.address.flatMap(_.city).contains("") &&
          !request.address.flatMap(_.street).contains("") &&
          !request.address.flatMap(_.zip).contains("")

        if(isValid) None else Some(Errors.EMPTY_STRING)
      })

      //email 'looks' like an email
      val _invalidEmail = Future.successful({
        val isValid = User.isMatchesEmailRegex(request.email)
        if(isValid) None else Some(Errors.INVALID_EMAIL)
      })

      //password meets length requirement
      val _passwordShort = Future.successful({
        val isValid = request.password.length >= App.config.USER_PASSWORD_MIN_LENGTH
        if(isValid) None else Some(Errors.PASSWORD_TOO_SHORT)
      })

      Future.sequence(List(
        _emailExists,
        _emptyStrings,
        _invalidEmail,
        _passwordShort
      )).map(_.flatten match {
        case Nil => None
        case ers => Some(ERROR.badRequest(ers.toMap))
      })
    }

    private def registerUser: Future[(HttpCookie, JsObject)] = {
      val user = User(
        email = email.get,
        emailRaw = request.email,
        password = request.password.bcrypt
      )

      val defaultProfile = Profile(
        name = request.name,
        email = request.email,
        phone = request.phone,
        address = request.address,
        user = Some(user._id),
        flags = Some(List(ProfileFlag.Default)),
        organization = None,
        updatedOn = user.createdOn,
        createdOn = user.createdOn
      )

      for {
        userInsert <- App.db.Users.insert(user)
        profileInsert <- App.db.Profiles.insert(defaultProfile)
        cookie <- User.login(user)
      } yield {
        cookie -> JsObject()
      }
    }

    def getResponse: Future[(HttpCookie, JsObject)] = {
      validate.flatMap({
        case Some(error) => throw error
        case None => registerUser
      })
    }
  }
}
