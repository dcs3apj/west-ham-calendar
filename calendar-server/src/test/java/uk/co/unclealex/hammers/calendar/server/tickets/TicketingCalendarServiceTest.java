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

package uk.co.unclealex.hammers.calendar.server.tickets;

import javax.persistence.Table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import uk.co.unclealex.hammers.calendar.server.model.TicketingCalendar;
import uk.co.unclealex.hammers.calendar.shared.model.CalendarType;

/**
 * The Class TicketingCalendarServiceTest.
 * 
 * @author alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/application-contexts/dao/context.xml",
    "/application-contexts/dao/test-db.xml",
    "/application-contexts/tickets/context.xml" })
@SuppressWarnings("deprecation")
public class TicketingCalendarServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

  /** The Constant TABLE_NAME. */
  private static final String TABLE_NAME = TicketingCalendar.class.getAnnotation(Table.class).name();

  /** The simple jdbc template. */
  @Autowired
  private SimpleJdbcTemplate simpleJdbcTemplate;

  /** The ticketing calendar service. */
  @Autowired
  private TicketingCalendarService ticketingCalendarService;

  /**
   * Setup.
   */
  @Before
  public void setup() {
    SimpleJdbcTestUtils.deleteFromTables(simpleJdbcTemplate, TABLE_NAME);
  }

  /**
   * Test default is null.
   */
  @Test
  public void testDefaultIsNull() {
    Assert.assertNull(
        "By default, the selected ticketing calendar was not null.",
        ticketingCalendarService.getSelectedTicketingCalendar());
  }

  /**
   * Test all calendar types.
   */
  @Test
  public void testAllCalendarTypes() {
    for (final CalendarType calendarType : CalendarType.values()) {
      try {
        ticketingCalendarService.setSelectedTicketingCalendar(calendarType);
        Assert.assertTrue(
            "Setting the selected calendar should have failed for calendar type " + calendarType,
            calendarType.isTicketCalendar());
        Assert.assertEquals(
            "There were the wrong amount of ticketing calendars.",
            1,
            SimpleJdbcTestUtils.countRowsInTable(simpleJdbcTemplate, TABLE_NAME));
        final CalendarType actualCalendarType = ticketingCalendarService.getSelectedTicketingCalendar();
        Assert.assertEquals(
            "The wrong calendar was returned for calendar type " + calendarType,
            calendarType,
            actualCalendarType);
      }
      catch (final IllegalArgumentException e) {
        Assert.assertFalse(
            "Setting the selected calendar failed for calendar type " + calendarType,
            calendarType.isTicketCalendar());
      }
    }
  }

  /**
   * Test clear.
   */
  @Test
  public void testClear() {
    ticketingCalendarService.setSelectedTicketingCalendar(CalendarType.TICKETS_ACADEMY);
    ticketingCalendarService.setSelectedTicketingCalendar(null);
    Assert.assertNull("The selected calendar should be null.", ticketingCalendarService.getSelectedTicketingCalendar());
    Assert.assertEquals(
        "The wrong number of ticketing calendars were found.",
        0,
        SimpleJdbcTestUtils.countRowsInTable(simpleJdbcTemplate, TABLE_NAME));
  }
}
