package littleprograms

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.util._

object FutureExample {
  def getGithubUser(user: String): Future[GithubUser] = Future {
    throw new NotImplementedError()
  }

  def getMostPopularRepository(user: GithubUser): Future[GithubRepository] = Future {
    throw new NotImplementedError()
  }

  def getBiggestContributor(repo: GithubRepository): Future[GithubContributor] = Future {
    throw new NotImplementedError()
  }

  def run(user: String) {
    println(s"Looking for the biggest contributor of $user's most popular repository using Future")

    val biggestContributor = for {
      user <- getGithubUser(user)
      repo <- getMostPopularRepository(user)
      contributor <- getBiggestContributor(repo)
    } yield s"${repo.owner.name}/${repo.name}: ${contributor.name}"

    println("And the winner is...")
    biggestContributor onComplete {
      case Success(contributor) => println(contributor)
      case Failure(e) => {
        val errorText = errorWithCauses(e)
        println(s"Oops! Something went wrong: $errorText")
      }
    }

    // We block and wait for the result
    Await.ready(biggestContributor, Duration.Inf)
  }

  def errorWithCauses(error: Throwable, isCause: Boolean = false): String = {
    val text = if (isCause) s"Caused by: $error" else error.toString

    (for {
      cause <- Option(error.getCause())
      causeText = errorWithCauses(cause, true)
    } yield s"$text\n$causeText")
      .getOrElse(text)
  }
}
