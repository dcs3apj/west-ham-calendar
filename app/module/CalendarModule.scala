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
import controllers.SecretToken
import dao._
import dates.{DateService, DateServiceImpl, NowService, SystemNowService}
import json.ConfigurationReader
import location._
import pdf.{PdfBoxPriorityPointsPdfFactory, PdfPositioning, PriorityPointsPdfFactory}
import play.api.Configuration
import play.api.cache.CacheApi
import scaldi.Module
import security.Authorised
import security.Definitions.Auth
import security.models.daos.{CredentialsStorage, PlayCacheCredentialsStorage}
import services.{GameRowFactory, GameRowFactoryImpl}
import update._
import update.fixtures.{FixturesGameScanner, FixturesGameScannerImpl}
import update.tickets.{TicketsGameScanner, TicketsGameScannerImpl}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * @author alex
 *
 */
class CalendarModule extends Module {

  /**
   * The configuration object supplied with this application.
   */
  val config = ConfigFactory.load

    // Persistence
    bind[DatabaseConfigFactory] to injected[PlayDatabaseConfigFactory]
    bind[NowService] to new SystemNowService()
    bind[GameDao] to injected[SlickGameDao]

    // Dates
    bind[DateService] to injected[DateServiceImpl]
    bind[LastUpdated] to injected[PlayCacheLastUpdated]

    // Game harvesting and update services
    bind[URI] to new URI("http://www.whufc.com")
    bind[FixturesGameScanner] to injected[FixturesGameScannerImpl]
    bind[TicketsGameScanner] to injected[TicketsGameScannerImpl]
    bind[MainUpdateService] to injected[MainUpdateServiceImpl]

    // Calendars
    bind[CalendarFactory] to injected[CalendarFactoryImpl]
    bind[CalendarWriter] to injected[IcalCalendarWriter]

    // Game Locations
    bind[LocationService] to injected[LocationServiceImpl]
    bind[AsyncHttpClient] to injected[DispatchAsyncHttpClient]
    bind[LocationClientKey] to LocationClientKey("AIzaSyCnaYyFjEYYaKIQ6ZQ64Tx-xkKP2kArRzE")

    //MVC
    bind[GameRowFactory] to injected[GameRowFactoryImpl]
    bind[SecretToken] to SecretToken(config.getString("secret"))

    // PDF
    bind[PriorityPointsPdfFactory] to injected[PdfBoxPriorityPointsPdfFactory]
    bind[PdfPositioning] to ConfigurationReader[PdfPositioning]("pdf-positioning.json")
    bind[PriorityPointsConfigurationDao] to injected[SlickPriorityPointsConfigurationDao]

    // Security

  bind[Auth].to {
    Authorised(config.getString("valid-users.users").split(",").map(_.trim()))
  }

  bind[CredentialsStorage] toProvider new PlayCacheCredentialsStorage(inject[CacheApi], 15.minutes)(inject[ExecutionContext])

}