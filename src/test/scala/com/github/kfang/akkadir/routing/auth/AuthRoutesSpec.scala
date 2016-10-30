package com.github.kfang.akkadir.routing.auth

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.github.kfang.akkadir.MainTest
import org.scalatest.{Matchers, WordSpec}
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Cookie
import com.github.kfang.akkadir.models.profile.ProfileName
import spray.json.JsObject

class AuthRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest {

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

    "allow registration" in {
      val arr = AuthRegisterRequest(email = "fjdakslfjdkl", password = "fjdskl", name = ProfileName(first = "", last = ""))
      Post("/auth/register", arr) ~> authRoutes ~> check {
        println(responseAs[JsObject].prettyPrint)
        assert(status == StatusCodes.BadRequest)
        assert(responseAs[JsObject].fields.keys.toSeq.contains("errors"))
      }
    }

  }

}
