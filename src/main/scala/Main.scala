import akka.actor.ActorSystem
import akka.io.IO
import akka.util.Timeout
import com.zipfworks.sprongo.{ExtendedJsonProtocol, SprongoDSL}
import reactivemongo.core.nodeset.Authenticate
import spray.can.Http
import spray.httpx.SprayJsonSupport
import spray.routing.{Directives, Route}
import scala.concurrent.duration._
import Directives._
import akka.pattern.ask

object Main extends App with SprayJsonSupport with ExtendedJsonProtocol {

  implicit val timeout = Timeout(5.seconds)
  implicit val system: ActorSystem = ActorSystem("directory")

  val DB_URLS = Seq("localhost")
  val DB_AUTH = List(Authenticate("admin", "superman", "12345678"))
  val DirDB = new DirectoryDriver(DB_URLS, system, DB_AUTH)

  val routing: Route = get {
    complete("hello")
  }

  val service = system.actorOf(HttpServiceActor.props(routing))
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
