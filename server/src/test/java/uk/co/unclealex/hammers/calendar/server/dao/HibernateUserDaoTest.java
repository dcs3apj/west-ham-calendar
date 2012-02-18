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

package uk.co.unclealex.hammers.calendar.server.dao;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.unclealex.hammers.calendar.server.model.Authority;
import uk.co.unclealex.hammers.calendar.server.model.User;
import uk.co.unclealex.hammers.calendar.shared.model.Role;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author alex
 *
 */
public class HibernateUserDaoTest extends DaoTest {

	private Map<Role, Authority> authorities;
	
	@Autowired UserDao userDao;
	
	public HibernateUserDaoTest() {
		super(User.class);
	}

	@Override
	protected void doSetup() throws Exception {
		authorities = Maps.newHashMap();
		for (Role role : Role.values()) {
			Authority authority = new Authority();
			authority.setRole(role);
			authorities.put(role, authority);
		}
		User admin = createUser("admin", Role.ROLE_ADMIN, Role.ROLE_USER);
		userDao.saveOrUpdate(admin);
		User user = createUser("user", Role.ROLE_USER);
		userDao.saveOrUpdate(user);
	}

	protected User createUser(String username, Role... roles) {
		User user = new User();
		user.setUsername(username);
		user.setPassword("password");
		user.setEnabled(true);
		user.setAuthorities(Sets.newHashSet(Iterables.transform(Arrays.asList(roles), Functions.forMap(authorities))));
		return user;
	}
	
	@Test
	public void testCountUsers() {
		Assert.assertEquals("The wrong number of users were returned.", 2, userDao.countUsers());
	}

	@Test
	public void testFindByKey() {
		User user = userDao.findByKey("user");
		Assert.assertEquals("The user had the wrong username.", "user", user.getUsername());
		Assert.assertEquals("The user had the wrong password.", "password", user.getPassword());
		Assert.assertArrayEquals("The user had the wrong roles.", new Role[] { Role.ROLE_USER }, rolesForUser(user));
	}

	@Test
	public void testChangeAuthorities() {
		User user = userDao.findByKey("user");
		user.setAuthorities(Sets.newHashSet(Iterables.transform(Arrays.asList(Role.values()), Functions.forMap(authorities))));
		userDao.saveOrUpdate(user);
		User actualUser = userDao.findByKey("user");
		Role[] actualRoles = rolesForUser(actualUser);
		Assert.assertArrayEquals("User had the wrong roles.", Role.values(), actualRoles);
	}

	protected Role[] rolesForUser(User actualUser) {
		Function<Authority, Role> function = new Function<Authority, Role>() {
			@Override
			public Role apply(Authority authority) {
				return authority.getRole();
			}
		};
		Role[] actualRoles = Iterables.toArray(Iterables.transform(actualUser.getAuthorities(), function), Role.class);
		Arrays.sort(actualRoles);
		return actualRoles;
	}
	
}
