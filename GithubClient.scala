package littleprograms

import org.apache.http.client.fluent._
import org.apache.commons.io.IOUtils
import java.io.InputStream

case class GithubResponse(status: Int, body: String)

/**
 * A simple client that makes call to github returning very simple response
 * objects containing a status code and the whole response string.
 */
object GithubClient {
  private val baseUri = "https://api.github.com"

  /** Fetch a user by name */
  def user(userName: String) = get(s"$baseUri/users/$userName")

  /** Fetch all repositories for a given user */
  def repos(userName: String) = get(s"$baseUri/users/$userName/repos")

  private def get(uri: String) = execute(Request.Get(uri))

  private def execute(request: Request) = {
    val response = request.execute().returnResponse()

    GithubResponse(
      response.getStatusLine().getStatusCode(),
      IOUtils.toString(response.getEntity().getContent())
    )
  }
}
