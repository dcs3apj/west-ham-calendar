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
package uk.co.unclealex.hammers.calendar;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

@SuppressWarnings("deprecation")
public abstract class SpringTest extends AbstractDependencyInjectionSpringContextTests {

	public SpringTest() {
		setAutowireMode(AUTOWIRE_NO);
	}
	
	protected <E> E autowire(E object) {
		AutowireCapableBeanFactory autowireCapableBeanFactory = getApplicationContext().getAutowireCapableBeanFactory();
		autowireCapableBeanFactory.autowireBeanProperties(object, AUTOWIRE_BY_NAME, false);
		return object;
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath*:applicationContext-hammers.xml", "classpath*:applicationContext-hammers-test.xml" };
	}
	
	protected URL makeUrl(String path) throws MalformedURLException {
		return new URL(getTestResourceUrl(), path);
	}
	
	protected URL getTestResourceUrl() {
		return getClass().getClassLoader().getResource("test.html");
	}
}
