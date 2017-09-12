/**
 * Copyright 2010-2012 Alex Jones
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

package html;

import dates.Date._
import dates.{Date, September}
import model.Game
import models.{GameResult, Score, Location, Competition}
import java.time.ZonedDateTime
import org.specs2.mutable.Specification

import scala.reflect.ClassTag

/**
 * The Class GameUpdateCommandSpec.
 *
 * @author alex
 */
class GameUpdateCommandSpec extends Specification {

  val DEFAULT_COMPETITION: Competition = Competition.FACP
  val DEFAULT_LOCATION: Location = Location.HOME
  val DEFAULT_OPPONENTS = "Them"
  val DEFAULT_SEASON = 2012
  val DEFAULT_DATE_PLAYED = September(5, 1972)
  val DEFAULT_BONDHOLDERS_AVAILABLE = September(5, 1973)
  val DEFAULT_PRIORITY_POINT_AVAILABLE = September(5, 1974)
  val DEFAULT_SEASON_TICKETS_AVAILABLE = September(5, 1975)
  val DEFAULT_ACADEMY_TICKETS_AVAILABLE = September(5, 1976)
  val DEFAULT_GENERAL_SALE_TICKETS_AVAILABLE = September(5, 1977)
  val DEFAULT_UPDATE_DATE = September(5, 2013) at (9, 12)
  val DEFAULT_RESULT = GameResult(Score(1, 0))
  val DEFAULT_ATTENDANCE = 100000
  val DEFAULT_MATCH_REPORT = "Good"
  val DEFAULT_TELEVISION_CHANNEL = "BBC"
  val DEFAULT_ATTENDED = false

  /**
   * Test date played.
   */
  "Updating the date played" should {
    testGameUpdateCommand[ZonedDateTime](
      gameLocator => (datePlayed: ZonedDateTime) => DatePlayedUpdateCommand(gameLocator, datePlayed),
      _.at,
      DEFAULT_DATE_PLAYED,
      DEFAULT_DATE_PLAYED plusHours 1,
      _.copy(at = Some(DEFAULT_DATE_PLAYED plusHours 1)))
  }

  "Updating the result" should {
    val newResult: GameResult = DEFAULT_RESULT.copy(maybeShootoutScore = Some(Score(2, 0)))
    testGameUpdateCommand(
      gameLocator => (result: GameResult) => ResultUpdateCommand(gameLocator, result),
      _.result,
      DEFAULT_RESULT,
      newResult,
      _.copy(result = Some(newResult)))
  }

  "Updating the attendence" should {
    testGameUpdateCommand(
      gameLocator => (attendence: Int) => AttendenceUpdateCommand(gameLocator, attendence),
      _.attendance,
      DEFAULT_ATTENDANCE,
      DEFAULT_ATTENDANCE * 2,
      _.copy(attendance = Some(DEFAULT_ATTENDANCE * 2)))
  }

  "Updating the match report" should {
    testGameUpdateCommand(
      gameLocator => (matchReport: String) => MatchReportUpdateCommand(gameLocator, matchReport),
      _.matchReport,
      DEFAULT_MATCH_REPORT,
      DEFAULT_MATCH_REPORT + "!",
      _.copy(matchReport = Some(DEFAULT_MATCH_REPORT + "!")))
  }

  "Updating the television channel" should {
    testGameUpdateCommand(
      gameLocator => (televisionChannel: String) => TelevisionChannelUpdateCommand(gameLocator, televisionChannel),
      _.televisionChannel,
      DEFAULT_TELEVISION_CHANNEL,
      DEFAULT_TELEVISION_CHANNEL + "!",
      _.copy(televisionChannel = Some(DEFAULT_TELEVISION_CHANNEL + "!")))
  }

  "Updating the attended flag" should {
    testGameUpdateCommand(
      gameLocator => (attended: Boolean) => AttendedUpdateCommand(gameLocator, attended),
      g => Some(g.attended),
      DEFAULT_ATTENDED,
      !DEFAULT_ATTENDED,
      _.copy(attended = !DEFAULT_ATTENDED))
  }

  "Updating the bond holder ticket sale date" should {
    testGameUpdateCommand[ZonedDateTime](
      gameLocator => (saleDate: ZonedDateTime) => BondHolderTicketsUpdateCommand(gameLocator, saleDate),
      _.bondholdersAvailable,
      DEFAULT_BONDHOLDERS_AVAILABLE,
      DEFAULT_BONDHOLDERS_AVAILABLE plusDays 1,
      _.copy(bondholdersAvailable = Some(DEFAULT_BONDHOLDERS_AVAILABLE plusDays 1)))
  }

