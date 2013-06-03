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

package uk.co.unclealex.hammers.calendar.html;

import java.io.IOException
import java.net.URI

import scala.collection.JavaConverters.iterableAsScalaIterableConverter

import org.specs2.mutable.Specification

/**
 * Test that the links to each season work.
 *
 * @author alex
 */
class SeasonsLinkHarvesterTest extends Specification {

  "Harvesting the links for each season" should {
    "Return a link for each season" in {
      val tagNode = new HtmlPageLoaderImpl().loadPage(getClass.getClassLoader.getResource("html/fixtures.html"))
      val actualLinks = new SeasonsLinkHarvester().harvestLinks(
        new URI("http://www.whufc.com/page/FixturesResults/0,,12562,00.html"), tagNode)
      actualLinks must be equalTo (
        List(
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2001,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2002,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2003,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2004,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2005,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2006,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2007,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2008,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2009,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2010,00.html"),
          new URI("http://www.whufc.com/page/FixturesResults/0,,12562~2011,00.html")))
    }
  }
}
