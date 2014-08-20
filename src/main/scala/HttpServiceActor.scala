import akka.actor.Actor.Receive
import akka.actor.{ActorLogging, Props, ActorRefFactory, Actor}
import spray.routing._
import spray.util.LoggingContext

object HttpServiceActor {
  def props(route: Route): Props = Props(classOf[HttpServiceActor], route)
}

class HttpServiceActor(route: Route) extends Actor with ActorLogging with HttpService {

  def actorRefFactory: ActorRefFactory = context
  def receive: Receive = runRoute(route)

}
