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
package uk.co.unclealex.hammers.calendar.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;


/**
 * Small class to adapt Joda time to JAXB.
 * 
 * @author alex
 * 
 */
public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTime unmarshal(String v) {
		return new DateTime(v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(DateTime v) {
		return v.toString();
	}

}