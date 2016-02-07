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

package dates

import dates.DateTimeImplicits._
import org.joda.time.DateTime
import org.specs2.mutable.Specification

/**
 * The Class DateServiceImplTest.
 *
 * @author alex
 */
class DateServiceImplSpec extends Specification {

  "A day in a month before the current date but meant to be later than it" should {
    "be interpreted as a date in the next year" in {
      January(6) after December(25, 2012) must be_===(January(6, 2013))
    }
  }

  "A day in a month after the current date and meant to be later than it" should {
    "be interpreted as a date in the same year" in {
      March(6) after February(25, 2013) must be_===(March(6, 2013))
    }
  }

  "A day in a month before the current date and meant to be earlier than it" should {
    "be interpreted as a date in the same year" in {
      June(6) before September(5, 2012) must be_===(June(6, 2012))
    }
  }

  "A day in a month after the current date and meant to be earlier than it" should {
    "be interpreted as a date in the previous year" in {
      December(25) before January(6, 2012) must be_===(December(25, 2011))
    }
  }

  "A date format with an explicit year" should {
    parseAndFind("05/09/1972 9:12", Before(October(10, 2012)) parsedBy "dd/MM[/yyyy] HH:mm",
      Some(September(5, 1972) at (9, 12)))
  }

  "A date format that possibly requires a year to be added" should {
    parseAndFind("05/09 9:12", Before(October(10, 2012)) parsedBy "dd/MM[/yyyy] HH:mm",
      Some(September(5, 2012) at (9, 12)))
  }

  "A date format that definitely requires a year to be added" should {
    parseAndFind("05/09 9:12", Before(October(10, 2012)) parsedBy "dd/MM HH:mm",
      Some(September(5, 2012) at (9, 12)))
  }

  "A date format that fails the first parse but succedes on the second" should {
    parseAndFind("05/09 9:12", Before(October(10, 2012)) parsedBy ("HH:mm dd/MM[/yyyy]", "dd/MM[/yyyy] HH:mm"),
      Some(September(5, 2012) at (9, 12)))
  }

  "A date format requiring the day of a week but without a year" should {
    parseAndFind("9am Thu 26 Jan", Before(February(18, 2012)) parsedBy "ha EEE dd MMM",
      Some(January(26, 2012) at (9, 0)))
  }

  "An invalid date format" should {
    parseAndFind("05:09 9:12", Before(October(10, 2012)) parsedBy "dd/MM[/yyyy] HH:mm", None)
  }

  "Checking for whether a day is during the working week or not" should {
    sealed class DayOfWeek(val day: Int, val name: String, val isWeekday: Boolean)
    object Sunday extends DayOfWeek(3, "Sunday", false)
    object Monday extends DayOfWeek(4, "Monday", true)
    object Tuesday extends DayOfWeek(5, "Tuesday", true)
    object Wednesday extends DayOfWeek(6, "Wednesday", true)
    object Thursday extends DayOfWeek(7, "Thursday", true)
    object Friday extends DayOfWeek(8, "Friday", true)
    object Saturday extends DayOfWeek(9, "Saturday", false)
    List(Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday) foreach { dayOfWeek =>
      val day = September(dayOfWeek.day, 1972)
      s"${dayOfWeek.name} $day must ${if (dayOfWeek.isWeekday) "" else "not "}be a weekday" in {
        day.toDateTime.isWeekday must be_===(dayOfWeek.isWeekday)
      }
    }
    "nothing else" in {
      1 must be_===(1)
    }
  }

  "Checking for whether an instant is the fabled Saturday at 3pm" should {
    List(
      September(9, 1972) at (15, 0),
      September(9, 1972) at (12, 0),
      September(9, 1972) at (15, 30),
      September(9, 1972) at (19, 45),
      September(10, 1972) at (15, 0),
      September(10, 1972) at (12, 0),
      September(10, 1972) at (15, 30),
      September(10, 1972) at (19, 45)) zip List(true, false, false, false, false, false, false, false) foreach {
        case (day, isSaturday3pm) =>
          s"$day must ${if (isSaturday3pm) "" else "not "}be Saturday 3pm" in {
            day.toDateTime.isThreeOClockOnASaturday must be_===(isSaturday3pm)
          }
      }
    "nothing else" in {
      1 must be_===(1)
    }
  }

  /**
   * Test parse and find.
   *
   * @param expectedDateTime
   *          the expected date time
   * @param date
   *          the date
   * @param yearDeterminingDate
   *          the year determining date
   * @param yearDeterminingDateIsLaterThanTheDate
   *          the year determining date is later than the date
   * @param possiblyYearlessDateFormats
   *          the possibly yearless date formats
   */
  def parseAndFind(
                    date: String,
                    parsingRules: (Boolean, Date, Seq[String]),
                    expectedDateTime: Option[DateTime]) = {
    val (yearDeterminingDateIsLaterThanTheDate, yearDeterminingDate, possiblyYearlessDateFormats) = parsingRules
    s"The date string '$date' using formats ${possiblyYearlessDateFormats.mkString(", ")} must parse to $expectedDateTime" in {
      val actualDateTime =
        new DateServiceImpl().parsePossiblyYearlessDate(
          date,
          yearDeterminingDate,
          yearDeterminingDateIsLaterThanTheDate,
          possiblyYearlessDateFormats: _*)
      actualDateTime must be_===(expectedDateTime)
    }
    1 to 3 flatMap { paddingSize =>
      val padding = (1 to paddingSize).map(_ => "x").mkString
      List(date, padding + date, date + padding, padding + date + padding)
    } foreach { date =>
      s"The date string '$date' using formats ${possiblyYearlessDateFormats.mkString(", ")} must parse to $expectedDateTime" in {
        val actualDateTime =
          new DateServiceImpl().findPossiblyYearlessDate(
            date,
            yearDeterminingDate,
            yearDeterminingDateIsLaterThanTheDate,
            possiblyYearlessDateFormats: _*)
        actualDateTime must be_===(expectedDateTime)
      }
    }
    "nothing else" in {
      1 must be_===(1)
    }
  }

  /**
   * Syntatic sugar for date parsing information.
   */
  sealed class BeforeOrAfter(date: Date, before: Boolean) {
    def parsedBy(parseStrings: String*) = (before, date, parseStrings)
  }

  case class Before(date: Date) extends BeforeOrAfter(date, true)
  case class After(date: Date) extends BeforeOrAfter(date, false)

  implicit class BeforeAndAfterImplicits(monthAndDay: MonthAndDay) {
    def after(date: Date) = alter(date, yearDeterminingDateIsLaterThanTheDate = false)
    def before(date: Date) = alter(date, yearDeterminingDateIsLaterThanTheDate = true)

    def alter(yearDeterminingDate: Date, yearDeterminingDateIsLaterThanTheDate: Boolean) = {
      val newDateTime = YearSettingDateParserFactory.setYear(
        monthAndDay.toDateTime, yearDeterminingDate.toDateTime, yearDeterminingDateIsLaterThanTheDate)
      Date(newDateTime)
    }

  }
}
