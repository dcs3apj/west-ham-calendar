/**
 * Copyright 2013 Alex Jones
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
package controllers

import play.mvc.Controller
import javax.inject.Inject
import uk.co.unclealex.hammers.calendar.update.MainUpdateService
import play.api.mvc.Action
import play.api.mvc.Results._
import java.io.StringWriter
import java.io.PrintWriter

/**
 * @author alex
 *
 */
class Update @Inject() (
  /**
   * The main update service used to scrape the West Ham site and update game information.
   */
  mainUpdateService: MainUpdateService) {

  def update = Action {
    try {
      mainUpdateService.processDatabaseUpdates
      Ok("Updated")
    } catch {
      case e: Exception => {
        val buffer = new StringWriter
        val writer = new PrintWriter(buffer)
        e.printStackTrace(writer)
        writer.close()
        InternalServerError(buffer.toString)
      }
    }
  }
}