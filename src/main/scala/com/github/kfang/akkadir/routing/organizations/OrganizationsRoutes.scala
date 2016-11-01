package com.github.kfang.akkadir.routing.organizations

import akka.http.scaladsl.server.Route
import com.github.kfang.akkadir.AppPackage
import com.github.kfang.akkadir.utils.AppRoutes

class OrganizationsRoutes(implicit App: AppPackage) extends AppRoutes(App){

  private val createOrganization: Route = (
    post &
    pathEnd &
    entity(as[OrganizationCreateRequest]) &
    requiredAuth
  ){ (request, user) => request.getResponse(user) }

  override val routes: Route = pathPrefix("organizations"){
    createOrganization              //=> POST /v1/organizations
  }
}
