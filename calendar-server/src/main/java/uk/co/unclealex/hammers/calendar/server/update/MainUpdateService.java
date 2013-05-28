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

package uk.co.unclealex.hammers.calendar.server.update;

import java.io.IOException;
import java.util.SortedSet;

import uk.co.unclealex.hammers.calendar.server.calendar.UpdateChangeLog;
import uk.co.unclealex.hammers.calendar.shared.exceptions.GoogleAuthenticationFailedException;

/**
 * The service used to combine reading game information from the web and
 * transforming that information into google calendars.
 * 
 * @author alex
 * 
 */
public interface MainUpdateService {

  /**
   * Move a game to the attendend calendar.
   * 
   * @param gameId
   *          The the id of the game to move.
   * @throws GoogleAuthenticationFailedException
   *           Thrown if authentication with the Google servers fails.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  void attendGame(int gameId) throws GoogleAuthenticationFailedException, IOException;

  /**
   * Move a game to the unattendend calendar.
   * 
   * @param gameId
   *          The id of the game to move.
   * @throws GoogleAuthenticationFailedException
   *           Thrown if authentication with the Google servers fails.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  void unattendGame(int gameId) throws GoogleAuthenticationFailedException, IOException;

  /**
   * Update all calendars.
   * 
   * @return The set of changes required.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws GoogleAuthenticationFailedException
   *           Thrown if authentication with the Google servers fails.
   */
  SortedSet<UpdateChangeLog> updateAllCalendars() throws IOException, GoogleAuthenticationFailedException;

  /**
   * Update all calendars, taking only games from the latest season into
   * consideration.
   * 
   * @return The set of changes required.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws GoogleAuthenticationFailedException
   *           Thrown if authentication with the Google servers fails.
   */
  SortedSet<UpdateChangeLog> updateAllCalendarsThisSeason() throws IOException, GoogleAuthenticationFailedException;

  /**
   * Attend all home games in a season.
   * 
   * @param season
   *          the season
   * @throws GoogleAuthenticationFailedException
   *           Thrown if authentication with the Google servers fails.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  void attendAllHomeGamesForSeason(int season) throws GoogleAuthenticationFailedException, IOException;
}