  "Updating the priority point ticket sale date" should {
    testGameUpdateCommand[ZonedDateTime](
      gameLocator => (saleDate: ZonedDateTime) => PriorityPointTicketsUpdateCommand(gameLocator, saleDate),
      _.priorityPointAvailable,
      DEFAULT_PRIORITY_POINT_AVAILABLE,
      DEFAULT_PRIORITY_POINT_AVAILABLE plusDays 1,
      _.copy(priorityPointAvailable = Some(DEFAULT_PRIORITY_POINT_AVAILABLE plusDays 1)))
  }

  "Updating the season ticket holders' ticket sale date" should {
    testGameUpdateCommand[ZonedDateTime](
      gameLocator => (saleDate: ZonedDateTime) => SeasonTicketsUpdateCommand(gameLocator, saleDate),
      _.seasonTicketsAvailable,
      DEFAULT_SEASON_TICKETS_AVAILABLE,
      DEFAULT_SEASON_TICKETS_AVAILABLE plusDays 1,
      _.copy(seasonTicketsAvailable = Some(DEFAULT_SEASON_TICKETS_AVAILABLE plusDays 1)))
  }

  "Updating the academy members' ticket sale date" should {
    testGameUpdateCommand[ZonedDateTime](
      gameLocator => (saleDate: ZonedDateTime) => AcademyTicketsUpdateCommand(gameLocator, saleDate),
      _.academyMembersAvailable,
      DEFAULT_ACADEMY_TICKETS_AVAILABLE,
      DEFAULT_ACADEMY_TICKETS_AVAILABLE plusDays 1,
      _.copy(academyMembersAvailable = Some(DEFAULT_ACADEMY_TICKETS_AVAILABLE plusDays 1)))
  }

  "Updating the general ticket sale date" should {
    testGameUpdateCommand[ZonedDateTime](
      gameLocator => (saleDate: ZonedDateTime) => GeneralSaleTicketsUpdateCommand(gameLocator, saleDate),
      _.generalSaleAvailable,
      DEFAULT_GENERAL_SALE_TICKETS_AVAILABLE,
      DEFAULT_GENERAL_SALE_TICKETS_AVAILABLE plusDays 1,
      _.copy(generalSaleAvailable = Some(DEFAULT_GENERAL_SALE_TICKETS_AVAILABLE plusDays 1)))
  }

  def testGameUpdateCommand[E](
    gameUpdateCommandFactory: GameLocator => E => GameUpdateCommand,
    valueFactory: Game => Option[E],
    currentValue: E,
    newValue: E,
    expectedResult: Game => Game)(implicit ct: ClassTag[E]) = {
    val updateCommandFactory = (game: Game) => gameUpdateCommandFactory(GameKeyLocator(game.gameKey))
    "not change for equal values" in {
      val game = createFullyPopulatedGame
      val gameUpdateCommand = updateCommandFactory(game)(currentValue)
      gameUpdateCommand.update(game) must beNone
    }
    "change for different values" in {
      val game = createFullyPopulatedGame
      val gameUpdateCommand =
        updateCommandFactory(game)(newValue)
      gameUpdateCommand.update(game) must beSome(expectedResult(game))
    }
  }

  /**
   * Creates the fully populated game.
   *
   * @return the game
   */
  def createFullyPopulatedGame: Game = {
    return new Game(
      id = 1,
      location = DEFAULT_LOCATION,
      season = DEFAULT_SEASON,
      competition = DEFAULT_COMPETITION,
      opponents = DEFAULT_OPPONENTS,
      at = Some(DEFAULT_DATE_PLAYED.toZonedDateTime),
      attended = DEFAULT_ATTENDED,
      result = Some(DEFAULT_RESULT),
      attendance = Some(DEFAULT_ATTENDANCE),
      matchReport = Some(DEFAULT_MATCH_REPORT),
      televisionChannel = Some(DEFAULT_TELEVISION_CHANNEL),
      bondholdersAvailable = Some(DEFAULT_BONDHOLDERS_AVAILABLE.toZonedDateTime),
      priorityPointAvailable = Some(DEFAULT_PRIORITY_POINT_AVAILABLE.toZonedDateTime),
      seasonTicketsAvailable = Some(DEFAULT_SEASON_TICKETS_AVAILABLE.toZonedDateTime),
      academyMembersAvailable = Some(DEFAULT_ACADEMY_TICKETS_AVAILABLE.toZonedDateTime),
      generalSaleAvailable = Some(DEFAULT_GENERAL_SALE_TICKETS_AVAILABLE.toZonedDateTime),
      lastUpdated = DEFAULT_UPDATE_DATE,
      dateCreated = DEFAULT_UPDATE_DATE)
  }

}
