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

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;


/**
 * An {@link AbstractOauthCalendarFactory} that uses a known constant refresh token.
 * @author alex
 *
 */
public class ConstantOauthCalendarFactory extends AbstractOauthCalendarFactory {

	/**
	 * The access token given by Google.
	 */
	private String i_accessToken;

	
	/**
	 * Default constructor.
	 */
	public ConstantOauthCalendarFactory() {
		super();
		setHttpTransport(new NetHttpTransport());
		setJsonFactory(new JacksonFactory());
	}

	/**
	 * The constant refresh token to use.
	 * @return A constant refresh token that is known to be authenticated.
	 */
	public String getRefreshToken() {
		return "1/UcuXuqCOI1eV_1S8XlvVMsO2x6QOIqtMeZ-Qe7KeYXE";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void installTokens(String accessToken, String refreshToken) {
		throw new UnsupportedOperationException("installTokens");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getAccessToken() {
		return i_accessToken;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAccessToken(String accessToken) {
		i_accessToken = accessToken;
	}

	
}
