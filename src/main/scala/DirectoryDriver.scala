import akka.actor.ActorSystem
import com.zipfworks.sprongo.{CollectionDAO, SprongoDB}
import reactivemongo.core.nodeset.Authenticate

class DirectoryDriver(dbUrls: Seq[String], system: ActorSystem, auth: List[Authenticate])
  extends SprongoDB(
    dbUrl = dbUrls,
    dbName = "spray_directory",
    system = Some(system),
    authenticators = auth,
    nbChannelsPerNode = 20){

  object Users extends CollectionDAO[DirectoryUser]("users")

}
