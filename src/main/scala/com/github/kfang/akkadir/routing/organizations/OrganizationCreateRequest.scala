package com.github.kfang.akkadir.routing.organizations

import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.models.organization.Organization
import com.github.kfang.akkadir.models.user.User
import com.github.kfang.akkadir.utils.{ERROR, Errors, SlugUtils}
import reactivemongo.bson.BSONDocument
import spray.json.DefaultJsonProtocol._
import spray.json.JsObject

import scala.concurrent.Future

case class OrganizationCreateRequest(
  name: String
)

object OrganizationCreateRequest {

  implicit val __jsf = jsonFormat1(OrganizationCreateRequest.apply)

  implicit class OrganizationCreate(request: OrganizationCreateRequest)(implicit App: AppPackage){

    private def validate(user: User): Future[Option[ERROR]] = {
      val _slugExistsError = App.db.Organizations.count(Some(BSONDocument(
        "slug" -> SlugUtils.slugify(request.name)
      ))).map(c => if(c > 0) Some(Errors.SLUG_EXISTS) else None)

      Future.sequence(List(
        _slugExistsError
      )).map(_.flatten match {
        case Nil => None
        case ers => Some(ERROR.badRequest(ers.toMap))
      })
    }

    private def createOrganization(user: User): Future[JsObject] = {
      for {
        insertResult <- App.db.Organizations.insert(Organization(
          name = request.name,
          slug = List(SlugUtils.slugify(request.name)),
          owner = List(user._id)
        ))
      } yield {
        JsObject()
      }
    }

    def getResponse(user: User): Future[JsObject] = {
      validate(user).flatMap({
        case Some(error) => throw error
        case None => createOrganization(user)
      })
    }
  }
}
