/**
 * Copyright 2012 Alex Jones
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
package uk.co.unclealex.hammers.calendar.shared.model;


/**
 * The roles that a user can have.
 * @author alex
 *
 */
public enum Role {
	/**
	 * The standard user role who can attend or unattend games.
	 */
	ROLE_USER("User"),
	
	/**
	 * The administrator role who can do anything.
	 */
	ROLE_ADMIN("Administrator");
	
	/**
	 * The displayable name for i_role.
	 */
	private final String i_displayableName;

  /**
	 * Instantiates a new role.
	 * 
	 * @param displayableName
	 *          the displayable name
	 */
  private Role(String displayableName) {
    i_displayableName = displayableName;
  }

  /**
	 * Gets the displayable name for i_role.
	 * 
	 * @return the displayable name for i_role
	 */
  public String getDisplayableName() {
    return i_displayableName;
  }
	
	
}
