/**
 * 
 */
package uk.co.unclealex.hammers.calendar.client.util;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

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
public abstract class ValueListBoxAdaptor<T> implements IsWidget, HasValue<T> {

	private final ListBox i_listBox;
	
	public ValueListBoxAdaptor(ListBox listBox) {
		super();
		i_listBox = listBox;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> handler) {
		ChangeHandler changeHandler = new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ValueChangeEvent<T> vce = new ValueChangeEvent<T>(getValue()) {};
				handler.onValueChange(vce);
			}
		};
		return getListBox().addChangeHandler(changeHandler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		getListBox().fireEvent(event);
	}

	@Override
	public T getValue() {
		ListBox listBox = getListBox();
		int idx = listBox.getSelectedIndex();
		String value = listBox.getValue(idx);
		return parse(value);
	}

	
	protected abstract T parse(String value);

	protected abstract String toString(T value);
	
	protected abstract String toDisplayableString(T value);

	@Override
	public void setValue(T value) {
		String text = toString(value);
		boolean found = false;
		int itemCount = getListBox().getItemCount();
		for (int idx = 0; !found && idx < itemCount; idx++) {
			if (text.equals(getListBox().getValue(idx))) {
				found = true;
				getListBox().setSelectedIndex(idx);
			}
		}
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		setValue(value);
	}

	public void clear() {
		getListBox().clear();
	}
	
	public void addValue(T value) {
		getListBox().addItem(toDisplayableString(value), toString(value));
	}
	
	@Override
	public Widget asWidget() {
		return getListBox();
	}

	public ListBox getListBox() {
		return i_listBox;
	}

}
