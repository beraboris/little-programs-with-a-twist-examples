package littleprograms

import littleprograms._
import GithubConversions._

import argonaut._, Argonaut._

object OptionExample {
  def getGithubUser(name: String): Option[GithubUser] = for {
    // make request to gh
    json <- getResponseBody(GithubClient.user(name))
    // parse json
    user <- json.decodeOption[GithubUser]
  } yield user

  def getMostPopularRepository(user: GithubUser): Option[GithubRepository] = for {
    json <- getResponseBody(GithubClient.repos(user.name))
    repos <- json.decodeOption[List[GithubRepository]]
  } yield repos.maxBy(_.stars)

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

  private def getResponseBody(response: GithubResponse) = response match {
    case GithubResponse(200, body) => Some(body)
    case _ => None
  }
}
