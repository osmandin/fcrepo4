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

import org.infinispan.schematic.document.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring-test/repo.xml" })
public class RunningRepositoryConfigIT {

    @Inject
    javax.jcr.Repository repo;

    @Test
    public void testApply() {
        GetRepoConfig getRepoConfig = new GetRepoConfig();
        Document document = getRepoConfig.apply(repo);
        assert(repo != null);
        assert(document != null);
    }

    @Test
    public void getReadOnlyConnectorAsDocumentList() {
        assertNotNull("Repo null", repo);
        GetRepoConfig getRepoConfig = new GetRepoConfig();
        Document document = getRepoConfig.apply(repo);
        assertNotNull("Document representing config null", document);
        List<Document> list = new ArrayList();
        final Document externalSources = document.getDocument(RepositoryConfiguration.FieldName.EXTERNAL_SOURCES);
        Set<String> set = externalSources.keySet();
        for (String s: set) {
            Document subDoc = externalSources.getDocument(s);
            if (subDoc.getBoolean("readonly") == true) {
                list.add(subDoc);
            }
        }
        assert(list.size() == 2);

        for (Document sourcesDocument : list) {
            assert(sourcesDocument.getBoolean("readonly"));
        }
    }


}
