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
import java.util.Map;
import java.util.SortedSet;

import uk.co.unclealex.hammers.calendar.server.calendar.google.GoogleCalendar;
import uk.co.unclealex.hammers.calendar.server.model.Game;
import uk.co.unclealex.hammers.calendar.shared.exceptions.GoogleAuthenticationFailedException;


/**
 * A service that updates a calendar so that it is consistent with the given
 * list of games.
 * 
 * @author alex
 * 
 */
public interface GoogleCalendarUpdatingService {

	/**
	 * Update a set of calendars.
	 * 
	 * @param googleCalendarsByCalendarId
	 *          the google calendars by calendar id
	 * @param games
	 *          The current list of known games.
	 * @return A report of all the changes that occured.
	 * @throws IOException
	 *           Signals that an I/O exception has occurred.
	 * @throws GoogleAuthenticationFailedException
	 *           Thrown if authentication with the Google servers fails.
	 *           {@link GoogleCalendar}s keyed by the calendar id.
	 */
	SortedSet<UpdateChangeLog> updateCalendars(Map<String, GoogleCalendar> googleCalendarsByCalendarId,
			Iterable<Game> games) throws IOException, GoogleAuthenticationFailedException;
}
