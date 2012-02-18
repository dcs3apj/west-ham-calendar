/**
 * Copyright 2010 Alex Jones
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
package uk.co.unclealex.hammers.calendar.server.calendar.google;

import org.joda.time.Duration;
import org.joda.time.Interval;

import uk.co.unclealex.hammers.calendar.server.model.Game;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public abstract class AbstractGoogleCalendar implements GoogleCalendar {

	private String i_calendarTitle;
	private String i_description;
	private int i_durationInHours;
	private boolean i_busy;

	/**
	 * Default constructor
	 */
	protected AbstractGoogleCalendar() {
		super();
	}

	/**
	 * Constructor for testing
	 * 
	 * @param calendarTitle
	 *          The title of this calendar.
	 * @param description
	 *          The description of this calendar.
	 * @param durationInHours
	 *          The duration, in hours, of how long an event takes in this
	 *          calendar.
	 * @param busy
	 *          True if the time in this calendar should be marked as busy, false
	 *          otherwise.
	 */
	public AbstractGoogleCalendar(String calendarTitle, String description, int durationInHours, boolean busy) {
		super();
		i_calendarTitle = calendarTitle;
		i_description = description;
		i_durationInHours = durationInHours;
		i_busy = busy;
	}

	@Override
	public Function<Game, Interval> toCalendarDateInterval() {
		return new Function<Game, Interval>() {
			@Override
			public Interval apply(Game game) {
				return new Interval(getGameDate(game), Duration.standardHours(getDurationInHours()));
			}
		};
	}

	@Override
	public Predicate<Game> toContainsGamePredicate() {
		return new Predicate<Game>() {
			@Override
			public boolean apply(Game game) {
				return contains(game);
			}
		};
	}

	protected abstract boolean contains(Game game);

	public String getDescription() {
		return i_description;
	}

	public void setDescription(String description) {
		i_description = description;
	}

	public int getDurationInHours() {
		return i_durationInHours;
	}

	public void setDurationInHours(int durationInHours) {
		i_durationInHours = durationInHours;
	}

	public String getCalendarTitle() {
		return i_calendarTitle;
	}

	public void setCalendarTitle(String calendarTitle) {
		i_calendarTitle = calendarTitle;
	}

	public boolean isBusy() {
		return i_busy;
	}

	public void setBusy(boolean busy) {
		i_busy = busy;
	}

}
