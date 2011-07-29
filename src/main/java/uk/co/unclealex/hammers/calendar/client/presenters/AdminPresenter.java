/**
 * 
 */
package uk.co.unclealex.hammers.calendar.client.presenters;

import javax.inject.Inject;

import uk.co.unclealex.hammers.calendar.client.factories.AsyncCallbackExecutor;
import uk.co.unclealex.hammers.calendar.client.factories.GoogleAuthenticationPresenterFactory;
import uk.co.unclealex.hammers.calendar.client.util.ExecutableAsyncCallback;
import uk.co.unclealex.hammers.calendar.client.util.FailureAsPopupExecutableAsyncCallback;
import uk.co.unclealex.hammers.calendar.shared.remote.AdminAttendanceServiceAsync;
import uk.co.unclealex.hammers.calendar.shared.remote.AnonymousAttendanceServiceAsync;
import uk.co.unclealex.hammers.calendar.shared.remote.UserAttendanceServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

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
public class AdminPresenter extends RequiresPrerequisiteRemoteActionPresenter {

	public static interface Display extends IsWidget {

		HasWidgets getGameCalendarsPanel();
		HasWidgets getTicketCalendarsPanel();
		HasClickHandlers getRunJobButton();
		HasWidgets getUpdateUsersPanel();
	}
	
	private final GoogleAuthenticationPresenterFactory i_googleAuthenticationPresenterFactory;
	private final Display i_display;
	private final GameCalendarsPresenter i_gameCalendarsPresenter;
	private final TicketCalendarsPresenter i_ticketCalendarsPresenter;
	private final UpdateUsersPresenter i_updateUsersPresenter;

	@Inject
	public AdminPresenter(AsyncCallbackExecutor asyncCallbackExecutor,
			GoogleAuthenticationPresenterFactory googleAuthenticationPresenterFactory, Display display,
			GameCalendarsPresenter gameCalendarsPresenter, TicketCalendarsPresenter ticketCalendarsPresenter,
			UpdateUsersPresenter updateUsersPresenter) {
		super(asyncCallbackExecutor);
		i_googleAuthenticationPresenterFactory = googleAuthenticationPresenterFactory;
		i_display = display;
		i_gameCalendarsPresenter = gameCalendarsPresenter;
		i_ticketCalendarsPresenter = ticketCalendarsPresenter;
		i_updateUsersPresenter = updateUsersPresenter;
	}

	@Override
	protected void afterActionPerformed(AcceptsOneWidget panel, EventBus eventBus) {
		ExecutableAsyncCallback<String> authenticateCallback = new FailureAsPopupExecutableAsyncCallback<String>() {
			@Override
			public void onSuccess(String authenticationUrl) {
				if (authenticationUrl != null) {
					getGoogleAuthenticationPresenterFactory().createGoogleAuthenticationPresenter(authenticationUrl).center();
				}
			}
			@Override
			public void execute(AnonymousAttendanceServiceAsync anonymousAttendanceService,
					UserAttendanceServiceAsync userAttendanceService,
					AdminAttendanceServiceAsync adminAttendanceService, AsyncCallback<String> callback) {
				adminAttendanceService.createGoogleAuthenticationUrlIfRequired(callback);
			}
		};
		AsyncCallbackExecutor asyncCallbackExecutor = getAsyncCallbackExecutor();
		asyncCallbackExecutor.execute(authenticateCallback);
		Display display = getDisplay();
		getGameCalendarsPresenter().show(display.getGameCalendarsPanel());
		getTicketCalendarsPresenter().show(display.getTicketCalendarsPanel());
		getUpdateUsersPresenter().show(display.getUpdateUsersPanel());
		display.getRunJobButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ExecutableAsyncCallback<Void> callback = new FailureAsPopupExecutableAsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						// Do nothing
					}
					@Override
					public void execute(AnonymousAttendanceServiceAsync anonymousAttendanceService,
							UserAttendanceServiceAsync userAttendanceService, AdminAttendanceServiceAsync adminAttendanceService,
							AsyncCallback<Void> callback) {
						adminAttendanceService.updateCalendars(callback);
					}
				};
				getAsyncCallbackExecutor().execute(callback);
			}
		});
		panel.setWidget(display);
	}

	@Override
	protected void performPrerequisiteAction(AnonymousAttendanceServiceAsync anonymousAttendanceService,
			UserAttendanceServiceAsync userAttendanceService, AdminAttendanceServiceAsync adminAttendanceService,
			AsyncCallback<Void> callback) {
		userAttendanceService.forceLogin(callback);
	}
	
	public Display getDisplay() {
		return i_display;
	}

	public GoogleAuthenticationPresenterFactory getGoogleAuthenticationPresenterFactory() {
		return i_googleAuthenticationPresenterFactory;
	}

	public GameCalendarsPresenter getGameCalendarsPresenter() {
		return i_gameCalendarsPresenter;
	}

	public TicketCalendarsPresenter getTicketCalendarsPresenter() {
		return i_ticketCalendarsPresenter;
	}

  public UpdateUsersPresenter getUpdateUsersPresenter() {
    return i_updateUsersPresenter;
  }

}
