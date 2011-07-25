/**
 * 
 */
package uk.co.unclealex.hammers.calendar.server.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import uk.co.unclealex.hammers.calendar.server.dao.HibernateUserDao;
import uk.co.unclealex.hammers.calendar.server.dao.UserDao;
import uk.co.unclealex.hammers.calendar.server.exception.UsernameAlreadyExistsException;
import uk.co.unclealex.hammers.calendar.server.model.Authority;
import uk.co.unclealex.hammers.calendar.server.model.Role;
import uk.co.unclealex.hammers.calendar.server.model.User;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

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
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(HibernateUserDao.class);
	
	private PasswordEncoder i_passwordEncoder;
	private SaltSource i_saltSource;
	private UserDao i_userDao;
	
	private SortedSet<Role> i_allRoles;
	private Role i_smallestRole;

	@PostConstruct
	public void initialise() {
		TreeSet<Role> allRoles = new TreeSet<Role>(Arrays.asList(Role.values()));
		setAllRoles(allRoles);
		setSmallestRole(Iterables.get(allRoles, 0));
	}

	@Override
	public void ensureDefaultUsersExists(String defaultUsername, String defaultPassword) {
		UserDao userDao = getUserDao();
		if (userDao.countUsers() == 0) {
			try {
				addUser(defaultUsername, defaultPassword, Role.ROLE_ADMIN);
			}
			catch (UsernameAlreadyExistsException e) {
				log.warn("Cannot create the default user.", e);
			}
		}
	}

	@Override
	public void addUser(String username, String password, Role role) throws UsernameAlreadyExistsException {
		if (getUserDao().findByKey(username) != null) {
			throw new UsernameAlreadyExistsException(username, username + " is already taken.");
		}
		User user = new User();
		user.setUsername(username);
		user.setEnabled(true);
		String encryptedPassword = encryptPassword(username, password);
		user.setPassword(encryptedPassword);
		Iterable<Role> roles = rolesUpToAndIncluding(role);
		Set<Authority> authorities = new HashSet<Authority>();
		Iterables.addAll(authorities, Iterables.transform(roles, newAuthorityFunction()));
		user.setAuthorities(authorities);
		getUserDao().saveOrUpdate(user);
	}
	protected String encryptPassword(String username, String password) {
		Set<GrantedAuthority> emptySet = Collections.emptySet();
		org.springframework.security.core.userdetails.User user = 
				new org.springframework.security.core.userdetails.User(username, password, true, true, true, true, emptySet);
		return getPasswordEncoder().encodePassword(password, getSaltSource().getSalt(user));
	}

	protected Iterable<Role> rolesUpToAndIncluding(Role role) {
		SortedSet<Role> allRoles = getAllRoles();
		return Iterables.concat(allRoles.subSet(getSmallestRole(), role), Collections.singleton(role));
	}

	protected Function<Role, Authority> newAuthorityFunction() {
		return new Function<Role, Authority>() {
			@Override
			public Authority apply(Role role) {
				Authority authority = new Authority();
				authority.setRole(role);
				return authority;
			}
		};
	}


	public PasswordEncoder getPasswordEncoder() {
		return i_passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		i_passwordEncoder = passwordEncoder;
	}

	public SortedSet<Role> getAllRoles() {
		return i_allRoles;
	}

	public Role getSmallestRole() {
		return i_smallestRole;
	}

	public void setSmallestRole(Role smallestRole) {
		i_smallestRole = smallestRole;
	}

	public void setAllRoles(SortedSet<Role> allRoles) {
		i_allRoles = allRoles;
	}

	public SaltSource getSaltSource() {
		return i_saltSource;
	}

	public void setSaltSource(SaltSource saltSource) {
		i_saltSource = saltSource;
	}

	public UserDao getUserDao() {
		return i_userDao;
	}

	public void setUserDao(UserDao userDao) {
		i_userDao = userDao;
	}

}
