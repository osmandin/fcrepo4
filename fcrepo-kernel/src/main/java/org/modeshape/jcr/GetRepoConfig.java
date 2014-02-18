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
package org.modeshape.jcr;

import com.google.common.base.Function;
import org.infinispan.schematic.document.Document;

import javax.jcr.Repository;

/**
 * Retrieve the configuration from a running Modeshape Repository
 * @author Osman Din
 * @date Feb 18, 2014
 */
public class GetRepoConfig implements Function<Repository, Document> {

    /**
     * @param input the running ModeShape javax.jcr.Repository
     * @return a org.infinispan.schematic.document.Document representing the runtime configuration
     */
    @Override
    public Document apply(final Repository input) {
        assert (input != null);
        assert ((JcrRepository) input).getConfiguration().getDocument() != null;
        Document config =  ((JcrRepository) input).getConfiguration().getDocument();
        assert (config != null);
        return config;
    }

}
