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

import org.fcrepo.services.Policy;
import org.fcrepo.services.StoragePolicy;

import java.util.List;

/* TODO NOTE wrapper service class that manages policy storage decisions.
 * implementation needs to be fleshed out & contract with org.fcrepo.binary
 *  established.
 * 
 */

public class PolicyDecisionService {

    /**
     * 
     */
    private final StoragePolicy storagePolicyDecisionPoint =
            new PolicyDecisionPoint();

    final private static PolicyDecisionService service =
            new PolicyDecisionService();

    /**
     * @param policy
     */
    public void add(final Policy policy) {
        storagePolicyDecisionPoint.addPolicy(policy);
    }

    /**
     * @return
     */
    public List<Policy> getActivePolicyTypes() {
        return null; // org.fcrepo.binary.pdp contract doesn't allow yet..
    }

    /**
     * @return
     */
    public static PolicyDecisionService getInstance() {
        return service;
    }

}