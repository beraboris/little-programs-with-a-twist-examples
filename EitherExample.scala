package littleprograms

import littleprograms._
import GithubConversions._

// Provides the implementation of Either (\/))
import scalaz._

// Library used to decode json strings into scala classes
import argonaut._, Argonaut._

case class Error(reason: String)

/*
An important note on the Either type.

In This example, we are using the Either type provided by scalaz.

The core scala library has an Either type, but this type doesn't have a bias. To
use it in a monadic expression, you always have to specify the prefered side of
the monad.

By default the Either monad implemented by scalaz has a right bias. This means
that the monadic expression will keep on going on as long as we're dealing with
the right type and it will stop when we get the left type.

The scalaz Either monad has some strange syntax that I should expmain. The
either type is represented using \/. For example A \/ B means Either A or B
where A is the left type and B is the right type. The left and right type have
their own special syntax. -\/ stands for the left type and \/- stands for the
right type. Because we're dealing with a monad, we have to wrap values into the
monad. For example: -\/("foo") represents the string "foo" wrapped into either
as the left type. Similarly, \/-("foo") represents the string "foo" wrapped into
either as the right type. 
*/

object EitherExample {
  def getGithubUser(user: String): Error \/ GithubUser = for {
    // call api to get user with name
    json <- toEither(GithubClient.user(user))
    // parse result
    obj <- parse[GithubUser](json)
  } yield obj

  def getMostPopularRepository(user: GithubUser): Error \/ GithubRepository = for {
    // call api to get all repos for user
    json <- toEither(GithubClient.repos(user.name))
    // parse list of repos
    repos <- parse[List[GithubRepository]](json)
    // Find the repo with most stars (fail on empty list)
    mostPopularRepo <- repos match {
      case List() => -\/(Error("User has no repositories"))
      case list => \/-(list.maxBy(_.stars))
    }
  } yield mostPopularRepo

  def getBiggestContributor(repo: GithubRepository): Error \/ GithubContributor = for {
    // call api to get all contributors for the given repo
    json <- toEither(GithubClient.repoContributors(repo.owner.name, repo.name))
    // parse list of contributors
    contributors <- parse[List[GithubContributor]](json)
    // Find the contributor with the most contributions (fail on empty list)
    biggestContributor <- contributors match {
      case List() => -\/(Error("Repository has no contributors"))
      case list => \/-(list.maxBy(_.contributions))
    }
  } yield biggestContributor

  /**
  Fetches the biggest contributor of the most popular repository of a given user

  There are two cases.

  If we find what we are looking for, we display
  \/-("repo/user: biggestContributor")

  If there is some kind of error or issue, we display
  -\/(Error("some relevant error message"))
  */
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

  /**
  Convert a GithubResponse to Error \/ String

  If the response is a 200, we return the body, otherwise we return an error
  with the error code and the body of the error.
  */
  private def toEither(response: GithubResponse) = response match {
    case GithubResponse(200, body) => \/-(body)
    case GithubResponse(code, body) => -\/(Error(s"Request error $code. $body"))
  }

  /**
  Parse a json string to an object of the given class or returns a parsing error
  */
  private def parse[A](json: String)(implicit decoder: DecodeJson[A]) =
    json.decodeEither[A].leftMap(Error(_))
}
