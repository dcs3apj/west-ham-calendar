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

package uk.co.unclealex.hammers.calendar.server.calendar;

/**
 * 
 * A visitor for {@link GameUpdateInformation} beans.
 * 
 * @author alex
 * 
 */
public interface GameUpdateInformationVisitor {

	/**
	 * @param gameWasUpdatedInformation
	 */
	void visit(GameUpdateInformation gameUpdateInformation);

	/**
	 * @param gameWasUpdatedInformation
	 */
	void visit(GameWasUpdatedInformation gameWasUpdatedInformation);

	/**
	 * @param gameWasCreatedInformation
	 */
	void visit(GameWasCreatedInformation gameWasCreatedInformation);

	/**
	 * The default implementation of a {@link GameUpdateInformationVisitor} that
	 * throws an {@link IllegalArgumentException} if the supplied
	 * {@link GameUpdateInformation} bean is unknown.
	 * 
	 * @author alex
	 * 
	 */
	abstract class Default implements GameUpdateInformationVisitor {

		public void visit(GameUpdateInformation gameUpdateInformation) {
			throw new IllegalArgumentException("The class " + gameUpdateInformation.getClass() + " is unrecognised.");
		}
	}
}
