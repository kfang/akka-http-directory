package com.github.kfang.akkadir.routing.organizations

import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.models.organization.Organization
import com.github.kfang.akkadir.models.user.User
import com.github.kfang.akkadir.utils.{ERROR, Errors}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.Future

case class OrganizationReadRequest(slug: String)(implicit App: AppPackage){

  private implicit val __ctx = App.system.dispatcher
  private implicit val __db = App.db

  private def validate(user: User, organization: Organization): Future[Option[ERROR]] = {
    val _userNotInOrgnaziationError = User.isPartOfOrganization(user._id, organization._id).map({
      case true => None
      case false => Some(Errors.USER_NOT_IN_ORGANIZATION)
    })

    Future.sequence(List(
      _userNotInOrgnaziationError
    )).map(_.flatten match {
      case Nil => None
      case ers => Some(ERROR.badRequest(ers.toMap))
    })
  }

  def getResponse(user: User): Future[JsObject] = {
    Organization.findByIdOrSlug(slug).map({
      case None => throw ERROR.notFound(Errors.ORGANIZATION_NOT_FOUND)
      case Some(org) => org
    }).flatMap(org => validate(user, org).map({
      case Some(error) => throw error
      case None => JsObject("organizations" -> List(org).toJson)
    }))
  }
}
