/**
 * Copyright 2013 Alex Jones
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

/**
 * @author alex
 *
 */
import org.squeryl.Session
import org.squeryl.SessionFactory
import org.squeryl.adapters.H2Adapter
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.internals.DatabaseAdapter
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.Logging
import module.CalendarModule
import play.api.Application
import play.api.GlobalSettings
import play.api.db.DB
import play.api.mvc.Action
import play.api.mvc.Handler
import play.api.mvc.RequestHeader
import play.api.mvc.Results
import uk.co.unclealex.hammers.calendar.dao.SquerylGameDao
import play.api.Configuration

/**
 * The Play Framework global entry point.
 */
object Global extends GlobalSettings with Logging with Results {

  // Guice
  private lazy val injector = Guice.createInjector(new CalendarModule)

  // Guice
  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }

  override def onStart(app: Application) {
    logger info "Setting up database access."
    // Set up Squeryl database access
    SessionFactory.concreteFactory = app.configuration.getString("db.default.driver") match {
      case Some("org.h2.Driver") => Some(() => getSession(new H2Adapter, app))
      case Some("org.postgresql.Driver") => Some(() => getSession(new PostgreSqlAdapter, app))
      case _ => sys.error("Database driver must be either org.h2.Driver or org.postgresql.Driver")
    }
    // Schedule the update job
    logger info "Setting up calendar updates."
  }

  def getSession(adapter: DatabaseAdapter, app: Application) = Session.create(DB.getConnection()(app), adapter)

  /**
   * Enforce SSL if secure social requires it.
   */
  override def onRouteRequest(request: RequestHeader) = {
    onRouteRequest(
      configuration.getBoolean("securesocial.ssl").getOrElse(false),
      request,
      new SslRouter() {
        def onAllowed(request: RequestHeader) = Global.super.onRouteRequest(request)
        def onRedirectRequired(request: RequestHeader, url: String) = Action(MovedPermanently(url))
      })
  }

  /**
   * Allow different configurations for unit testing.
   */
  def onRouteRequest(
    requireSsl: Boolean, request: RequestHeader, sslRouter: SslRouter): Option[Handler] = {
    val uri = request.uri
    if (requireSsl && !uri.startsWith("https")) {
      Some(sslRouter.onRedirectRequired(request, "https" + uri.substring(4)))
    } else {
      sslRouter.onAllowed(request)
    }
  }
}

trait SslRouter {
  def onAllowed(request: RequestHeader): Option[Handler]
  def onRedirectRequired(request: RequestHeader, url: String): Handler
}