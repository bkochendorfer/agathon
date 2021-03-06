/*
 * Copyright 2014 BrightTag, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.brighttag.agathon.cassandra;

import java.net.InetAddress;

import com.google.common.collect.ImmutableList;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author codyaray
 * @since 5/23/12
 */
public class AgathonSeedProviderTest extends EasyMockSupport {

  private static final String RING_NAME = "ring";
  private static final String SEED1 = "host1";
  private static final String SEED2 = "host2";
  private static final String SEED3 = "host3";

  private AgathonConnector connector;
  private AgathonSeedProvider provider;

  @Before
  public void setUp() {
    connector = createMock(AgathonConnector.class);
    provider = createMockBuilder(AgathonSeedProvider.class)
        .withConstructor(connector, RING_NAME)
        .addMockedMethod("getInetAddress", String.class)
        .createMock();
  }

  @After
  public void tearDown() {
    verifyAll();
  }

  @Test
  public void getSeeds() throws Exception {
    InetAddress addr1 = createMock(InetAddress.class);
    InetAddress addr2 = createMock(InetAddress.class);
    InetAddress addr3 = createMock(InetAddress.class);
    expect(connector.getSeeds(RING_NAME)).andReturn(ImmutableList.of(SEED1, SEED2, SEED3));
    expect(provider.getInetAddress(SEED1)).andReturn(addr1);
    expect(provider.getInetAddress(SEED2)).andReturn(addr2);
    expect(provider.getInetAddress(SEED3)).andReturn(addr3);
    replayAll();

    assertEquals(ImmutableList.of(addr1, addr2, addr3), provider.getSeeds());
  }

  @Test
  public void getSeeds_unknownHost() throws Exception {
    InetAddress addr = createMock(InetAddress.class);
    expect(connector.getSeeds(RING_NAME)).andReturn(ImmutableList.of(SEED1, SEED2));
    expect(provider.getInetAddress(SEED1)).andReturn(addr);
    expect(provider.getInetAddress(SEED2)).andReturn(null);
    replayAll();

    assertEquals(ImmutableList.of(addr), provider.getSeeds());
  }

  @Test
  public void getSeeds_configurationException() throws Exception {
    final ConfigurationException exception = new ConfigurationException("misconfigured");
    expect(connector.getSeeds(RING_NAME)).andThrow(exception);
    replayAll();

    try {
      provider.getSeeds();
      fail("Expected a RuntimeException");
    } catch (RuntimeException e) {
      // Ok to catch RuntimeException here
      assertEquals(exception, e);
    }
  }

}
