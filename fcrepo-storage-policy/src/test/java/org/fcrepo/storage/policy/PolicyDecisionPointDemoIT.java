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
package org.fcrepo.storage.policy;

import java.io.ByteArrayInputStream;

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.fcrepo.storage.policy.MimeTypePolicy;
import org.fcrepo.storage.policy.PolicyDecisionPoint;
//import org.fcrepo.integration.AbstractIT;
import org.fcrepo.kernel.services.DatastreamService;
import org.fcrepo.kernel.services.ObjectService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({"/spring-test/repo.xml"})
public class PolicyDecisionPointDemoIT extends AbstractIT {

    @Inject
    Repository repo;

    @Inject
    DatastreamService datastreamService;

    @Inject
    ObjectService objectService;

    @Test
    public void shouldDemonstratePolicyDecisionPoints() throws Exception {

        final Session session = repo.login();

        final PolicyDecisionPoint pt = new PolicyDecisionPoint();
        pt.addPolicy(new MimeTypePolicy("image/tiff", "tiff-store"));

        datastreamService.createDatastreamNode(session,
                "/testDatastreamPolicyNode", "application/octet-stream",
                new ByteArrayInputStream("asdf".getBytes()));

        datastreamService.createDatastreamNode(session,
                "/testDatastreamPolicyNode", "image/tiff",
                new ByteArrayInputStream("1234".getBytes()));

        session.save();

    }
}
