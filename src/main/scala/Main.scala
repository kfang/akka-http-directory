import akka.actor.ActorSystem
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import spray.routing.{Directives, Route}
import scala.concurrent.duration._
import Directives._
import akka.pattern.ask

object Main extends App{

  implicit val timeout = Timeout(5.seconds)
  implicit val system: ActorSystem = ActorSystem("directory")

  val routing: Route = get {
    complete("Hello World!")
  }

  val service = system.actorOf(HttpServiceActor.props(routing))
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
