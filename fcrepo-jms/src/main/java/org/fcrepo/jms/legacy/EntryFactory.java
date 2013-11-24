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

package org.fcrepo.jms.legacy;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Category;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedInput;

/**
 * Create and parse ATOM events
 */
public abstract class EntryFactory {

    //private static final Abdera ABDERA = new Abdera();

    //private static final Parser ABDERA_PARSER = ABDERA.getParser();

    public static final String FORMAT =
            "info:fedora/fedora-system:ATOM-APIM-1.0";

    // TODO get this out of the build properties
    public static final String SERVER_VERSION = "4.0.0-SNAPSHOT";

    private static final String TYPES_NS =
            "http://www.fedora.info/definitions/1/0/types/";

    public static final String VERSION_PREDICATE =
            "info:fedora/fedora-system:def/view#version";

    private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";

    public static final String FORMAT_PREDICATE =
            "http://www.fedora.info/definitions/1/0/types/formatURI";

    /**
     * Create a new base Abdera document for our ATOM entry
     * @return
     */
    static Entry newEntry() {
        final Entry entry = new Entry();
        /*.
        entry.declareNS(XSD_NS, "xsd");
        entry.declareNS(TYPES_NS, "fedora-types");
        entry.setId("urn:uuid:" + UUID.randomUUID().toString());
        entry.addCategory(FORMAT_PREDICATE, FORMAT, "format");
        entry.addCategory(VERSION_PREDICATE, SERVER_VERSION, "version");
        */
        entry.setId(null);
        entry.setContents(null);
        List<Category> categoryList = new ArrayList<Category>();

        Category d = new Category();
        d.setLabel("format");
        d.setScheme(FORMAT_PREDICATE);
        d.setTerm(FORMAT);
        categoryList.add(d);

        Category e = new Category();
        e.setLabel("version");
        e.setScheme(VERSION_PREDICATE);
        e.setTerm(SERVER_VERSION);
        categoryList.add(e);

        entry.setCategories(categoryList);
        return entry;
    }

    /**
     * Parse an ATOM entry document into ROME
     * @param input
     * @return
     * @throws IOException
     * @throws FeedException
     * @throws IllegalArgumentException
     */
    static Entry parse(Reader input) throws IllegalArgumentException,
            FeedException, IOException {
        WireFeedInput wireFeedInput = new WireFeedInput();
        WireFeed wiredFeed = wireFeedInput.build(input);
        Feed f = (Feed)wiredFeed;//check
        if (f == null) {
            System.out.println("Feed null");
        } else {
            System.out.println(f.toString());
        }

        return (Entry) f.getEntries().get(0);
    }

}
