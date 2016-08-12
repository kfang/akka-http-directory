package com.github.kfang.akkadir.routing.auth

import com.github.kfang.akkadir.utils.JsonBsonHandlers._
import com.github.kfang.akkadir.models.profile.{ProfileAddress, ProfileName, ProfilePhone}

case class AuthRegisterRequest(
  email: String,
  password: String,
  name: ProfileName,
  phone: Option[ProfilePhone],
  address: Option[ProfileAddress]
)

object AuthRegisterRequest {
  implicit val jsf = jsonFormat5(AuthRegisterRequest.apply)
}
