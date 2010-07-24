/**
 * Copyright 2010 Alex Jones
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
package uk.co.unclealex.hammers.calendar.dao;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import uk.co.unclealex.hammers.calendar.model.Competition;
import uk.co.unclealex.hammers.calendar.model.Game;
import uk.co.unclealex.hammers.calendar.model.Location;

public interface GameDao {

	public Game findById(int id);
	public Game findByDatePlayed(Date datePlayed);
	public SortedSet<Game> getAllForSeason(int season);
	public SortedSet<Integer> getAllSeasons();
	public Game findByBusinessKey(Competition competition, Location location, String opponents, int season);
	public void store(Game game);
	public List<Game> getAll();
	public List<Game> getAllByAttendence(boolean booleanValue);
	public List<Game> getAllTicketDates();
	public List<Game> getAllAfter(Date date);
	public List<Game> getAllTicketDatesAfter(Date date);
}
