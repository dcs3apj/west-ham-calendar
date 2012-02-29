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
package uk.co.unclealex.hammers.calendar.server.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


/**
 * A {@link User} is someone who can log in to the West Ham Calender website.
 * @author alex
 *
 */
@Entity
@Table(name = "users")
public class User extends AbstractBusinessKeyBasedModel<String, User> {

	/**
	 * The id of the user.
	 */
	private Integer i_id;
	
	/**
	 * The user's username.
	 */
	private String i_username;
	
	/**
	 * The user's encrypted password.
	 */
	private String i_password;
	
	/**
	 * A flag indicating whether a user is enabled.
	 */
	private boolean i_enabled;
	
	/**
	 * The set of authorities granted to i_user.
	 */
	private Set<Authority> i_authorities;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getUsername();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public String getBusinessKey() {
		return getUsername();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBusinessKey(String businessKey) {
		setUsername(businessKey);
	}

	/**
	 * Gets the user's username.
	 * 
	 * @return the user's username
	 */
	@Column(nullable = false, name = "username")
	public String getUsername() {
		return i_username;
	}

	/**
	 * Sets the user's username.
	 * 
	 * @param username
	 *          the new user's username
	 */
	public void setUsername(String username) {
		i_username = username;
	}

	/**
	 * Gets the user's encrypted password.
	 * 
	 * @return the user's encrypted password
	 */
	@Column(nullable = false, name = "password")
	public String getPassword() {
		return i_password;
	}

	/**
	 * Sets the user's encrypted password.
	 * 
	 * @param password
	 *          the new user's encrypted password
	 */
	public void setPassword(String password) {
		i_password = password;
	}

	/**
	 * Checks if is a flag indicating whether a user is enabled.
	 * 
	 * @return the a flag indicating whether a user is enabled
	 */
	@Column(name = "enabled")
	public boolean isEnabled() {
		return i_enabled;
	}

	/**
	 * Sets the a flag indicating whether a user is enabled.
	 * 
	 * @param enabled
	 *          the new a flag indicating whether a user is enabled
	 */
	public void setEnabled(boolean enabled) {
		i_enabled = enabled;
	}

	/**
	 * Gets the set of authorities granted to i_user.
	 * 
	 * @return the set of authorities granted to i_user
	 */
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	public Set<Authority> getAuthorities() {
		return i_authorities;
	}

	/**
	 * Sets the set of authorities granted to i_user.
	 * 
	 * @param authorities
	 *          the new set of authorities granted to i_user
	 */
	public void setAuthorities(Set<Authority> authorities) {
		i_authorities = authorities;
	}

	/**
	 * {@inheritDoc}
	 */
	@Id
	@GeneratedValue
	public Integer getId() {
		return i_id;
	}

	/**
	 * Sets the id of the user.
	 * 
	 * @param id
	 *          the new id of the user
	 */
	public void setId(Integer id) {
		i_id = id;
	}
}
