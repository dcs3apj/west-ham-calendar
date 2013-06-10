/**
 * Copyright 2013 Alex Jones
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
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
 * @author unclealex72
 *
 */

package uk.co.unclealex.hammers.calendar.cal

import scala.collection.Traversable
import uk.co.unclealex.hammers.calendar.model.Game
import uk.co.unclealex.hammers.calendar.dates.DateTimeImplicits._
import org.joda.time.DateTime
import org.joda.time.Duration
import scala.collection.SortedSet
import uk.co.unclealex.hammers.calendar.geo.GeoLocation
import uk.co.unclealex.hammers.calendar.search.GameOrTicketSearchOption
import uk.co.unclealex.hammers.calendar.search.GameOrTicketSearchOption._
import uk.co.unclealex.hammers.calendar.search.AttendedSearchOption
import uk.co.unclealex.hammers.calendar.search.LocationSearchOption
import javax.inject.Inject
import uk.co.unclealex.hammers.calendar.dao.Transactional
/**
 * @author alex
 *
 */
class CalendarFactoryImpl @Inject() (
  /**
   * The transactional object to use for data access
   */
  tx: Transactional) extends CalendarFactory {

  def create(
    attendedSearchOption: AttendedSearchOption,
    locationSearchOption: LocationSearchOption,
    gameOrTicketSearchOption: GameOrTicketSearchOption): Calendar = {
    val games = tx(_ search (attendedSearchOption, locationSearchOption, gameOrTicketSearchOption))
    val (dateFactory, duration) = gamePeriodFactory(gameOrTicketSearchOption)
    val busy = attendedSearchOption match {
      case AttendedSearchOption.ATTENDED => true
      case _ => false
    }
    val entries = games map convert(dateFactory, busy, Duration.standardHours(duration)) flatten
    val title = createTitle(attendedSearchOption, locationSearchOption, gameOrTicketSearchOption)
    Calendar("whufc" + title.replace(' ', '_'), title, SortedSet.empty[Event] ++ entries)
  }

  def gamePeriodFactory(gameOrTicketSearchOption: GameOrTicketSearchOption): Pair[Game => Option[DateTime], Int] = {
    gameOrTicketSearchOption match {
      case BONDHOLDERS => (g => g.bondholdersAvailable, 1)
      case PRIORITY_POINT => (g => g.priorityPointAvailable, 1)
      case SEASON => (g => g.seasonTicketsAvailable, 1)
      case ACADEMY => (g => g.academyMembersAvailable, 1)
      case GENERAL_SALE => (g => g.generalSaleAvailable, 1)
      case GAME => (g => g.at, 2)
    }
  }

  def createTitle(attendedSearchOption: AttendedSearchOption,
    locationSearchOption: LocationSearchOption,
    gameOrTicketSearchOption: GameOrTicketSearchOption): String = {
    val titleType = gameOrTicketSearchOption match {
      case BONDHOLDERS => "Bondholder ticket selling"
      case PRIORITY_POINT => "Priority point ticket selling"
      case SEASON => "Season ticket selling"
      case ACADEMY => "Academy member ticket selling"
      case GENERAL_SALE => "General sale ticket selling"
      case GAME => "Game"
    }
    val attendedType = attendedSearchOption match {
      case AttendedSearchOption.ANY => "all"
      case AttendedSearchOption.ATTENDED => "attended"
      case AttendedSearchOption.UNATTENDED => "unattended"
    }
    val locationType = locationSearchOption match {
      case LocationSearchOption.ANY => "all"
      case LocationSearchOption.HOME => "home"
      case LocationSearchOption.AWAY => "away"
    }
    s"$titleType times for ${List("all", attendedType, locationType).distinct.mkString(" ")} games"
  }

  /**
   * Convert a game to into an event if the date factory supplies a date.
   */
  def convert(dateFactory: Game => Option[DateTime], busy: Boolean, duration: Duration): Game => Option[Event] = { game =>
    dateFactory(game) map { date =>
      new Event(
        id = game.id.toString,
        competition = game.competition,
        location = game.location,
        geoLocation = GeoLocation(game),
        opponents = game.opponents,
        dateTime = date,
        duration = duration,
        result = game.result,
        attendence = game.attendence,
        matchReport = game.matchReport,
        televisionChannel = game.televisionChannel,
        busy = busy)
    }
  }
}