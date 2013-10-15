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

package org.fcrepo.kernel.utils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Sets various boot time properties if they are not set and if integration
 * tests are not running
 * 
 * @author osmandin
 * @date Oct 15, 2013
 */
public class BootPropertyLoader {

    private static final Logger LOGGER = getLogger(BootPropertyLoader.class);
    /**
     * Init
     */
    @PostConstruct
    public void init() {
        final String dir = System.getProperty("user.home") + System.getProperty("file.separator") + "fcrepo4-datum";
        LOGGER.debug("Changing default write directory to test directory :" + dir);
        if (System.getProperty("com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean.default.objectStoreDir") == null) {
            //LOGGER.debug("Set Property : com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean.default.objectStoreDir");
            System.setProperty("com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean.default.objectStoreDir", dir);
        }
        if (System.getProperty("com.arjuna.ats.arjuna.objectstore.objectStoreDir") == null) {
            System.setProperty("com.arjuna.ats.arjuna.objectstore.objectStoreDir", dir);
        }
        if (System.getProperty("fcrepo.ispn.CacheDirPath") == null) {
            //LOGGER.debug("set fcrepo.ispn.cachedirpath");
            System.setProperty("fcrepo.ispn.CacheDirPath", dir);
        }
        if (System.getProperty("fcrepo.ispn.binary.CacheDirPath") == null) {
            //LOGGER.debug("set fcrepo.ispn.binary.CacheDirPath");
            System.setProperty("fcrepo.ispn.binary.CacheDirPath", dir);
        }
        if (System.getProperty("fcrepo.modeshape.index.location") == null) {
            //LOGGER.debug("Set fcrepo.modeshape.index.locatoin");
            System.setProperty("fcrepo.modeshape.index.location", dir);
        }
        if (System.getProperty("fcrepo.ispn.alternative.CacheDirPath") == null) {
            //LOGGER.debug("Set fcrepo.ispn.alternative.CachedirPath");
            System.setProperty("fcrepo.ispn.alternative.CacheDirPath", dir);
        }
        if (System.getProperty("fcrepo.ispn.repo.CacheDirPath") == null) {
            //LOGGER.debug("set fcrepo.ispn.repo.cachedirpath");
            System.setProperty("fcrepo.ispn.repo.CacheDirPath", dir);
        }
        if (System.getProperty("fcrep.activemq.dir") == null) {
            System.setProperty("fcrep.activemq.dir", dir);
        }
    }
}
