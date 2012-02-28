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
 */
package uk.co.unclealex.hammers.calendar.server.model;

import java.io.Serializable;

/**
 * The base class for models that can be uniquely defined by a business key.
 * @author alex
 *
 * @param <K> The type of the business key.
 * @param <M> The type of the model.
 */
public abstract class AbstractBusinessKeyBasedModel<K extends Comparable<K> & Serializable, M extends HasBusinessKey<K>>
		implements HasBusinessKey<K>, Comparable<M> {

	@Override
	public final int compareTo(M o) {
		return getBusinessKey().compareTo(o.getBusinessKey());
	}

	@Override
	public final int hashCode() {
		return getBusinessKey().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final boolean equals(Object obj) {
		return getClass().equals(obj.getClass()) && getBusinessKey().equals(((M) obj).getBusinessKey());
	}

}
