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

package uk.co.unclealex.hammers.calendar.server.view;

import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;

import uk.co.unclealex.hammers.calendar.shared.model.GameView;
import uk.co.unclealex.hammers.calendar.shared.model.LeagueRow;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * @author alex
 * 
 */
public class LeagueServiceImpl implements LeagueService {

	private GameService i_gameService;

	@Override
	public SortedSet<LeagueRow> getLeagueForSeason(int season) {
		Predicate<GameView> isLeagueGameViewPredicate = new Predicate<GameView>() {
			@Override
			public boolean apply(GameView gameView) {
				return gameView.getCompetition().isLeague();
			}
		};
		SortedSet<LeagueRow> league = Sets.newTreeSet();
		for (final GameView gameView : Iterables.filter(getGameService().getGameViewsForSeasonByDatePlayed(true, season),
				isLeagueGameViewPredicate)) {
			final String opponents = gameView.getOpponents();
			Predicate<LeagueRow> predicate = new Predicate<LeagueRow>() {
				@Override
				public boolean apply(LeagueRow leagueRow) {
					return leagueRow.getTeam().equals(opponents);
				}
			};
			String result = gameView.getResult();
			if (result != null) {
				String[] scores = StringUtils.split(result.substring(2), '-');
				int goalsFor = Integer.valueOf(scores[0]);
				int goalsAgainst = Integer.valueOf(scores[1]);
				LeagueRow leagueRow = Iterables.find(league, predicate, null);
				if (leagueRow == null) {
					leagueRow = new LeagueRow(opponents, goalsFor, goalsAgainst);
					league.add(leagueRow);
				}
				else {
					leagueRow.addGame(goalsFor, goalsAgainst);
				}
			}
		}
		return league;
	}

	public GameService getGameService() {
		return i_gameService;
	}

	public void setGameService(GameService gameService) {
		i_gameService = gameService;
	}

}
