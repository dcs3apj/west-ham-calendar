/**
 * Copyright 2010-2012 Alex Jones
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with work for additional information
 * regarding copyright ownership.  The ASF licenses file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use file except in compliance
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

package uk.co.unclealex.hammers.calendar.view;

import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import uk.co.unclealex.hammers.calendar.calendar.google.GoogleCalendar;
import uk.co.unclealex.hammers.calendar.calendar.google.GoogleCalendarFactory;
import uk.co.unclealex.hammers.calendar.dao.GameDao;
import uk.co.unclealex.hammers.calendar.dates.DateTimeImplicits;
import uk.co.unclealex.hammers.calendar.model.CalendarType;
import uk.co.unclealex.hammers.calendar.model.Game;
import uk.co.unclealex.hammers.calendar.model.GameView;
import uk.co.unclealex.hammers.calendar.tickets.TicketingCalendarService;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * The default implementation of {@link GameService}.
 * 
 * @author alex
 * 
 */
public class GameServiceImpl implements GameService {

  /**
   * The {@link GameDao} used to persist {@link Game}s.
   */
  private GameDao gameDao;

  /**
   * The {@link TicketingCalendarService} used to interface with the selected
   * ticketing calendar.
   */
  private TicketingCalendarService ticketingCalendarService;

  /**
   * The {@link GoogleCalendarFactory} used to get information about
   * {@link GoogleCalendar}s.
   */
  private GoogleCalendarFactory googleCalendarFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public SortedSet<GameView> getGameViewsForSeasonByOpponents(final boolean enabled, final int season) {
    final Comparator<GameView> comparator = new Comparator<GameView>() {
      @Override
      public int compare(final GameView g1, final GameView g2) {
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
  public SortedSet<GameView> getGameViewsForSeasonByDatePlayed(final boolean enabled, final int season) {
    final Comparator<GameView> comparator = new Comparator<GameView>() {
      @Override
      public int compare(final GameView g1, final GameView g2) {
        return g1.getDatePlayed().compareTo(g2.getDatePlayed());
      }
    };
    return getGameViewsForSeason(enabled, season, comparator);
  }

  /**
   * Get all the {@link GameView}s for a given season.
   * 
   * @param enabled
   *          True if these {@link GameView}s can be altered, false otherwise.
   * @param season
   *          The season for the {@link GameView}s.
   * @param comparator
   *          A {@link Comparator} to order the {@link GameView}s.
   * @return A sorted set of all the {@link GameView}s for a season.
   */
  protected SortedSet<GameView> getGameViewsForSeason(
      final boolean enabled,
      final int season,
      final Comparator<GameView> comparator) {
    final SortedSet<GameView> gameViewsForSeason = Sets.newTreeSet(comparator);
    Iterables.addAll(
        gameViewsForSeason,
        Iterables.transform(getGameDao().getAllForSeason(season), createGameViewFunction(enabled)));
    return gameViewsForSeason;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameView getGameViewById(final int gameId, final boolean enabled) {
    final Game game = getGameDao().findById(gameId);
    return createGameViewFunction(enabled).apply(game);
  }

  /**
   * Create a {@link Function} that transforms a {@link Game} into a
   * {@link GameView}.
   * 
   * @param enabled
   *          True if the {@link GameView} can be edited, false otherwise.
   * @return A {@link Function} that transforms a {@link Game} into a
   *         {@link GameView}.
   */
  protected Function<Game, GameView> createGameViewFunction(final boolean enabled) {
    final CalendarType selectedTicketingCalendarType = getTicketingCalendarService().getSelectedTicketingCalendar();
    final GoogleCalendar googleCalendar =
        selectedTicketingCalendarType == null ? null : getGoogleCalendarFactory().getGoogleCalendar(
            selectedTicketingCalendarType);
    return new Function<uk.co.unclealex.hammers.calendar.model.Game, GameView>() {
      @Override
      public GameView apply(final uk.co.unclealex.hammers.calendar.model.Game game) {
        Date ticketDate;
        if (googleCalendar == null) {
          ticketDate = null;
        }
        else {
          final Interval interval = googleCalendar.toCalendarDateInterval().apply(game);
          ticketDate = interval == null ? null : interval.getStart().toDate();
        }
        final DateTime datePlayed = game.getDateTimePlayed();
        final boolean weekGame = DateTimeImplicits.isWeekday(datePlayed);
        final boolean nonStandardWeekendGame = !weekGame && !DateTimeImplicits.isThreeOClockOnASaturday(datePlayed);
        return new GameView(
            game.getId(),
            game.getCompetition(),
            game.getLocation(),
            game.getOpponents(),
            game.getSeason(),
            datePlayed.toDate(),
            game.getResult(),
            game.getAttendence(),
            game.getMatchReport(),
            game.getTelevisionChannel(),
            ticketDate,
            game.isAttended(),
            weekGame,
            nonStandardWeekendGame,
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

  /**
   * Gets the {@link GameDao} used to persist {@link Game}s.
   * 
   * @return the {@link GameDao} used to persist {@link Game}s
   */
  public GameDao getGameDao() {
    return gameDao;
  }

  /**
   * Sets the {@link GameDao} used to persist {@link Game}s.
   * 
   * @param gameDao
   *          the new {@link GameDao} used to persist {@link Game}s
   */
  public void setGameDao(final GameDao gameDao) {
    this.gameDao = gameDao;
  }

  /**
   * Gets the {@link TicketingCalendarService} used to interface with the
   * selected ticketing calendar.
   * 
   * @return the {@link TicketingCalendarService} used to interface with the
   *         selected ticketing calendar
   */
  public TicketingCalendarService getTicketingCalendarService() {
    return ticketingCalendarService;
  }

  /**
   * Sets the {@link TicketingCalendarService} used to interface with the
   * selected ticketing calendar.
   * 
   * @param ticketingCalendarService
   *          the new {@link TicketingCalendarService} used to interface with
   *          the selected ticketing calendar
   */
  public void setTicketingCalendarService(final TicketingCalendarService ticketingCalendarService) {
    this.ticketingCalendarService = ticketingCalendarService;
  }

  /**
   * Gets the {@link GoogleCalendarFactory} used to get information about
   * {@link GoogleCalendar}s.
   * 
   * @return the {@link GoogleCalendarFactory} used to get information about
   *         {@link GoogleCalendar}s
   */
  public GoogleCalendarFactory getGoogleCalendarFactory() {
    return googleCalendarFactory;
  }

  /**
   * Sets the {@link GoogleCalendarFactory} used to get information about
   * {@link GoogleCalendar}s.
   * 
   * @param googleCalendarFactory
   *          the new {@link GoogleCalendarFactory} used to get information
   *          about {@link GoogleCalendar}s
   */
  public void setGoogleCalendarFactory(final GoogleCalendarFactory googleCalendarFactory) {
    this.googleCalendarFactory = googleCalendarFactory;
  }

}
