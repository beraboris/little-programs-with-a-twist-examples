package littleprograms

import argonaut._, Argonaut._

case class GithubUser(id: Int, name: String)

case class GithubRepository(name: String)

object GithubConversions {
  /** Defines how to deserialize the json for a github user */
  implicit def GithubUserDecode = jdecode2L(GithubUser.apply)("id", "login")
}
