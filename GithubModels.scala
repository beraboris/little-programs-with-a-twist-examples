package littleprograms

import argonaut._, Argonaut._

case class GithubUser(id: Int, name: String)

case class GithubContributor(id: Int, name: String, contributions: Int)

case class GithubRepository(id: Int, name: String, owner: GithubUser, stars: Int)

object GithubConversions {
  /** Defines how to deserialize the json for a github user */
  implicit def GithubUserDecode =
    jdecode2L(GithubUser.apply)("id", "login")

  /** Defines how to deserialize the json for a github user */
  implicit def GithubContributorDecode =
    jdecode3L(GithubContributor.apply)("id", "login", "contributions")

  /** Defines how to deserialize a repository from json */
  implicit def GithubRepositoryDecode =
    jdecode4L(GithubRepository.apply)("id", "name", "owner", "stargazers_count")
}
