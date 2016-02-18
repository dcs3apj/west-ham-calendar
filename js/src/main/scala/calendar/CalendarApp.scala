package calendar

/**
  * Created by alex on 07/02/16.
  */

import com.greencatsoft.angularjs.Angular

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object CalendarApp extends JSApp {

  override def main() {
    val module = Angular.module("hammers-calendar")

    module
      .controller[CalendarCtrl]
      .factory[AjaxServiceFactory]
  }
}