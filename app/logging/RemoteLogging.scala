package logging

import org.slf4j.{Logger, LoggerFactory}

trait RemoteLogging {

  protected lazy val _logger: Logger = LoggerFactory.getLogger(getClass.getName)

  protected lazy val logger: RemoteLogger = new RemoteLogger(_logger)

  def logOnEmpty[E](ev: Either[String, E])(implicit remoteStream: RemoteStream): Option[E] = {
    ev match {
      case Left(msg) =>
        logger warn msg
        None
      case Right(v) => Some(v)
    }
  }
}