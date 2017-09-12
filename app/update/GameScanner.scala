package update

import html.GameUpdateCommand
import logging.RemoteStream
import monads.FE.FutureEitherNel

/**
 * The common interface for services that scan the web for details on West Ham games. Such details can either
 * be when games are played, the result, what games are televised or when tickets are available.
  *
  * @author alex
 *
 */
trait GameScanner {

  def scan(latestSeason: Option[Int])(implicit remoteStream: RemoteStream): FutureEitherNel[String, Seq[GameUpdateCommand]]
}
