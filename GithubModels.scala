package littleprograms

import argonaut._, Argonaut._

case class GithubUser(id: Int, name: String)

case class GithubRepository(id: Int, name: String, stars: Int)

object GithubConversions {
  /** Defines how to deserialize the json for a github user */
  implicit def GithubUserDecode =
    jdecode2L(GithubUser.apply)("id", "login")

  /** Defines how to deserialize a repository from json */
  implicit def GithubRepositoryDecode =
    jdecode3L(GithubRepository.apply)("id", "name", "stargazers_count")
}
