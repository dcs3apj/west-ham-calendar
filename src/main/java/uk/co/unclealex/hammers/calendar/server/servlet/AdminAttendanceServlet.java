/**
 * 
 */
package uk.co.unclealex.hammers.calendar.server.servlet;

import java.io.IOException;

import uk.co.unclealex.hammers.calendar.shared.exceptions.GoogleAuthenticationFailedException;
import uk.co.unclealex.hammers.calendar.shared.exceptions.GoogleException;
import uk.co.unclealex.hammers.calendar.shared.exceptions.NoSuchUsernameException;
import uk.co.unclealex.hammers.calendar.shared.exceptions.UsernameAlreadyExistsException;
import uk.co.unclealex.hammers.calendar.shared.model.CalendarColour;
import uk.co.unclealex.hammers.calendar.shared.model.CalendarConfiguration;
import uk.co.unclealex.hammers.calendar.shared.model.CalendarType;
import uk.co.unclealex.hammers.calendar.shared.model.Role;
import uk.co.unclealex.hammers.calendar.shared.model.User;
import uk.co.unclealex.hammers.calendar.shared.remote.AdminAttendanceService;

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
public class AdminAttendanceServlet extends AbstractAttendanceServlet implements AdminAttendanceService {

	@Override
	public void remove(CalendarType calendarType) throws GoogleAuthenticationFailedException, IOException, GoogleException {
		createAttendanceService().remove(calendarType);
	}

	@Override
	public void createOrUpdate(CalendarConfiguration calendarConfiguration) throws GoogleAuthenticationFailedException,
			IOException, GoogleException {
		createAttendanceService().createOrUpdate(calendarConfiguration);

	}

	@Override
	public CalendarConfiguration[] getAllCalendarConfigurations() {
		return createAttendanceService().getAllCalendarConfigurations();
	}

	@Override
	public void authenticate(String successToken) throws GoogleAuthenticationFailedException, IOException {
		createAttendanceService().authenticate(successToken);
	}

	@Override
	public String createGoogleAuthenticationUrlIfRequired() {
		return createAttendanceService().createGoogleAuthenticationUrlIfRequired();
	}
	
	@Override
	public CalendarColour[] getUsedCalendarColours() throws GoogleAuthenticationFailedException, IOException, GoogleException {
		return createAttendanceService().getUsedCalendarColours();
	}
	
	@Override
	public CalendarConfiguration createNewCalendarConfiguration(CalendarType calendarType)
			throws GoogleAuthenticationFailedException, IOException, GoogleException {
		return createAttendanceService().createNewCalendarConfiguration(calendarType);
	}
	
	@Override
	public CalendarConfiguration[] getCalendarConfigurations(boolean tickets) {
		return createAttendanceService().getCalendarConfigurations(tickets);
	}
	
	@Override
	public void updateCalendars() {
		createAttendanceService().updateCalendars();		
	}
	
	@Override
	public void addUser(String username, String password, Role role) throws UsernameAlreadyExistsException {
	  createAttendanceService().addUser(username, password, role);
	}
	
	@Override
	public void alterUser(String username, String newPassword, Role newRole) throws NoSuchUsernameException {
	  createAttendanceService().alterUser(username, newPassword, newRole);
	}
	
	@Override
	public void removeUser(String username) throws NoSuchUsernameException {
	  createAttendanceService().removeUser(username);
	}
	
	@Override
	public User[] getAllUsers() {
	  return createAttendanceService().getAllUsers();
	}
}
