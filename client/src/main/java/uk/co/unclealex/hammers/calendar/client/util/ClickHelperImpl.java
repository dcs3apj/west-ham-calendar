/**
 * Copyright 2011 Alex Jones
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
package uk.co.unclealex.hammers.calendar.client.util;

import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * @author aj016368
 *
 */
public class ClickHelperImpl implements ClickHelper {

  @Override
  public void clickOnReturnKeyPressed(Object source, final Button target) {
    if (source instanceof HasKeyPressHandlers) {
      KeyPressHandler handler = new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
          if (event.getCharCode() == KeyCodes.KEY_ENTER) {
            target.click();
          }
        }
      };
      ((HasKeyPressHandlers) source).addKeyPressHandler(handler);
    }
  }

}
