import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.util.{Failure, Success}

object Main extends App {

  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()
  private implicit val executionContext = system.dispatcher

  private val route = (get & pathEnd & complete){ "Hello World!" }
  private val _serverBinding = Http().bindAndHandle(route, "localhost", 8080)

  _serverBinding.andThen({
    case Success(sb) => system.log.info(s"Binding Successful: ${sb.localAddress}")
    case Failure(er) => system.log.error(er, "Binding Failure")
  })
}
