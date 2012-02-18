/**
 * Copyright 2011 Alex Jones
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

package uk.co.unclealex.hammers.calendar.server.calendar;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import uk.co.unclealex.hammers.calendar.server.calendar.DurationFindingAwareGoogleCalendarDao.StringAndDurationFieldType;
import uk.co.unclealex.hammers.calendar.shared.exceptions.GoogleAuthenticationFailedException;
import uk.co.unclealex.hammers.calendar.shared.model.Competition;
import uk.co.unclealex.hammers.calendar.shared.model.Location;

import com.google.api.services.calendar.model.EventDateTime;
import com.google.common.collect.Maps;

/**
 * @author alex
 * 
 */
public abstract class AbstractGoogleCalendarDaoTest {

	protected DateTime dateOf(int day, int month, int year, int hour, int minute) {
		return new DateTime(year, month, day, hour, minute, 0, 0, DateTimeZone.forID("Europe/London"));
	}

	/**
	 * Test method for
	 * {@link uk.co.unclealex.hammers.calendar.server.calendar.GoogleCalendarDaoImpl#createOrUpdateGame(java.lang.String, java.lang.String, java.lang.String, java.lang.String, uk.co.unclealex.hammers.calendar.shared.model.Competition, uk.co.unclealex.hammers.calendar.shared.model.Location, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, boolean)}
	 * .
	 * 
	 * @throws ParseException
	 * @throws GoogleAuthenticationFailedException
	 * @throws IOException
	 */
	@Test
	public void testCreateOrUpdateGameCreate() throws IOException, GoogleAuthenticationFailedException, ParseException {
		String calendarId = getPrimaryCalendarId();
		String eventId = definitelyCreateGame(calendarId, "Simple Calendar", "1", Competition.FACP, Location.HOME,
				"Opponents", new Interval(dateOf(5, 9, 1972, 9, 12), dateOf(5, 9, 1972, 11, 12)), "1-0", 200000, "Report",
				"Sky", true);
		checkGame(calendarId, eventId, "1", Competition.FACP, Location.HOME, "Opponents", dateOf(5, 9, 1972, 9, 12),
				dateOf(5, 9, 1972, 11, 12), "1-0", 200000, "Report", "Sky", true);
	}

	/**
	 * Create or update a game.
	 * 
	 * @param calendarId
	 *          The id of the calendar for the game.
	 * @param calendarTitle
	 *          The title of the calendar for the game.
	 * @param gameId
	 *          The id of the game.
	 * @param competition
	 *          The competition the game was played in.
	 * @param location
	 *          The game's location.
	 * @param opponents
	 *          The opponents for this game.
	 * @param gameInterval
	 *          The start and end time of this game with respect to the calendar
	 *          type.
	 * @param result
	 *          The game's result.
	 * @param attendence
	 *          The game's attendance.
	 * @param matchReport
	 *          The URL for the match report.
	 * @param televisionChannel
	 *          The TV channel showing the match.
	 * @param busy
	 *          True if the time should be marked as busy, false otherwise.
	 * @return The id of the created event.
	 * @throws IOException
	 *           Thrown if there is a problem contacting Google.
	 * @throws GoogleAuthenticationFailedException
	 *           Thrown if the application cannot authenticate itself.
	 */
	protected String definitelyCreateGame(String calendarId, String calendarTitle, String gameId,
			Competition competition, Location location, String opponents, Interval gameInterval, String result,
			Integer attendence, String matchReport, String televisionChannel, boolean busy) throws IOException,
			GoogleAuthenticationFailedException {
		GameUpdateInformation gameUpdateInformation = getGoogleCalendarDao().createOrUpdateGame(calendarId, null, gameId,
				competition, location, opponents, gameInterval, result, attendence, matchReport, televisionChannel, busy);
		class EventIdInformationVisitor extends GameUpdateInformationVisitor.Default {
			String eventId;

			@Override
			public void visit(GameWasCreatedInformation gameWasCreatedInformation) {
				eventId = gameWasCreatedInformation.getEventId();
			}

			@Override
			public void visit(GameWasUpdatedInformation gameWasUpdatedInformation) {
				Assert.fail("A game was updated when it was expected to be created.");
			}
		}
		;
		EventIdInformationVisitor visitor = new EventIdInformationVisitor();
		gameUpdateInformation.accept(visitor);
		return visitor.eventId;
	}

