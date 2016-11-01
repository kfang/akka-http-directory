package com.github.kfang.akkadir.routing.organizations

import akka.http.scaladsl.server.Route
import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.utils.AppRoutes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

class OrganizationsRoutes(implicit App: AppPackage) extends AppRoutes(App){

  private val createOrganization: Route = (
    post &
    pathEnd &
    entity(as[OrganizationCreateRequest]) &
    requiredAuth
  ){ (request, user) => request.getResponse(user) }

  private val readOrganization: Route = (
    get &
    path(idOrSlug)
  ){ (slug) => OrganizationReadRequest(slug).getResponse }

  override val routes: Route = pathPrefix("organizations"){
    createOrganization ~            //=> POST /v1/organizations
    readOrganization                //=> GET  /v1/organizations/{:id,:slug}
  }
}
