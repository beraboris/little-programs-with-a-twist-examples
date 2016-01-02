package littleprograms

import littleprograms._
import GithubConversions._

// Library used to decode json strings into scala classes
import argonaut._, Argonaut._

object OptionExample {
  def getGithubUser(name: String): Option[GithubUser] = for {
    // make request to gh
    json <- getResponseBody(GithubClient.user(name))
    // parse json
    user <- json.decodeOption[GithubUser]
  } yield user

  def getMostPopularRepository(user: GithubUser): Option[GithubRepository] = for {
    // Make request to gh
    json <- getResponseBody(GithubClient.repos(user.name))
    // Parse json
    repos <- json.decodeOption[List[GithubRepository]]
    // Find the most popular repo in the list
    mostPopularRepo <- repos.reduceLeftOption((best, curr) => if (curr.stars > best.stars) curr else best)
  } yield mostPopularRepo

  def getBiggestContributor(repository: GithubRepository): Option[GithubContributor] = for {
    // make request to gh
    json <- getResponseBody(GithubClient.repoContributors(repository.owner.name, repository.name))
    // parse json
    contributors <- json.decodeOption[List[GithubContributor]]
    // Find the one with the most contributons
    biggestContributor <- contributors.reduceLeftOption((best, curr) => if (curr.contributions > best.contributions) curr else best)
  } yield biggestContributor

  /**
  Fetch the biggest contributor of a github user's most popular repository

  There are two possible results here.
  If we found what we were looking for
  we return Some("user/repo: biggestContributor")

  If there were any problems: (user missing, repo missing, contributor missing,
  http error, etc.) we return None
  */
  def run(user: String) {
    println(s"Looking for the biggest contributor of $user's most popular repository using Option")

    val biggestContributor = for {
      user <- getGithubUser(user)
      repo <- getMostPopularRepository(user)
      contributor <- getBiggestContributor(repo)
    } yield s"${repo.owner.name}/${repo.name}: ${contributor.name}"

    println("And the winner is...")
    println(biggestContributor)
  }

  /**
  Converts a GithubResponse to and Option[String]

  If the response has a 200 status code we return Some(body) otherwise we
  return None
  */
  private def getResponseBody(response: GithubResponse) = response match {
    case GithubResponse(200, body) => Some(body)
    case _ => None
  }
}
