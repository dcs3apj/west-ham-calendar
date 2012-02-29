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

package uk.co.unclealex.hammers.calendar.server.view;

import java.util.SortedSet;

import uk.co.unclealex.hammers.calendar.shared.model.GameView;


/**
 * The service for creating sets of {@link GameView} instances to be viewed by the GUI.
 * @author alex
 *
 */
public interface GameService {

	/**
	 * Get all games for a season in opponent order.
	 * @param enabled True if the game should be selectable, false otherwise.
	 * @param season The season to look for.
	 * @return All games for the given season in the described order.
	 */
	SortedSet<GameView> getGameViewsForSeasonByOpponents(boolean enabled, int season);
	
	/**
	 * Get all games for a season in chronological order.
	 * @param enabled True if the game should be selectable, false otherwise.
	 * @param season The season to look for.
	 * @return All games for the given season in the described order.
	 */
	SortedSet<GameView> getGameViewsForSeasonByDatePlayed(boolean enabled, int season);

	/**
	 * Get a seasons.
	 * @return All the seasons in ascending order.
	 */
	SortedSet<Integer> getAllSeasons();

	/** 
	 * Get a {@link GameView} that represents the {@link Game} with the given id.
	 * @param gameId The id of the game.
	 * @param enabled True if i_{@link GameView} can be edited, false otherwise.
	 * @return A {@link GameView} that represents the {@link Game} with the given id.
	 */
	GameView getGameViewById(int gameId, boolean enabled);
	
}
