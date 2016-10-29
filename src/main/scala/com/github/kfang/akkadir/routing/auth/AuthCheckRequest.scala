package com.github.kfang.akkadir.routing.auth

import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.models.profile.Profile
import com.github.kfang.akkadir.models.user.User
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.Future

case class AuthCheckRequest(user: Option[User])(implicit App: AppPackage) {

  private implicit val __ctx = App.system.dispatcher
  private implicit val __db = App.db

  def getResponse: Future[JsObject] = {
    user.map(u => {
      for {
        profiles <- Profile.findDefaultForUser(u._id).map(p => List(p).flatten)
        users = List(u.responseFormat)
        response = JsObject("users" -> users.toJson, "profiles" -> profiles.toJson)
      } yield {
        response
      }
    }).getOrElse(Future.successful(JsObject()))
  }

}
