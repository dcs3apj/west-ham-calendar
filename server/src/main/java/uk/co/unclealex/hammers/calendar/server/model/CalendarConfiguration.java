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
package uk.co.unclealex.hammers.calendar.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import uk.co.unclealex.hammers.calendar.shared.model.CalendarType;


/**
 * A calendar configuration is used to link a calendar in the Google Calendar
 * API with a {@link CalendarType}.
 * 
 * @author alex
 * 
 */
@Entity
@Table(name = "calendar")
public class CalendarConfiguration extends AbstractBusinessKeyBasedModel<CalendarType, CalendarConfiguration> {

	/**
	 * The primary key of i_calendar configuration.
	 */
	private Integer i_id;
	
	/**
	 * The {@link CalendarType} that is to be bound to a Google calendar.
	 */
	private CalendarType i_calendarType;
	
	/**
	 * The id of the calendar in the Google Calendar API.
	 */
	private String i_googleCalendarId;

	/**
	 * Instantiates a new calendar configuration.
	 */
	protected CalendarConfiguration() {
		super();
	}

	/**
	 * Instantiates a new calendar configuration.
	 * 
	 * @param id
	 *          the id
	 * @param calendarType
	 *          the calendar type
	 * @param googleCalendarId
	 *          the google calendar id
	 */
	public CalendarConfiguration(Integer id, CalendarType calendarType, String googleCalendarId) {
		super();
		i_id = id;
		i_calendarType = calendarType;
		i_googleCalendarId = googleCalendarId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("[%d:%s:%s]", getId(), getCalendarType(), getGoogleCalendarId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public CalendarType getBusinessKey() {
		return getCalendarType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBusinessKey(CalendarType businessKey) {
		setCalendarType(businessKey);
	}

	/**
	 * Gets the {@link CalendarType} that is to be bound to a Google calendar.
	 * 
	 * @return the {@link CalendarType} that is to be bound to a Google calendar
	 */
	@Enumerated(EnumType.STRING)
	@Column(unique = true, nullable = false)
	public CalendarType getCalendarType() {
		return i_calendarType;
	}

	/**
	 * Sets the {@link CalendarType} that is to be bound to a Google calendar.
	 * 
	 * @param calendarType
	 *          the new {@link CalendarType} that is to be bound to a Google
	 *          calendar
	 */
	public void setCalendarType(CalendarType calendarType) {
		i_calendarType = calendarType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Id
	@GeneratedValue
	public Integer getId() {
		return i_id;
	}

	/**
	 * Sets the primary key of i_calendar configuration.
	 * 
	 * @param id
	 *          the new primary key of i_calendar configuration
	 */
	public void setId(Integer id) {
		i_id = id;
	}

	/**
	 * Gets the id of the calendar in the Google Calendar API.
	 * 
	 * @return the id of the calendar in the Google Calendar API
	 */
	@Column(nullable = false, unique = true)
	public String getGoogleCalendarId() {
		return i_googleCalendarId;
	}

	/**
	 * Sets the id of the calendar in the Google Calendar API.
	 * 
	 * @param googleCalendarId
	 *          the new id of the calendar in the Google Calendar API
	 */
	public void setGoogleCalendarId(String googleCalendarId) {
		i_googleCalendarId = googleCalendarId;
	}
}
