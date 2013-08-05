/**
 * Copyright 2013 DuraSpace, Inc.
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

package org.fcrepo.storage.http;

import org.fcrepo.auth.oauth.api.AuthzEndpoint;
import org.fcrepo.binary.MimeTypePolicy;
import org.fcrepo.binary.Policy;
import org.fcrepo.services.NodeService;
import org.fcrepo.session.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PolicyGeneratorTest {
	@Mock
	private HttpServletRequest mockRequest;

	@Mock
	private SessionFactory mockSessions;

	@Mock
	private Session mockSession;

	@Mock
	private Node mockRootNode;

	@Mock
	private Node mockCodeNode;

	private AuthzEndpoint testObj;

	@Mock
	NodeService mockNodeService;

	@Before
	public void setup() throws RepositoryException {
		initMocks(this);
		when(mockSessions.getSession()).thenReturn(mockSession);
		when(
				mockNodeService.findOrCreateNode(mockSession,
						"/fedora:system/fedora:storage_policy", null))
				.thenReturn(mockCodeNode);
		Property property = mock(Property.class);
		when(property.getString()).thenReturn("image/tiff");
		when(mockCodeNode.getProperty("image/tiff")).thenReturn(property);
	}

	@Test
	public void nodeCreated() throws Exception {
		mockSession = mockSessions.getSession();
		Node node = mockNodeService.findOrCreateNode(mockSession,
				"/fedora:system/fedora:storage_policy", null);
		assertEquals(mockCodeNode.getProperty("image/tiff").getString(),
				"image/tiff");
	}

	@Test
	public void testHintsByPolicyType() throws Exception {
		PolicyGenerator obj = new PolicyGenerator();
		Response actual = obj.getHintsByPolicyType("image/tiff");
		assertEquals(actual.getStatus(), 200);
	}

	@Test
	public void getPolicyType() throws PolicyTypeException {
		PolicyGenerator obj = new PolicyGenerator();
		Policy type = obj.getPolicyType("mix:mimeType", "image/tiff", null);
		assertEquals(type.getClass(), MimeTypePolicy.class);
	}
}