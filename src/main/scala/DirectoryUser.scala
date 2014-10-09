import java.util.UUID

import com.zipfworks.sprongo.{ExtendedJsonProtocol, Model}

case class DirectoryUser(
  name: String,
  id: String = UUID.randomUUID().toString
) extends Model

object DirectoryUser extends ExtendedJsonProtocol {
  implicit val duJS = jsonFormat2(DirectoryUser.apply)
}
