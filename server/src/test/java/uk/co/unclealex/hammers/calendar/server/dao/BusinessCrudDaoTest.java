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

package uk.co.unclealex.hammers.calendar.server.dao;

import java.util.Map.Entry;
import java.util.SortedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

/**
 * @author alex
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/application-contexts/dao/test-hibernate.xml", "/application-contexts/dao/test-db.xml" })
@SuppressWarnings("deprecation")
public class BusinessCrudDaoTest {

	@Autowired
	private ModelDao modelDao;

	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Before
	public void setup() {
		SimpleJdbcTestUtils.deleteFromTables(simpleJdbcTemplate, "model");
	}

	@Test
	public void testInsertNew() {
		ModelBean modelBean = new ModelBean("One", "Two");
		modelDao.saveOrUpdate(modelBean);
		int rowCount = SimpleJdbcTestUtils.countRowsInTable(simpleJdbcTemplate, "model");
		Assert.assertEquals("The model table contained the incorrect amount of rows.", 1, rowCount);
		Assert.assertNotNull("The model's id was not set.", modelBean);
	}

	@Test
	public void testFindById() {
		ModelBean modelBean1 = new ModelBean("One", "Two");
		ModelBean modelBean2 = new ModelBean("Three", "Four");
		modelDao.saveOrUpdate(modelBean1, modelBean2);
		ModelBean storedModelBean = modelDao.findById(modelBean1.getId());
		Assert.assertEquals("The stored model bean had the wrong id.", modelBean1.getId(), storedModelBean.getId());
		Assert.assertEquals("The stored model bean had the wrong business key.", modelBean1.getBusinessKey(),
				storedModelBean.getBusinessKey());
		Assert
				.assertEquals("The stored model bean had the wrong value.", modelBean1.getValue(), storedModelBean.getValue());
	}

	@Test
	public void testUpdate() {
		ModelBean modelBean = new ModelBean("One", "Two");
		modelDao.saveOrUpdate(modelBean);
		modelBean.setValue("Three");
		modelDao.saveOrUpdate(modelBean);
		ModelBean storedModelBean = modelDao.findById(modelBean.getId());
		Assert.assertEquals("The stored model bean had the wrong id.", modelBean.getId(), storedModelBean.getId());
		Assert.assertEquals("The stored model bean had the wrong business key.", modelBean.getBusinessKey(),
				storedModelBean.getBusinessKey());
		Assert.assertEquals("The stored model bean had the wrong value.", modelBean.getValue(), storedModelBean.getValue());
	}

	@Test
	public void testGetAll() {
		ModelBean modelBean1 = new ModelBean("One", "Two");
		ModelBean modelBean2 = new ModelBean("Three", "Four");
		modelDao.saveOrUpdate(modelBean1, modelBean2);
		ModelBean[] actualModelBeans = Iterables.toArray(modelDao.getAll(), ModelBean.class);
		ModelBean[] expectedModelBeans = new ModelBean[] { modelBean1, modelBean2 };
		Assert.assertArrayEquals("The wrong model beans were found.", expectedModelBeans, actualModelBeans);
	}

	@Test
	public void testRemoveById() {
		ModelBean modelBean1 = new ModelBean("One", "Two");
		ModelBean modelBean2 = new ModelBean("Three", "Four");
		modelDao.saveOrUpdate(modelBean1, modelBean2);
		modelDao.remove(modelBean2.getId());
		ModelBean[] actualModelBeans = Iterables.toArray(modelDao.getAll(), ModelBean.class);
		ModelBean[] expectedModelBeans = new ModelBean[] { modelBean1 };
		Assert.assertArrayEquals("The wrong model beans were found.", expectedModelBeans, actualModelBeans);
	}

	@Test
	public void testFindByKey() {
		ModelBean modelBean1 = new ModelBean("One", "Two");
		ModelBean modelBean2 = new ModelBean("Three", "Four");
		modelDao.saveOrUpdate(modelBean1, modelBean2);
		ModelBean storedModelBean = modelDao.findByKey(modelBean1.getBusinessKey());
		Assert.assertEquals("The stored model bean had the wrong id.", modelBean1.getId(), storedModelBean.getId());
		Assert.assertEquals("The stored model bean had the wrong business key.", modelBean1.getBusinessKey(),
				storedModelBean.getBusinessKey());
		Assert
				.assertEquals("The stored model bean had the wrong value.", modelBean1.getValue(), storedModelBean.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllByKey() {
		ModelBean modelBean1 = new ModelBean("One", "Two");
		ModelBean modelBean2 = new ModelBean("Three", "Four");
		modelDao.saveOrUpdate(modelBean1, modelBean2);
		Entry<String, ModelBean>[] actualModelBeans = Iterables.toArray(modelDao.getAllByKey().entrySet(), Entry.class);
		SortedMap<String, ModelBean> expectedModelBeans = Maps.newTreeMap();
		expectedModelBeans.put("One", modelBean1);
		expectedModelBeans.put("Three", modelBean2);
		Assert.assertArrayEquals("The wrong model beans were found.",
				Iterables.toArray(expectedModelBeans.entrySet(), Entry.class), actualModelBeans);
	}

	@Test
	public void testRemoveByKey() {
		ModelBean modelBean1 = new ModelBean("One", "Two");
		ModelBean modelBean2 = new ModelBean("Three", "Four");
		modelDao.saveOrUpdate(modelBean1, modelBean2);
		modelDao.remove(modelBean2.getBusinessKey());
		ModelBean[] actualModelBeans = Iterables.toArray(modelDao.getAll(), ModelBean.class);
		ModelBean[] expectedModelBeans = new ModelBean[] { modelBean1 };
		Assert.assertArrayEquals("The wrong model beans were found.", expectedModelBeans, actualModelBeans);
	}
}
