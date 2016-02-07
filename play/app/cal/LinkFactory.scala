package cal

import java.net.URI

import model.Location

/**
 * A trait that can create links to this server.
 * Created by alex on 13/04/15.
 */
trait LinkFactory {

  /**
   * Create a link to the location page for a game.
   * @param gameId
   * @return
   */
  def locationLink(gameId: Long): URI
}
