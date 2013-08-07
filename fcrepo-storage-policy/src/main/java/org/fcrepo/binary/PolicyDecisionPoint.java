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

package org.fcrepo.binary;

import static org.slf4j.LoggerFactory.getLogger;

import org.fcrepo.services.Policy;
import org.fcrepo.services.StoragePolicyDecisionPoint;
import org.slf4j.Logger;

import javax.jcr.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Service that evaluates a set of storage policies for an object and provides
 * storage hints for a binary stream
 * 
 * @author cbeer
 * @date Apr 25, 2013
 */
public class PolicyDecisionPoint implements StoragePolicyDecisionPoint {

    private static final Logger LOGGER = getLogger(MimeTypePolicy.class);

    private List<Policy> policies;

    /**
     * Initialize the policy storage machinery
     */
    public PolicyDecisionPoint() {
        LOGGER.debug("Initializing binary PolicyDecisionPoint");
        policies = new ArrayList<Policy>();
    }

    /*
     * (non-Javadoc)
     * @see org.fcrepo.binary.StoragePolicy#addPolicy(org.fcrepo.binary.Policy)
     */
    @Override
    public void addPolicy(final Policy p) {
        policies.add(p);
    }

    /*
     * (non-Javadoc)
     * @see org.fcrepo.binary.StoragePolicy#evaluatePolicies(javax.jcr.Node)
     */
    @Override
    public String evaluatePolicies(final Node n) {
        for (final Policy p : policies) {
            final String h = p.evaluatePolicy(n);
            if (h != null) {
                return h;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.fcrepo.binary.StoragePolicy#addPolicy(org.fcrepo.binary.Policy)
     */
    @Override
    public void removePolicy(final Policy p) {
        policies.remove(p);
    }

    /*
     * (non-Javadoc)
     * @see org.fcrepo.binary.StoragePolicy#setPolicies(java.util.List)
     */
    @Override
    public void setPolicies(final List<Policy> policies) {
        LOGGER.debug("Adding policies to " + "PolicyDecisionPoint: {}",
            policies.toString());
        this.policies = policies;
    }

    @Override
    public String toString() {
        return "PolicyDecisionPoint [policies=" + policies + "]";
    }
}
