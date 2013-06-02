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

package uk.co.unclealex.hammers.calendar.calendar.google;

import java.util.Map;

import uk.co.unclealex.hammers.calendar.model.CalendarType;


/**
 * The default implementation of {@link GoogleCalendarFactory}.
 * @author alex
 *
 */
public class GoogleCalendarFactoryImpl implements GoogleCalendarFactory {

	/**
	 * The map of {@link GoogleCalendar}s by the {@link CalendarType} they represent.
	 */
	private Map<CalendarType, GoogleCalendar> googleCalendarsByCalendarType;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GoogleCalendar getGoogleCalendar(CalendarType calendarType) {
		return getGoogleCalendarsByCalendarType().get(calendarType);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<CalendarType, GoogleCalendar> getGoogleCalendarsByCalendarType() {
		return googleCalendarsByCalendarType;
	}

	/**
	 * Sets the map of {@link GoogleCalendar}s by the {@link CalendarType} they
	 * represent.
	 * 
	 * @param googleCalendarsByCalendarType
	 *          the new map of {@link GoogleCalendar}s by the {@link CalendarType}
	 *          they represent
	 */
	public void setGoogleCalendarsByCalendarType(Map<CalendarType, GoogleCalendar> googleCalendarsByCalendarType) {
		this.googleCalendarsByCalendarType = googleCalendarsByCalendarType;
	}

	
}
