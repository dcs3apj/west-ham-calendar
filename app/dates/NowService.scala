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
package dates

import org.joda.time.DateTime

/**
 * A trait for classes that can retrieve the current time or a predetermined time during tests.
 * @author alex
 *
 */
trait NowService {

  /**
   * Get the current time.
   */
  def now: DateTime
}

object NowService {

  /**
   * Generate a static NowService.
   * @param dateTime
   * @return
   */
  def apply(dateTime: DateTime): NowService = new NowService {
    override def now: DateTime = dateTime
  }
}