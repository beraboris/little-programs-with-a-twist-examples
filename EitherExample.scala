package littleprograms

import littleprograms._
import GithubConversions._

// Provides the implementation of Either (\/))
import scalaz._

// Library used to decode json strings into scala classes
import argonaut._, Argonaut._

case class Error(reason: String)

object EitherExample {
  def getGithubUser(user: String): Error \/ GithubUser = for {
    json <- toEither(GithubClient.user(user))
    obj <- parse[GithubUser](json)
  } yield obj

  def getMostPopularRepository(user: GithubUser): Error \/ GithubRepository = for {
    json <- toEither(GithubClient.repos(user.name))
    repos <- parse[List[GithubRepository]](json)
    mostPopularRepo <- repos match {
      case List() => -\/(Error("User has no repositories"))
      case list => \/-(list.maxBy(_.stars))
    }
  } yield mostPopularRepo

  def getBiggestContributor(repo: GithubRepository): Error \/ GithubContributor = for {
    json <- toEither(GithubClient.repoContributors(repo.owner.name, repo.name))
    contributors <- parse[List[GithubContributor]](json)
    biggestContributor <- contributors match {
      case List() => -\/(Error("Repository has no contributors"))
      case list => \/-(list.maxBy(_.contributions))
    }
  } yield biggestContributor

  def run(user: String) {
    println(s"Looking for the biggest contributor of $user's most popular repository using Either (\\/)")

    val biggestContributor = for {
      user <- getGithubUser(user)
      repo <- getMostPopularRepository(user)
      contributor <- getBiggestContributor(repo)
    } yield s"${repo.owner.name}/${repo.name}: ${contributor.name}"

    println("And the winner is...")
    println(biggestContributor)
  }

  private def toEither(response: GithubResponse) = response match {
    case GithubResponse(200, body) => \/-(body)
    case GithubResponse(code, body) => -\/(Error(s"Request error $code. $body"))
  }

  private def parse[A](json: String)(implicit decoder: DecodeJson[A]) =
    json.decodeEither[A].leftMap(Error(_))
}
