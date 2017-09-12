/*
 * Copyright 2014 Alex Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import io.circe.{Decoder, Encoder}

/**
 * A class that encapsulates the global information sent to the client.
 */
case class Globals(
  /**
   * The currently persisted seasons.
   */
  seasons: List[Int],
  /**
   * The name of the currently logged in user.
   */
  maybeUsername: Option[String]) {
}

object Globals {

  implicit val globalsEncoder: Encoder[Globals] =
    Encoder.forProduct2("seasons", "username")(g => (g.seasons, g.maybeUsername))
  implicit val globalsDecoder: Decoder[Globals] =
    Decoder.forProduct2("seasons", "username")(Globals.apply)
}