	/**
	 * Test method for
	 * {@link uk.co.unclealex.hammers.calendar.server.calendar.GoogleCalendarDaoImpl#createOrUpdateGame(java.lang.String, java.lang.String, java.lang.String, java.lang.String, uk.co.unclealex.hammers.calendar.shared.model.Competition, uk.co.unclealex.hammers.calendar.shared.model.Location, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, boolean)}
	 * .
	 * 
	 * @throws IOException
	 * @throws GoogleAuthenticationFailedException
	 */
	@Test
	public void testCreateOrUpdateGameUpdate() throws IOException, GoogleAuthenticationFailedException {
		final String calendarId = getPrimaryCalendarId();
		class GameUpdateChecker {
			String calendarTitle = "Simple Calendar";
			String gameId = "1";
			String eventId;
			Competition competition = Competition.FACP;
			Location location = Location.HOME;
			String opponents = "Someone";
			DateTime dateStarted = dateOf(5, 9, 1972, 9, 12);
			DateTime dateFinished = dateOf(5, 9, 1972, 11, 12);
			String result = "1-0";
			Integer attendence = 200000;
			String matchReport = "Report";
			String televisionChannel = "Sky";
			boolean busy = true;

			void update(final boolean expectChange) throws IOException, GoogleAuthenticationFailedException {
				GameUpdateInformation gameUpdateInformation = getGoogleCalendarDao().createOrUpdateGame(calendarId, eventId,
						gameId, competition, location, opponents, new Interval(dateStarted, dateFinished), result, attendence,
						matchReport, televisionChannel, busy);
				GameUpdateInformationVisitor visitor = new GameUpdateInformationVisitor.Default() {
					@Override
					public void visit(GameWasCreatedInformation gameWasCreatedInformation) {
						Assert.fail("A game was created when it was expected to be updated.");
					}

					@Override
					public void visit(GameWasUpdatedInformation gameWasUpdatedInformation) {
						Assert.assertEquals("The update status of a game was incorrect.", expectChange,
								gameWasUpdatedInformation.isUpdated());
					}
				};
				gameUpdateInformation.accept(visitor);
				checkGame(calendarId, eventId, gameId, competition, location, opponents, dateStarted, dateFinished, result,
						attendence, matchReport, televisionChannel, busy);
			}
		}
		GameUpdateChecker checker = new GameUpdateChecker();
		final String eventId = definitelyCreateGame(calendarId, checker.calendarTitle, checker.gameId, checker.competition,
				checker.location, checker.opponents, new Interval(checker.dateStarted, checker.dateFinished), checker.result,
				checker.attendence, checker.matchReport, checker.televisionChannel, checker.busy);
		checker.eventId = eventId;
		checker.update(false);
		checker.dateStarted = dateOf(5, 4, 1972, 9, 12);
		checker.update(true);
		checker.dateFinished = dateOf(5, 4, 1972, 11, 12);
		checker.update(true);
		checker.result = "2-0";
		checker.update(true);
		checker.attendence = 250000;
		checker.update(true);
		checker.matchReport = "Reporting";
		checker.update(true);
		checker.televisionChannel = "BBC";
		checker.update(true);
		checker.busy = false;
		checker.update(true);
	}

	protected abstract void checkGame(String calendarId, String eventId, String gameId, Competition competition,
			Location location, String opponents, DateTime dateStarted, DateTime dateFinished, String result,
			Integer attendence, String matchReport, String televisionChannel, boolean busy) throws IOException;

	protected void checkDate(String errorMessage, DateTime expected, EventDateTime actual) {
		long googleInstant = actual.getDateTime().getValue() / 1000;
		long expectedInstant = expected.getMillis() / 1000;
		Assert.assertEquals(errorMessage, expectedInstant, googleInstant);
	}

