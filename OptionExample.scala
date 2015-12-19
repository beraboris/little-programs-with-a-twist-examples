package littleprograms

import littleprograms._
import GithubConversions._

import argonaut._, Argonaut._

object OptionExample {
  def getGithubUser(name: String): Option[GithubUser] = {
    // If we get a 200, we wrap the body string in Option,
    // otherwise we return nothing
    val maybeJson = GithubClient.user(name) match {
      case GithubResponse(200, body) => Some(body)
      case _ => None
    }

    // parse json
    for {
      json <- maybeJson
      user <- json.decodeOption[GithubUser]
    } yield user
  }

  def getMostPopularRepository(user: GithubUser): Option[GithubRepository] = {
    None
  }

  def getBiggestContributor(repository: GithubRepository): Option[GithubUser] = {
    None
  }

  def run() {
    println("Looking for the biggest contibutor using Option")

    val biggestContributor = for {
      user <- getGithubUser("beraboris")
      repo <- getMostPopularRepository(user)
      contributor <- getBiggestContributor(repo)
    } yield s"$user/$repo: $contributor"

    println("And the winner is...")
    println(biggestContributor)
  }
}
