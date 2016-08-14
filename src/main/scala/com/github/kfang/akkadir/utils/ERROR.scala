package com.github.kfang.akkadir.utils

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import spray.json.{JsArray, JsNumber, JsObject, JsString, JsValue}

case class ERROR(code: StatusCode, errors: Map[String, JsValue]) extends Throwable {

  def getResponse: JsObject = JsObject(
    "code" -> JsNumber(code.intValue),
    "reason" -> JsString(code.reason),
    "message" -> JsString(code.defaultMessage),
    "errors" -> JsObject(errors)
  )

}

object ERROR {
  def apply(err: Throwable): ERROR = ERROR(
    code = StatusCodes.InternalServerError,
    errors = Map(
      "stacktrace" -> JsArray(err.getStackTrace.toVector.map(s => JsString(s.toString))),
      "exception" -> JsString(err.getMessage)
    )
  )

  def badRequest(errors: Map[String, JsValue]): ERROR = ERROR(StatusCodes.BadRequest, errors)
  def badRequest(error: (String, JsValue)): ERROR = ERROR(StatusCodes.BadRequest, Map(error))
  def notFound(errors: Map[String, JsValue]): ERROR = ERROR(StatusCodes.NotFound, errors)
  def notFound(error: (String, JsValue)): ERROR = ERROR(StatusCodes.NotFound, Map(error))
}

object Errors {
  val EMAIL_EXISTS = "email-exists" -> JsString("email already exists")
  val EMPTY_STRING = "empty-string" -> JsString("request contains an empty string")
  val INVALID_EMAIL = "invalid-email" -> JsString("email is invalid")
  val PASSWORD_TOO_SHORT = "pasword-too-short" -> JsString("password is too short")
}
