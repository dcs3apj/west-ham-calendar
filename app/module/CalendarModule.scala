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

import java.net.URI

import cal.{CalendarFactory, CalendarFactoryImpl, CalendarWriter, IcalCalendarWriter}
import com.google.inject.Provides
import com.typesafe.config.ConfigFactory
import com.tzavellas.sse.guice.ScalaModule
import dao._
import dates.{DateService, DateServiceImpl, NowService, SystemNowService}
import json.ConfigurationReader
import location.{AsyncHttpClient, DispatchAsyncHttpClient, LocationService, LocationServiceImpl}
import pdf.{PdfBoxPriorityPointsPdfFactory, PdfPositioning, PriorityPointsPdfFactory}
import play.api.cache.CacheApi
import security.Authorised
import security.Definitions.Auth
import security.models.daos.{CredentialsStorage, PlayCacheCredentialsStorage}
import services.{GameRowFactory, GameRowFactoryImpl}
import update._
import update.fixtures.FixturesGameScanner
import update.tickets.TicketsGameScanner

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * @author alex
 *
 */
class CalendarModule extends ScalaModule {

  /**
   * The configuration object supplied with this application.
   */
  val config = ConfigFactory.load()

  override def configure() {
    // Persistence
    bind[NowService].to[SystemNowService]
    bind[GameDao].to[SlickGameDao]

    // Dates
    bind[DateService].to[DateServiceImpl]
    bind[LastUpdated].to[PlayCacheLastUpdated]
    
    // Game harvesting and update services
    bind[URI].toInstance(new URI("http://www.whufc.com"))
    bind[GameScanner].annotatedWithName("fixturesGameScanner").to[FixturesGameScanner]
    bind[GameScanner].annotatedWithName("ticketsGameScanner").to[TicketsGameScanner]
    bind[MainUpdateService].to[MainUpdateServiceImpl]

    // Calendars
    bind[CalendarFactory].to[CalendarFactoryImpl]
    bind[CalendarWriter].to[IcalCalendarWriter]

    // Game Locations
    bind[LocationService].to[LocationServiceImpl]
    bind[AsyncHttpClient].to[DispatchAsyncHttpClient]
    bind[String].annotatedWithName("locationClientKey").toInstance("AIzaSyCnaYyFjEYYaKIQ6ZQ64Tx-xkKP2kArRzE")

    //MVC
    bind[GameRowFactory].to[GameRowFactoryImpl]
    bind[String].annotatedWithName("secret").toInstance(config.getString("secret"))

    // PDF
    bind[PriorityPointsPdfFactory].to[PdfBoxPriorityPointsPdfFactory]
    bind[PdfPositioning].toInstance(ConfigurationReader[PdfPositioning]("pdf-positioning.json"))
    bind[PriorityPointsConfigurationDao].to[SlickPriorityPointsConfigurationDao]

    // Security

    val validUsers = ((path: String) => if (config.hasPath(path)) config.getString(path) else "")("valid-users.users")
    bind[Auth].toInstance(Authorised(validUsers.split(",").map(_.trim())))
  }

  @Provides
  def provideCredentialsStorage(cacheApi: CacheApi)(implicit ec: ExecutionContext): CredentialsStorage = {
    new PlayCacheCredentialsStorage(cacheApi, 15.minutes)
  }
}