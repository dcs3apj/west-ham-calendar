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
package uk.co.unclealex.hammers.calendar.html;

public class SkySportsFixture {

	private String i_monthAndYear;
	private String i_date;
	private String i_time;
	private String i_channel;

	public SkySportsFixture(String monthAndYear) {
		super();
		i_monthAndYear = monthAndYear;
	}
	
	public boolean isComplete() {
		return getMonthAndYear() != null && getDate() != null && getTime() != null && getChannel() != null;
		
	}
	public String getMonthAndYear() {
		return i_monthAndYear;
	}
	
	public String getDate() {
		return i_date;
	}
	
	public void setDate(String date) {
		i_date = date;
	}
	
	public String getTime() {
		return i_time;
	}
	
	public void setTime(String time) {
		i_time = time;
	}
	
	public String getChannel() {
		return i_channel;
	}
	
	public void setChannel(String channel) {
		i_channel = channel;
	}
}