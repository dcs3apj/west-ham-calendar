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
package services

import models.GameRow
import uk.co.unclealex.hammers.calendar.model.Game
import org.joda.time.DateTime
import models.GameTimeType
import models.GameTimeType._
import uk.co.unclealex.hammers.calendar.dates.DateTimeImplicits._
import uk.co.unclealex.hammers.calendar.geo.GeoLocation
import uk.co.unclealex.hammers.calendar.geo.GeoLocation._
import uk.co.unclealex.hammers.calendar.model.Location._
import java.util.Date

/**
 * @author alex
 *
 */
class GameRowFactoryImpl(ticketFactory: Game => Option[DateTime]) extends GameRowFactory {

  def timeTypeOf(dateTime: DateTime): GameTimeType = {
    if (dateTime.isThreeOClockOnASaturday) {
      ThreePmSaturday
    } else if (dateTime.isWeekday) {
      Weekday
    } else {
      Weekend
    }
  }

  def toRow(game: Game): GameRow = {
    game.at match {
      case Some(gameAt) =>
        GameRow(
          id = game.id,
          at = gameAt,
          gameTimeType = timeTypeOf(gameAt),
          season = game.season,
          opponents = game.opponents,
          competition = game.competition,
          location = game.location,
          geoLocation = game.location match {
            case HOME => Some(WEST_HAM)
            case AWAY => GeoLocation(game.opponents)
          },
          result = game.result,
          ticketsAt = ticketFactory(game),
          attended = game.attended getOrElse false)
      case None => throw new IllegalStateException(s"Game $game did not have it's date played attribute set.")
    }
  }
}

object GameRowFactoryImpl {

  def apply(ticketType: String): GameRowFactory = {
    val ticketFactory: Game => Option[DateTime] = ticketType.toUpperCase match {
      case "BONDHOLDER" => game => game.bondholdersAvailable
      case "PRIORITYPOINT" => game => game.priorityPointAvailable
      case "SEASON" => game => game.seasonTicketsAvailable
      case "ACADEMY" => game => game.academyMembersAvailable
      case "GENERAL" => game => game.generalSaleAvailable
      case _ => throw new IllegalArgumentException(s"$ticketType is not a valid type of ticket")
    }
    new GameRowFactoryImpl(ticketFactory)
  }
}