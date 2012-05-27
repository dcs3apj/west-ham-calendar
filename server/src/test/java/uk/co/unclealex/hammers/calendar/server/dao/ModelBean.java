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

package uk.co.unclealex.hammers.calendar.server.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.Objects;

import uk.co.unclealex.hammers.calendar.server.model.HasBusinessKey;


/**
 * A Model bean for testing the business crud DAO.
 * 
 * @author alex
 * 
 */
@Entity
@Table(name = "model")
public class ModelBean implements HasBusinessKey<String> {

	/** The id. */
	private Integer i_id;
	
	/** The business key. */
	private String i_businessKey;
	
	/** The value. */
	private String i_value;

	/**
	 * Instantiates a new model bean.
	 */
	public ModelBean() {
		super();
	}

	/**
	 * Instantiates a new model bean.
	 * 
	 * @param businessKey
	 *          the business key
	 * @param value
	 *          the value
	 */
	public ModelBean(String businessKey, String value) {
		super();
		i_businessKey = businessKey;
		i_value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ModelBean && getBusinessKey().equals(((ModelBean) obj).getBusinessKey())
				&& getValue().equals(((ModelBean) obj).getValue()) && Objects.equal(getId(), ((ModelBean) obj).getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s: %s->%s", getId()==null?"null":getId().toString(), getBusinessKey(), getValue());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() {
		return i_id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *          the new id
	 */
	public void setId(Integer id) {
		i_id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Column(name = "businesskey", unique = true)
	public String getBusinessKey() {
		return i_businessKey;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBusinessKey(String businessKey) {
		i_businessKey = businessKey;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	@Column(name = "value")
	public String getValue() {
		return i_value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *          the new value
	 */
	public void setValue(String value) {
		i_value = value;
	}

}
