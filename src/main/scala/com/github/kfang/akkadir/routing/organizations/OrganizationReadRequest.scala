package com.github.kfang.akkadir.routing.organizations

import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.models.organization.Organization
import com.github.kfang.akkadir.utils.{ERROR, Errors}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.Future

case class OrganizationReadRequest(slug: String)(implicit App: AppPackage){
  def getResponse: Future[JsObject] = {
    Organization.findByIdOrSlug(slug).map({
      case None => throw ERROR.notFound(Errors.ORGANIZATION_NOT_FOUND)
      case Some(org) => JsObject("organizations" -> List(org).toJson)
    })
  }
}
