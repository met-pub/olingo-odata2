/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package org.apache.olingo.odata2.spring;

import static org.junit.Assert.*;

import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.apache.olingo.odata2.core.rest.ODataExceptionMapperImpl;
import org.apache.olingo.odata2.core.rest.app.ODataApplication;
import org.apache.olingo.odata2.core.rest.spring.ODataRootLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@WebAppConfiguration
public class SpringNamespaceHandlerTest {

  @Autowired
  private ApplicationContext appCtx;

  @Test
  public void testSuccessfullyCreated() {
    assertTrue(appCtx.containsBean("testServer"));

    assertTrue(appCtx.containsBean("OlingoODataExceptionHandler"));
    assertTrue(appCtx.containsBean("OlingoODataProvider"));

    assertEquals(ODataExceptionMapperImpl.class, appCtx.getType("OlingoODataExceptionHandler"));
    assertEquals(ODataApplication.MyProvider.class, appCtx.getType("OlingoODataProvider"));

    String rootLocatorName = "OlingoODataRootLocator-testServer-serviceFactory";
    assertTrue(appCtx.containsBean(rootLocatorName));
    assertEquals(ODataRootLocator.class, appCtx.getType(rootLocatorName));

    SpringJAXRSServerFactoryBean server = appCtx.getBean("testServer", SpringJAXRSServerFactoryBean.class);
    assertEquals("/service.svc", server.getAddress());
  }

  @Test
  public void testCorrectFactoryAndPathSplit() {
    String rootLocatorName = "OlingoODataRootLocator-testServer-serviceFactory";
    ODataRootLocator rootLocator = appCtx.getBean(rootLocatorName, ODataRootLocator.class);
    assertNotNull(rootLocator.getServiceFactory());
    assertSame(appCtx.getBean("serviceFactory"), rootLocator.getServiceFactory());
    assertEquals(3, rootLocator.getPathSplit());
  }
}