	/**
	 * Test method for
	 * {@link uk.co.unclealex.hammers.calendar.server.calendar.GoogleCalendarDaoImpl#findGame(java.lang.String, java.lang.String, java.util.Date)}
	 * .
	 * 
	 * @throws GoogleAuthenticationFailedException
	 * @throws IOException
	 */
	@Test
	public void testFindGame() throws IOException, GoogleAuthenticationFailedException {
		String calendarId = getPrimaryCalendarId();
		String eventId = definitelyCreateGame(calendarId, "Simple Calendar", "1", Competition.FACP, Location.HOME,
				"Opponents", new Interval(dateOf(5, 9, 1972, 9, 12), dateOf(5, 9, 1972, 11, 12)), "1-0", 200000, "Report",
				"Sky", true);
		Map<DateTime, DurationFieldType> tests = Maps.newLinkedHashMap();
		tests.put(dateOf(5, 9, 1972, 9, 12), DurationFieldType.hours());
		tests.put(dateOf(5, 9, 1972, 15, 12), DurationFieldType.days());
		tests.put(dateOf(11, 9, 1972, 15, 12), DurationFieldType.weeks());
		tests.put(dateOf(11, 8, 1972, 15, 12), DurationFieldType.months());
		tests.put(dateOf(11, 8, 1973, 15, 12), DurationFieldType.years());
		tests.put(dateOf(11, 8, 1980, 15, 12), null);
		for (Entry<DateTime, DurationFieldType> entry : tests.entrySet()) {
			DateTime dateTime = entry.getKey();
			StringAndDurationFieldType stringAndDurationFieldType = getGoogleCalendarDao().findGameAndDurationFieldType(
					calendarId, "1", dateTime);
			Assert.assertNotNull("The game could not be found.", stringAndDurationFieldType.string);
			Assert.assertEquals("The wrong game was found.", eventId, stringAndDurationFieldType.string);
			DurationFieldType durationFieldType = entry.getValue();
			if (durationFieldType == null) {
				Assert.assertNull("The wrong duration was successful.", stringAndDurationFieldType.durationFieldType);
			}
			else {
				Assert.assertEquals("The wrong duration was successful.", durationFieldType,
						stringAndDurationFieldType.durationFieldType);
			}
		}
		StringAndDurationFieldType stringAndDurationFieldType = getGoogleCalendarDao().findGameAndDurationFieldType(
				calendarId, "2", dateOf(11, 8, 1973, 15, 12));
		Assert.assertNull("A game was found when it should not have been.", stringAndDurationFieldType.string);
		Assert.assertNull("A game was found when it should not have been.", stringAndDurationFieldType.durationFieldType);
	}

	@Test
	public void testRemoveGame() throws IOException, GoogleAuthenticationFailedException {
		String calendarId = getPrimaryCalendarId();
		final String eventId = definitelyCreateGame(calendarId, "My Calendar Title", "24", Competition.FACP, Location.HOME,
				"opponents", new Interval(dateOf(5, 9, 1972, 0, 0), Duration.standardHours(1)), "1-0", 100000, "Good", "Sky",
				true);
		getGoogleCalendarDao().removeGame(calendarId, eventId, "24");
		checkGameRemoved(calendarId, eventId);
	}

	/**
	 * Check that a game has been removed from a calendar.
	 * 
	 * @param calendarId
	 *          The id of the calendar to check.
	 * @param eventId
	 *          The event id that should not exist.
	 * @throws IOException
	 */
	protected abstract void checkGameRemoved(String calendarId, final String eventId) throws IOException;

	@Test
	public void testMoveGame() throws IOException, GoogleAuthenticationFailedException {
		String primaryCalendarId = getPrimaryCalendarId();
		String secondaryCalendarId = getSecondaryCalendarId();
		String eventId = definitelyCreateGame(primaryCalendarId, "Primary", "150", Competition.FACP, Location.HOME,
				"Vagabonds", new Interval(dateOf(5, 9, 1972, 9, 0), Duration.standardHours(2)), "1-0", 100000, "Nice", "BBC",
				true);
		getGoogleCalendarDao().moveGame(primaryCalendarId, secondaryCalendarId, eventId, "150", false);
		// Check we cannot find the game in the original calendar
		Assert.assertNull("The moved game was incorrectly found in the source calendar.",
				getGoogleCalendarDao().findGame(primaryCalendarId, "150", dateOf(5, 9, 1972, 9, 0)));
		checkGame(secondaryCalendarId, eventId, "150", Competition.FACP, Location.HOME, "Vagabonds",
				dateOf(5, 9, 1972, 9, 0), dateOf(5, 9, 1972, 11, 0), "1-0", 100000, "Nice", "BBC", false);
	}

