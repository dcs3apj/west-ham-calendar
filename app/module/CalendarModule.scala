/**
 * Copyright 2013 Alex Jones
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with work for additional information
 * regarding copyright ownership.  The ASF licenses file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package module

import com.typesafe.config.ConfigFactory
import com.tzavellas.sse.guice.ScalaModule
import uk.co.unclealex.hammers.calendar.dao.GameDao
import uk.co.unclealex.hammers.calendar.dao.SquerylGameDao
import uk.co.unclealex.hammers.calendar.dao.Transactional
import uk.co.unclealex.hammers.calendar.html.HtmlGamesScanner
import uk.co.unclealex.hammers.calendar.html.TicketsHtmlSingleGameScanner
import uk.co.unclealex.hammers.calendar.html.SeasonHtmlGamesScanner
import uk.co.unclealex.hammers.calendar.html.HtmlPageLoader
import uk.co.unclealex.hammers.calendar.html.HtmlPageLoaderImpl
import uk.co.unclealex.hammers.calendar.dates.DateServiceImpl
import uk.co.unclealex.hammers.calendar.dates.DateService
import uk.co.unclealex.hammers.calendar.html.MainPageServiceProvider
import uk.co.unclealex.hammers.calendar.html.MainPageService
import uk.co.unclealex.hammers.calendar.update.MainUpdateService
import uk.co.unclealex.hammers.calendar.update.MainUpdateServiceImpl
import java.net.URI
import uk.co.unclealex.hammers.calendar.update.TicketsHtmlGamesScannerFactory
import uk.co.unclealex.hammers.calendar.update.TicketsHtmlGamesScannerFactoryImpl
/**
 * @author alex
 *
 */
class CalendarModule extends ScalaModule {

  /**
   * The configuration object supplied with this application.
   */
  val config = ConfigFactory.load()

  override def configure {
    // Persistence
    bind[Transactional].toInstance(SquerylGameDao)
    
    // Dates
    bind[DateService].to[DateServiceImpl]
    
    // Game harvesting and update services
    bind[TicketsHtmlGamesScannerFactory].to[TicketsHtmlGamesScannerFactoryImpl]
    bind[HtmlGamesScanner].to[SeasonHtmlGamesScanner]
    bind[URI].annotatedWithName("mainPage").toInstance(new URI("http://www.whufc.com/page/Home/0,,12562,00.html"))
    bind[HtmlPageLoader].to[HtmlPageLoaderImpl]
    bind[MainPageService].toProvider[MainPageServiceProvider]
    bind[MainUpdateService].to[MainUpdateServiceImpl]
  }
}