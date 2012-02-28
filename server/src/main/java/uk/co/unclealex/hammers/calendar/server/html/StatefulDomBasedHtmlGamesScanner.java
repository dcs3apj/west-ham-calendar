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

package uk.co.unclealex.hammers.calendar.server.html;

import java.io.IOException;
import java.net.URI;
import java.util.SortedSet;

import org.htmlcleaner.TagNode;

import com.google.common.collect.Sets;

/**
 * A base class for {@link HtmlGamesScanner}s that require state. Really, this is to avoid writing Spring factories. 
 * @author alex
 *
 */
public abstract class StatefulDomBasedHtmlGamesScanner extends TagNodeBasedHtmlGamesScanner {

	/**
	 * {@inheritDoc}
	 */
	@Override SortedSet<GameUpdateCommand> scan(URI uri, TagNode tagNode) throws IOException {
		Scanner scanner = createScanner(uri, tagNode);
		scanner.scan();
		return scanner.getGameUpdateCommands();
	}
	
	/**
	 * Create a scanner to scan the games.
	 * @param uri The URI of the page being scanned.
	 * @param tagNode The XML tagNode to scan.
	 * @return A scanner as described above.
	 */
	protected abstract Scanner createScanner(URI uri, TagNode tagNode);

	/**
	 * An abstract class to allow for scanning state to be stored, mainly so that there is
	 * no need to muck around with spring-based factories.
	 * @author alex
	 *
	 */
	abstract class Scanner {

		/**
		 * The set of {@link GameUpdateCommand}s to update.
		 */
		private final SortedSet<GameUpdateCommand> i_gameUpdateCommands = Sets.newTreeSet();
		
		/**
		 * The URI of the page being scanned.
		 */
		private final URI i_uri;
		
		/**
		 * The top-level {@link TagNode} of the page being scanned.
		 */
		private final TagNode i_tagNode;
		
		public Scanner(URI uri, TagNode tagNode) {
			super();
			i_uri = uri;
			i_tagNode = tagNode;
		}

		/**
		 * Scan the page for any information.
		 * @throws IOException
		 */
		public abstract void scan() throws IOException;
		
		public final SortedSet<GameUpdateCommand> getGameUpdateCommands() {
			return i_gameUpdateCommands;
		}

		public final URI getUri() {
			return i_uri;
		}

		public final TagNode getTagNode() {
			return i_tagNode;
		};
	}	
}
