import java.net.{HttpURLConnection, URL, URLEncoder}
import java.util.logging.{Level, Logger}

import de.fuberlin.wiwiss.silk.linkagerule.input.Transformer
import de.fuberlin.wiwiss.silk.runtime.plugin.Plugin

import scala.io.Source

@Plugin(
  id = "foursquare",
  categories = Array("API"),
  label = "Foursquare",
  description = "Get the Foursquare Id."
)
case class FoursquareTransformer(clientId: String, clientSecret: String, prefix: String = "http://foursquare.com/venue/", limit: Int = 1) extends Transformer {

  private val logger = Logger.getLogger(getClass.getName)

  /**
   * Receive the parameters for the query to Foursquare. The parameters must be ordered: (1) the venue name, (2) the
   * locality, (3) the country.
   *
   * @param values
   * @return A list of potential Foursquare Ids.
   */
  override def apply(values: Seq[Set[String]]): Set[String] = {

    // Get a vector from the Seq[Set[...]] in order to access the elements by their index to build the query string.
    val parameters = values.reduce(_ union _).toVector

    parameters.size match {

      // Return an empty set if the parameters size is less than 3.
      case size if size < 3 =>
        logger.log(Level.WARNING, s"Expected three parameters [ parameters size :: $size ]")
        Set()

      // Process the parameters.
      case _ =>
        val queryString = "?intent=match" +
          "&client_id=" + clientId + // The client Id.
          "&client_secret=" + clientSecret + // The client secret.
          "&v=" + FoursquareTransformer.API_VERSION + // The Foursquare API version (defined in the object).
          "&query=" + URLEncoder.encode(parameters(0), "UTF-8") +
          "&near=" + URLEncoder.encode(parameters(1) + ", " + parameters(2), "UTF-8")

        logger.log(Level.FINE, "[ query-string :: " + queryString + " ]")

        // Return the set of Ids otherwise return an empty set.
        FoursquareClient.request("venues/search", queryString).getOrElse(Set())
          .map(prefix + _) // add the prefix.
          .slice(0, limit) // return only the number of requested results.

    }

  }

}

/**
 *
 */
object FoursquareTransformer {

  // The supported Foursquare API version.
  private val API_VERSION = "20141113"

}

// if you don't supply your own Protocol

/**
 * Provides client features to Foursquare.
 */
object FoursquareClient {

  import play.api.libs.json._

  private val logger = Logger.getLogger(FoursquareClient.getClass.getName)

  private val API_URL = "https://api.foursquare.com/v2/"

  def request(path: String, queryString: String): Option[Set[String]] = {

    logger.log(Level.FINE, s"Going to perform a request to Foursquare [ path :: $path ][ query-string :: $queryString ]")

    // Combine the request in one URL.
    val url = new URL(API_URL + path + queryString)
    val connection = url.openConnection().asInstanceOf[HttpURLConnection]
    connection.setRequestMethod("GET")
    connection.setRequestProperty("Accept", "application/json")
    connection.connect()

    // Process only successful responses.
    connection.getResponseCode match {

      // Error response code.
      case code: Int if code < 200 || code >= 300 =>
        val responseMessage = connection.getResponseMessage
        logger.log(Level.WARNING, s"An error occurred [ code :: $code ][ url :: $url ][ response :: $responseMessage ].") // error
        None // Return no results.

      // Success (2xx)
      case _ =>

        // Get the response string using the specified encoding (or use UTF-8 by default).
        val encoding = if (null != connection.getContentEncoding) connection.getContentEncoding else "UTF-8"
        val responseString = Source.fromInputStream(connection.getInputStream, encoding).mkString

        // Decode the JSON.
        val json: JsValue = Json.parse(responseString)
        logger.log(Level.FINER, s"[ response-string :: $responseString ]")

        // Check Foursquare response.
        json \ "meta" \ "code" match {

          case code: JsValue if code.as[Int] != 200 => // error
            logger.log(Level.WARNING, s"Foursquare returned an error [ code :: $code ][ response :: $responseString ].")
            None // Return no results.

          case _ =>
            // Check the venues in the JSON response.
            json \ "response" \ "venues" match {

              // Foursquare found no venues
              case venues: JsArray if 0 == venues.value.size => // no venues found.
                logger.log(Level.WARNING, "No venues found.")
                None // Return no results.

              // Return the venues IDs.
              case venues: JsArray =>
                logger.log(Level.INFO, s"Found ${venues.value.size} venue(s).")
                // Return some results.
                Some(venues.value.map((venue: JsValue) => (venue \ "id").as[String]).toSet)
            }

        }


    }
  }
}
