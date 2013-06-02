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

package uk.co.unclealex.hammers.calendar.dao;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.unclealex.hammers.calendar.dao.OauthTokenDao;
import uk.co.unclealex.hammers.calendar.model.OauthToken;
import uk.co.unclealex.hammers.calendar.model.OauthTokenType;


/**
 * The Class HibernateOauthTokenDaoTest.
 * 
 * @author alex
 */
public class HibernateOauthTokenDaoTest extends DaoTest {

	
	/**
	 * Instantiates a new hibernate oauth token dao test.
	 */
	public HibernateOauthTokenDaoTest() {
		super(OauthToken.class);
	}

	/** The oauth token dao. */
	@Autowired OauthTokenDao oauthTokenDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doSetup() throws Exception {
		// do nothing special.
	}

	/**
	 * Test find by key.
	 */
	@Test
	public void testFindByKey() {
		OauthToken accessOauthToken = new OauthToken(OauthTokenType.ACCESS, "1234");
		OauthToken refreshOauthToken = new OauthToken(OauthTokenType.REFRESH, "4321");
		oauthTokenDao.saveOrUpdate(accessOauthToken, refreshOauthToken);
		OauthToken actualOauthToken = oauthTokenDao.findByKey(OauthTokenType.REFRESH);
		Assert.assertNotNull("Could not find the refresh token.", actualOauthToken);
		Assert.assertEquals("The refresh token was incorrect.", refreshOauthToken, actualOauthToken);
	}

}