	/**
	 * Test method for
	 * {@link uk.co.unclealex.hammers.calendar.server.calendar.GoogleCalendarDaoImpl#createOrUpdateCalendar(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}
	 * .
	 * 
	 * @throws IOException
	 * @throws GoogleAuthenticationFailedException
	 */
	@Test
	public void testCreateOrUpdateCalendarCreate() throws IOException, GoogleAuthenticationFailedException {
		String calendarId = getGoogleCalendarDao().createOrUpdateCalendar(null, "My Calendar", "Really My Calendar");
		checkCalendar(calendarId, "My Calendar", "Really My Calendar");
	}

	/**
	 * Test method for
	 * {@link uk.co.unclealex.hammers.calendar.server.calendar.GoogleCalendarDaoImpl#createOrUpdateCalendar(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}
	 * .
	 * 
	 * @throws IOException
	 * @throws GoogleAuthenticationFailedException
	 */
	@Test
	public void testCreateOrUpdateCalendarUpdate() throws IOException, GoogleAuthenticationFailedException {
		String firstCalendarId = getGoogleCalendarDao().createOrUpdateCalendar(null, "A Calendar", "A Calendar");
		String secondCalendarId = getGoogleCalendarDao().createOrUpdateCalendar(firstCalendarId, "My Other Calendar",
				"Another Calendar");
		Assert.assertEquals("An updated calendar had a different id.", firstCalendarId, secondCalendarId);
		checkCalendar(secondCalendarId, "My Other Calendar", "Another Calendar");
	}

	/**
	 * @param secondCalendarId
	 * @param string
	 * @param string2
	 * @param b
	 * @param c
	 * @throws IOException
	 */
	protected abstract void checkCalendar(String calendarId, String title, String description) throws IOException;

	protected boolean toDefault(Boolean value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		else {
			return value.booleanValue();
		}
	}

	/**
	 * Test method for
	 * {@link uk.co.unclealex.hammers.calendar.server.calendar.GoogleCalendarDaoImpl#listGameIdsByEventId(java.lang.String)}
	 * .
	 * 
	 * @throws GoogleAuthenticationFailedException
	 * @throws IOException
	 */
	@Test
	public void testListGameIdsByEventId() throws IOException, GoogleAuthenticationFailedException {
		String calendarId = getPrimaryCalendarId();
		String firstEventId = definitelyCreateGame(calendarId, "Calendar", "100", Competition.FACP, Location.HOME,
				"Opponents", new Interval(dateOf(5, 9, 1972, 9, 12), dateOf(5, 9, 1972, 11, 12)), "1-0", 10, null, null, true);
		String secondEventId = definitelyCreateGame(calendarId, "Calendar", "200", Competition.FACP, Location.HOME,
				"Opponents", new Interval(dateOf(4, 9, 1972, 9, 12), dateOf(4, 9, 1972, 11, 12)), "1-0", 10, null, null, true);
		Map<String, String> actualGameIdsByEventId = Maps.newTreeMap();
		for (Entry<String, String> entry : getGoogleCalendarDao().listGameIdsByEventId(calendarId).entrySet()) {
			actualGameIdsByEventId.put(entry.getKey(), entry.getValue());
		}
		Map<String, String> expectedGameIdsByEventId = Maps.newTreeMap();
		expectedGameIdsByEventId.put(firstEventId, "100");
		expectedGameIdsByEventId.put(secondEventId, "200");
		Assert
				.assertEquals("The wrong game event mappings were returned", expectedGameIdsByEventId, actualGameIdsByEventId);
	}

	protected abstract String getPrimaryCalendarId();

	protected abstract String getSecondaryCalendarId();

	protected abstract DurationFindingAwareGoogleCalendarDao getGoogleCalendarDao();
}
