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
package org.fcrepo.kernel.spring;

import org.infinispan.schematic.document.Document;
import org.modeshape.connector.filesystem.FileSystemConnector;
import org.modeshape.jcr.GetRepoConfig;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.jcr.federation.spi.Connector;

import javax.jcr.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Intended (ultimately) as a wrapper around (the?) underlying running javax.jcr.Repository config elements.
 * Note: could be injected or use repo inject itself (instead of passing in methods)
 *
 * @author Osman Din
 * @date Feb 18, 2014
 * @see org.fcrepo.kernel.services.functions.GetClusterConfiguration (ispn config is instantiated as java.util.Map)
 *
 */
public class RunningRepositoryConfig {

    // (A)

     /**
     * This helper method returns read-only configured connectors list.
     * Note: Could be replaced by some Iterator tied to some class representing externalSources (or the whole thing)
     * @param repo javax.jcr.Repository
     * @return list of Document representing org.modeshape.jcr.federation.spi.Connector objects.
     */
    public List<Document> getReadOnlyConnectorsConfiguration(final Repository repo) {
        GetRepoConfig getRepoConfig = new GetRepoConfig();

        checkNotNull(repo, "Null repository supplied");

        final Document document = getRepoConfig.apply(repo);

        final List<Document> list = new ArrayList();
        final Document externalSources = document.getDocument(RepositoryConfiguration.FieldName.EXTERNAL_SOURCES);
        Set<String> set = externalSources.keySet();
        for (String key: set) {
            final Document sourcesElement = externalSources.getDocument(key);
            if (sourcesElement.getBoolean("readonly")) {
                list.add(sourcesElement);
            }
        }
        return list;
    }

    // (B)

    /**
     * This helper method returns read-only configured connectors list of config data structure.
     * Note: Could return an iterator
     * @param repo javax.jcr.Repository
     * @return list of Document representing org.modeshape.jcr.federation.spi.Connector objects.
     */
    public List<ExternalSources> getReadOnlyConfigExternalSources(final Repository repo) {
        GetRepoConfig getRepoConfig = new GetRepoConfig();

        checkNotNull(repo, "Null repository supplied");

        final Document document = getRepoConfig.apply(repo);
        final List<ExternalSources> list = new ArrayList();
        final Document externalSources = document.getDocument(RepositoryConfiguration.FieldName.EXTERNAL_SOURCES);
        final Set<String> set = externalSources.keySet();
        for (String key: set) {
            Document sourcesElement = externalSources.getDocument(key);
            if (sourcesElement.getBoolean("readonly")) {
                list.add(new ExternalSources(FileSystemConnector.class, "home-directory",
                        true, "dir", "projections", true)); //TODO replace with actual read values
            }
        }
        return list;
    }

    /**
     * Helper data structure representing external sources element.
     * Note: maintenance headache if underlying abstractions change.
     *
     * home-directory" : {
     *   classname" : "org.modeshape.connector.filesystem.FileSystemConnector",
     *   directoryPath" : "/tmp",
     *   readonly" : true,
     *   projections" : [ "default:/tmp => /" ],
     *   addMimeTypeMixin" : true
     *  }, . . .
     */
    class ExternalSources {
        Class<? extends Connector> classname;
        String identifier;
        boolean readonly;
        String directoryPath;
        String projections;
        boolean addMimeTypeMixin;

        public ExternalSources(Class<? extends Connector> classname, String identifier, boolean readonly,
                               String directoryPath, String projections, boolean addMimeTypeMixin) {
            this.classname = classname;
            this.identifier = identifier;
            this.readonly = readonly;
            this.directoryPath = directoryPath;
            this.projections = projections;
            this.addMimeTypeMixin = addMimeTypeMixin;
        }
    }
}
