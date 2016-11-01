package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.github.kfang.akkadir.MainTest
import org.scalatest.{Matchers, WordSpec}
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Cookie
import com.github.kfang.akkadir.models.profile.ProfileName
import com.github.kfang.akkadir.utils.Errors
import spray.json.JsObject
import scala.concurrent.duration._

class AuthRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest {

  implicit val to: RouteTestTimeout = RouteTestTimeout(1.minute)
  private implicit val __app = MainTest.app
  val authRoutes = new AuthRoutes().routes

  "Auth Routes" should {
    "return empty object on /check if there is no cookie" in {
      Get("/auth") ~> authRoutes ~> check {
        assert(responseAs[JsObject] == JsObject())
      }
    }

    "return empty object on /check if the cookie is invalid" in {
      Get("/auth").addHeader(Cookie(__app.config.USER_COOKIE_NAME, "")) ~> authRoutes ~> check {
        assert(responseAs[JsObject] == JsObject())
      }
    }

    "reject invalid information in registration" in {
      val arr = AuthRegisterRequest(email = "fjdakslfjdkl", password = "fjdskl", name = ProfileName(first = "", last = ""))
      Post("/auth/register", arr) ~> authRoutes ~> check {
        lazy val errors = responseAs[JsObject].fields.get("errors")
        lazy val errorKeys = errors.map(_.asJsObject.fields.keys).getOrElse(Nil).toList
        assert(status == StatusCodes.BadRequest)
        assert(errors.isDefined)
        assert(errorKeys.contains(Errors.INVALID_EMAIL._1))
        assert(errorKeys.contains(Errors.EMPTY_STRING._1))
        assert(errorKeys.contains(Errors.PASSWORD_TOO_SHORT._1))
      }
    }

    "allow valid registration parameters" in {
      val arr = AuthRegisterRequest(email = "test@domain.com", password = "12345678", name = ProfileName(first = "first", last = "last"))
      Post("/auth/register", arr) ~> authRoutes ~> check {
        lazy val rFields = responseAs[JsObject].fields
        lazy val users = rFields.get("users")
        lazy val profiles= rFields.get("profiles")
        assert(status == StatusCodes.OK)
        assert(users.isDefined)
        assert(profiles.isDefined)
      }
    }

    "reject duplicate email" in {
      val arr = AuthRegisterRequest(email = "test+tag@domain.com", password = "12345678", name = ProfileName(first = "first", last = "last"))
      Post("/auth/register", arr) ~> authRoutes ~> check {
        lazy val errors = responseAs[JsObject].fields.get("errors")
        lazy val errorKeys = errors.map(_.asJsObject.fields.keys).getOrElse(Nil).toList
        assert(status == StatusCodes.BadRequest)
        assert(errors.isDefined)
        assert(errorKeys.contains(Errors.EMAIL_EXISTS._1))
      }
    }

  }

}
