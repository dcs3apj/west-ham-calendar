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
package uk.co.unclealex.hammers.calendar.server.auth;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
import uk.co.unclealex.hammers.calendar.server.model.Authority;
import uk.co.unclealex.hammers.calendar.server.model.User;
import uk.co.unclealex.hammers.calendar.shared.exceptions.NoSuchUsernameException;
import uk.co.unclealex.hammers.calendar.shared.exceptions.UsernameAlreadyExistsException;
import uk.co.unclealex.hammers.calendar.shared.model.Role;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * The default implementation of {@link UserService}.
 * @author alex
 *
 */
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(HibernateUserDao.class);
	
	/**
	 * The {@link PasswordEncoder} to use to encode passwords.
	 */
	private PasswordEncoder i_passwordEncoder;
	
	/**
	 * The {@link SaltSource} used by the password encoder.
	 */
	private SaltSource i_saltSource;
	
	/**
	 * The {@link UserDao} used for user persistence.
	 */
	private UserDao i_userDao;
	
	/**
	 * All roles, in order of authority.
	 */
	private SortedSet<Role> i_allRoles;

	/**
	 * The least authoratitive role.
	 */
	private Role i_smallestRole;

	/**
	 * Initialise the set of roles and their order of authority.
	 */
	@PostConstruct
	public void initialise() {
		TreeSet<Role> allRoles = new TreeSet<Role>(Arrays.asList(Role.values()));
		setAllRoles(allRoles);
		setSmallestRole(Iterables.get(allRoles, 0));
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addUser(String username, String password, Role role) throws UsernameAlreadyExistsException {
		UserDao userDao = getUserDao();
    if (userDao.findByKey(username) != null) {
			throw new UsernameAlreadyExistsException(username + " is already taken.");
		}
		User user = new User();
		user.setUsername(username);
		user.setEnabled(true);
		alterPassword(user, password);
		alterRoles(user, role);
		userDao.saveOrUpdate(user);
	}

	/**
	 * Change a user's roles.
	 * @param user The user to change roles for.
	 * @param role The highest authority role the user requires.
	 */
  protected void alterRoles(User user, Role role) {
    Iterable<Role> roles = rolesUpToAndIncluding(role);
		Set<Authority> authorities = removeRoles(user);
		Iterables.addAll(authorities, Iterables.transform(roles, newAuthorityFunction()));
		user.setAuthorities(authorities);
  }

  /**
   * Remove a roles for a user.
   * @param user The user who will lose their roles.
   * @return The roles the user used to have.
   */
  protected Set<Authority> removeRoles(User user) {
    Set<Authority> authorities = user.getAuthorities();
		if (authorities == null) {
		  authorities = new HashSet<Authority>();
		}
		else {
		  for (Iterator<Authority> iter = authorities.iterator(); iter.hasNext();) {
		    iter.next();
		    iter.remove();
		  }
		}
    return authorities;
  }

  /**
   * Alter a user's password.
   * @param user The user whose password will be changed.
   * @param password The new password for the user.
   */
  protected void alterPassword(User user, String password) {
    String encryptedPassword = encryptPassword(user.getUsername(), password);
		user.setPassword(encryptedPassword);
  }
	
  /**
   * {@inheritDoc}
   */
  @Override
	public void alterPassword(String username, String newPassword) throws NoSuchUsernameException {
	  User user = getExistingUserByUsername(username);
	  alterPassword(user, newPassword);
	  getUserDao().saveOrUpdate(user);
	}

  /**
   * Alter a user's roles.
   * @param username The username of the user who should have their roles changed.
   * @param role The highest authority role for the user.
   * @throws NoSuchUsernameException Thrown if their is no user with the given username.
   */
	protected void alterRole(String username, Role role) throws NoSuchUsernameException {
	  User user = getExistingUserByUsername(username);
	  alterRoles(user, role);
	  getUserDao().saveOrUpdate(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void alterUser(String username, String newPassword, Role newRole) throws NoSuchUsernameException {
    User user = getExistingUserByUsername(username);
    alterRoles(user, newRole);
    if (!newPassword.isEmpty()) {
      alterPassword(user, newPassword);
    }
    getUserDao().saveOrUpdate(user);
	}
	
	/**
	 * Find an existing user by their username.
	 * @param username The existing user's userrname.
	 * @return The user with the given username.
	 * @throws NoSuchUsernameException Thrown if no user has the given username.
	 */
  protected User getExistingUserByUsername(String username) throws NoSuchUsernameException {
    User user = getUserDao().findByKey(username);
    if (user == null) {
      throw new NoSuchUsernameException("Cannot find user " + username);
    }
    return user;
  }

  /**
   * Encrypt a user's password.
   * @param username The username of the user whose password needs to be encrypted.
   * @param password The user's unencrypted password.
   * @return The user's encrypted password.
   */
  protected String encryptPassword(String username, String password) {
		Set<GrantedAuthority> emptySet = Collections.emptySet();
		org.springframework.security.core.userdetails.User user = 
				new org.springframework.security.core.userdetails.User(username, password, true, true, true, true, emptySet);
		return getPasswordEncoder().encodePassword(password, getSaltSource().getSalt(user));
	}

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUser(String username) throws NoSuchUsernameException {
    User user = getExistingUserByUsername(username);
    removeRoles(user);
    UserDao userDao = getUserDao();
    userDao.saveOrUpdate(user);
    userDao.remove(user.getUsername());
  }
 
  /**
   * Get a list of roles up to and including a given role.
   * @param role The given role.
   * @return A list of roles up to and including a given role
   */
	protected Iterable<Role> rolesUpToAndIncluding(Role role) {
		SortedSet<Role> allRoles = getAllRoles();
		return Iterables.concat(allRoles.subSet(getSmallestRole(), role), Collections.singleton(role));
	}

	/**
	 * Create a {@link Function} that transforms a {@link Role} into an {@link Authority}.
	 * @return A {@link Function} that transforms a {@link Role} into an {@link Authority}.
	 */
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