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
package uk.co.unclealex.hammers.calendar.model

import org.joda.time.DateTime

/**
 * A persistable unit that represents an advertised West Ham game.
 */
case class Game(
  /**
   * The primary key of the game.
   */
  val id: Int,
  /**
   * The game's {@link Competition}.
   */
  var competition: Competition,
  /**
   * The game's {@link Location}.
   */
  var location: Location,
  /**
   * The game's opponents.
   */
  var opponents: String,
  /**
   * The season the game was played in.
   */
  var season: Int,
  /**
   * The {@link DateTime} the game was played.
   */
  var dateTimePlayed: Option[DateTime],
  /**
   * The {@link DateTime} that Bondholder tickets went on sale.
   */
  var dateTimeBondholdersAvailable: Option[DateTime],
  /**
   * The {@link DateTime} that priority point tickets went on sale.
   */
  var dateTimePriorityPointPostAvailable: Option[DateTime],
  /**
   * The {@link DateTime} that season ticker holder tickets went on sale.
   */
  var dateTimeSeasonTicketsAvailable: Option[DateTime],
  /**
   * The {@link DateTime} that Academy members' tickets went on sale.
   */
  var dateTimeAcademyMembersAvailable: Option[DateTime],
  /**
   * The {@link DateTime} that tickets went on general sale.
   */
  var dateTimeGeneralSaleAvailable: Option[DateTime],
  /**
   * The game's result.
   */
  var result: Option[String],
  /**
   * The game's attendence.
   */
  var attendence: Option[Int],
  /**
   * The game's match report.
   */
  var matchReport: Option[String],
  /**
   * The TV channel that showed the match.
   */
  var televisionChannel: Option[String],

  /**
   * True if the game has been marked as attended, false otherwise.
   */
  var attended: Option[Boolean]) {

  /**
   * Squeryl constructor
   */

  protected def this() =
    this(
      0, Competition.PREM, Location.HOME, "", 0,
      Some(new DateTime()), Some(new DateTime()), Some(new DateTime()),
      Some(new DateTime()), Some(new DateTime()), Some(new DateTime()),
      Some("result"), Some(0), Some("matchReport"), Some("televionChannel"), Some(false))
  /**
   * Create a new game from a business key.
   */
  def this(gameKey: GameKey) =
    this(
      0, gameKey.competition, gameKey.location, gameKey.opponents, gameKey.season,
      None, None, None, None, None, None, None, None, None, None, Some(false))

  /**
   * Get the unique business key for this game.
   */
  def gameKey: GameKey = GameKey(competition, location, opponents, season)
}

object Game {

  /**
   * Create a new game
   */
  def apply(gameKey: GameKey) = new Game(gameKey)
}