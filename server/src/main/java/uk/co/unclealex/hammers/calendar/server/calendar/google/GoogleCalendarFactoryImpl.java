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

package uk.co.unclealex.hammers.calendar.server.calendar.google;

import java.util.Map;

import uk.co.unclealex.hammers.calendar.shared.model.CalendarType;

/**
 * The default implementation of {@link GoogleCalendarFactory}.
 * @author alex
 *
 */
public class GoogleCalendarFactoryImpl implements GoogleCalendarFactory {

	/**
	 * The map of {@link GoogleCalendar}s by the {@link CalendarType} they represent.
	 */
	private Map<CalendarType, GoogleCalendar> i_googleCalendarsByCalendarType;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GoogleCalendar getGoogleCalendar(CalendarType calendarType) {
		return getGoogleCalendarsByCalendarType().get(calendarType);
	}
	
	public Map<CalendarType, GoogleCalendar> getGoogleCalendarsByCalendarType() {
		return i_googleCalendarsByCalendarType;
	}

	public void setGoogleCalendarsByCalendarType(Map<CalendarType, GoogleCalendar> googleCalendarsByCalendarType) {
		i_googleCalendarsByCalendarType = googleCalendarsByCalendarType;
	}

	
}
