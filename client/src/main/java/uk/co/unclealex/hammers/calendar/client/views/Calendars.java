/**
 * 
 */
package uk.co.unclealex.hammers.calendar.client.views;

import javax.inject.Inject;

import uk.co.unclealex.hammers.calendar.client.presenters.CalendarsPresenter.Display;
import uk.co.unclealex.hammers.calendar.client.ui.UnorderedListPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

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
public class Calendars extends Composite implements Display {

	@UiTemplate("Calendars.ui.xml")
	public interface Binder extends UiBinder<Widget, Calendars> {
    // No extra method
  }
	
	private static final Binder binder = GWT.create(Binder.class);

	@UiField UnorderedListPanel calendarListPanel;
	@UiField ListBox otherCalendarNames;
	@UiField Button addNewCalendar;

	@Inject
	public Calendars() {
		initWidget(binder.createAndBindUi(this));
	}

	public UnorderedListPanel getCalendarListPanel() {
		return calendarListPanel;
	}

	public ListBox getOtherCalendarNames() {
		return otherCalendarNames;
	}

	public Button getAddNewCalendar() {
		return addNewCalendar;
	}

}