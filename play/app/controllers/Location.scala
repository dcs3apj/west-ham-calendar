package controllers

import javax.inject.Inject

import location.LocationService
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

class Location @Inject() (val locationService: LocationService) extends Controller {

  def location(gameId: Long) = Action.async {
    locationService.location(gameId).map {
      case Some(url) => TemporaryRedirect(url.toString)
      case _ => NotFound
    }
  }
}