/**
 * Copyright 2012 Alex Jones
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
package model

/**
 * The location of where a game is played, either home or away. Note that this
 * decided by who is the designated home team for a game.
 *
 * @author alex
 *
 */
sealed trait Location extends Location.Value {
  val isHome: Boolean
  val isAway = !isHome
}
object Location extends PersistableEnumeration[Location] {
  /**
   * Home
   */
  case object HOME extends Location {
    val persistableToken = "HOME"
    val isHome = true
  }
  HOME

  /**
   * Away
   */
  case object AWAY extends Location {
    val persistableToken = "AWAY"
    val isHome = false
  }
  AWAY
}
