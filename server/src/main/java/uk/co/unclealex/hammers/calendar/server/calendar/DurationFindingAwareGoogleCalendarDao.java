/**
 * Copyright 2010-2012 Alex Jones
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with i_work for additional information
 * regarding copyright ownership.  The ASF licenses i_file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use i_file except in compliance
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

package uk.co.unclealex.hammers.calendar.server.calendar;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;

import uk.co.unclealex.hammers.calendar.shared.exceptions.GoogleAuthenticationFailedException;


/**
 * A stub class for calendars that can also return the required duration needed
 * to find a calendar.
 * 
 * @author alex
 * 
 */
public abstract class DurationFindingAwareGoogleCalendarDao implements GoogleCalendarDao {

	/**
	 * This method is for testing so that the duration type required to find a
	 * game can be tested.
	 * 
	 * @param calendarId
	 *          The id of the calendar containing the game.
	 * @param gameId
	 *          The id of the game to search for.
	 * @param searchDate
	 *          The search date to try first.
	 * @return The found game id (if any) and the used duration (if any).
	 * @throws IOException
	 *           Signals that an I/O exception has occurred.
	 * @throws GoogleAuthenticationFailedException
	 *           Thrown if authentication with the Google servers fails.
	 */
	public abstract GameIdAndDurationFieldType findGameAndDurationFieldType(String calendarId, String gameId,
			DateTime searchDate) throws IOException, GoogleAuthenticationFailedException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String findGame(String calendarId, String gameId, DateTime searchDate) throws IOException,
			GoogleAuthenticationFailedException {
		GameIdAndDurationFieldType type = findGameAndDurationFieldType(calendarId, gameId, searchDate);
		return type == null ? null : type.getGameId();
	}

	/**
	 * Duration field types.
	 * 
	 * @return The array of {@link DurationFieldType}s to use when searching for a
	 *         game.
	 */
	protected DurationFieldType[] durationFieldTypes() {
		return new DurationFieldType[] { DurationFieldType.hours(), DurationFieldType.days(), DurationFieldType.weeks(),
				DurationFieldType.months(), DurationFieldType.years(), null };
	}

	/**
	 * A small class for returning a game id and a duration field type.
	 * 
	 * @author alex
	 * 
	 */
	class GameIdAndDurationFieldType {

		/**
		 * The game id.
		 */
		private final String i_gameId;
		
		/**
		 * The {@link DurationFieldType} that found the game.
		 */
		private final DurationFieldType i_durationFieldType;

		/**
		 * Instantiates a new game id and duration field type.
		 * 
		 * @param gameId
		 *          the game id
		 * @param durationFieldType
		 *          the duration field type
		 */
		public GameIdAndDurationFieldType(String gameId, DurationFieldType durationFieldType) {
			super();
			i_gameId = gameId;
			i_durationFieldType = durationFieldType;
		}

		/**
		 * Gets the game id.
		 * 
		 * @return the game id
		 */
		public String getGameId() {
			return i_gameId;
		}

		/**
		 * Gets the {@link DurationFieldType} that found the game.
		 * 
		 * @return the {@link DurationFieldType} that found the game
		 */
		public DurationFieldType getDurationFieldType() {
			return i_durationFieldType;
		}

	}

}
