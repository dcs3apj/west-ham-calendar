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

import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;

import org.joda.time.DateTime;

import uk.co.unclealex.hammers.calendar.server.calendar.google.GoogleCalendar;
import uk.co.unclealex.hammers.calendar.server.calendar.google.GoogleCalendarFactory;
import uk.co.unclealex.hammers.calendar.server.dao.GameDao;
import uk.co.unclealex.hammers.calendar.server.dates.DateService;
import uk.co.unclealex.hammers.calendar.server.model.Game;
import uk.co.unclealex.hammers.calendar.server.tickets.TicketingCalendarService;
import uk.co.unclealex.hammers.calendar.shared.model.CalendarType;
import uk.co.unclealex.hammers.calendar.shared.model.GameView;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * @author alex
 * 
 */
public class GameServiceImpl implements GameService {

	private GameDao i_gameDao;
	private TicketingCalendarService i_ticketingCalendarService;
	private DateService i_dateService;
	private GoogleCalendarFactory i_googleCalendarFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedSet<GameView> getGameViewsForSeasonByOpponents(boolean enabled, int season) {
		Comparator<GameView> comparator = new Comparator<GameView>() {
			@Override
			public int compare(GameView g1, GameView g2) {
				int cmp = g1.getOpponents().compareTo(g2.getOpponents());
				if (cmp == 0) {
					cmp = g1.getLocation().compareTo(g2.getLocation());
					if (cmp == 0) {
						cmp = g1.getDatePlayed().compareTo(g2.getDatePlayed());
					}
				}
				return cmp;
			}
		};
		return getGameViewsForSeason(enabled, season, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedSet<GameView> getGameViewsForSeasonByDatePlayed(boolean enabled, int season) {
		Comparator<GameView> comparator = new Comparator<GameView>() {
			@Override
			public int compare(GameView g1, GameView g2) {
				return g1.getDatePlayed().compareTo(g2.getDatePlayed());
			}
		};
		return getGameViewsForSeason(enabled, season, comparator);
	}

	protected SortedSet<GameView> getGameViewsForSeason(boolean enabled, int season, Comparator<GameView> comparator) {
		SortedSet<GameView> gameViewsForSeason = Sets.newTreeSet(comparator);
		Iterables.addAll(gameViewsForSeason,
				Iterables.transform(getGameDao().getAllForSeason(season), createGameViewFunction(enabled)));
		return gameViewsForSeason;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameView getGameViewById(int gameId, boolean enabled) {
		Game game = getGameDao().findById(gameId);
		return createGameViewFunction(enabled).apply(game);
	}
	
	protected Function<Game, GameView> createGameViewFunction(final boolean enabled) {
		CalendarType selectedTicketingCalendarType = getTicketingCalendarService().getSelectedTicketingCalendar();
		final GoogleCalendar googleCalendar = selectedTicketingCalendarType == null ? null : getGoogleCalendarFactory()
				.getGoogleCalendar(selectedTicketingCalendarType);
		return new Function<uk.co.unclealex.hammers.calendar.server.model.Game, GameView>() {
			@Override
			public GameView apply(uk.co.unclealex.hammers.calendar.server.model.Game game) {
				Date ticketDate = googleCalendar == null ? null : googleCalendar.getGameDate(game).toDate();
				DateTime datePlayed = game.getDateTimePlayed();
				boolean weekGame = getDateService().isWeekday(datePlayed);
				boolean nonStandardWeekendGame = !weekGame && !getDateService().isThreeOClockOnASaturday(datePlayed);
				return new GameView(game.getId(), game.getCompetition(), game.getLocation(), game.getOpponents(),
						game.getSeason(), datePlayed.toDate(), game.getResult(), game.getAttendence(), game.getMatchReport(),
						game.getTelevisionChannel(), ticketDate, game.isAttended(), weekGame, nonStandardWeekendGame,
						enabled);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedSet<Integer> getAllSeasons() {
		return getGameDao().getAllSeasons();
	}

	public GameDao getGameDao() {
		return i_gameDao;
	}

	public void setGameDao(GameDao gameDao) {
		i_gameDao = gameDao;
	}

	public TicketingCalendarService getTicketingCalendarService() {
		return i_ticketingCalendarService;
	}

	public void setTicketingCalendarService(TicketingCalendarService ticketingCalendarService) {
		i_ticketingCalendarService = ticketingCalendarService;
	}

	public DateService getDateService() {
		return i_dateService;
	}

	public void setDateService(DateService dateService) {
		i_dateService = dateService;
	}

	public GoogleCalendarFactory getGoogleCalendarFactory() {
		return i_googleCalendarFactory;
	}

	public void setGoogleCalendarFactory(GoogleCalendarFactory googleCalendarFactory) {
		i_googleCalendarFactory = googleCalendarFactory;
	}

}
