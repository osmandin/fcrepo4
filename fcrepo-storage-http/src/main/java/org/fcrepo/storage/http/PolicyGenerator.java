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

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import org.fcrepo.AbstractResource;
import org.fcrepo.binary.MimeTypePolicy;
import org.fcrepo.binary.Policy;
import org.fcrepo.session.InjectedSession;
import org.modeshape.jcr.api.JcrTools;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;

@Component
@Scope("prototype")
@Path("/storagepolicy")
public class PolicyGenerator extends AbstractResource {

    @InjectedSession
    protected Session session;

    @Context
    private HttpServletRequest request;

    private static final Logger LOGGER = getLogger(PolicyGenerator.class);

    /**
     * Initialize
     * 
     * @throws RepositoryException
     * @throws IOException
     */
    @PostConstruct
    public void setUpRepositoryConfiguration() throws RepositoryException,
    IOException {
        final JcrTools jcrTools = new JcrTools(true);
        Session session = null;
        try {
            session = sessions.getSession();
            jcrTools.findOrCreateNode(session,
                    "/fedora:system/fedora:storage_policy", null);
            session.save();
            LOGGER.debug("Created configuration node");
        } catch (final Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * POST to store nodeType and hint
     * 
     * @param request For now, follows pattern: mix:mimeType image/tiff
     *        store-hint
     * @return status TODO non JCR abstraction TODO input specification using
     *         PDL, and content under something like
     *         info:fedora/fedora-system:def/internal# TODO property defn type
     *         should allow multiple values
     */

    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    // @Produces(APPLICATION_JSON)
    public
    Response postPropertyType(final String request) throws Exception {

        final JcrTools jcrTools = new JcrTools(true);
        try {
            LOGGER.debug("POST Received request param: {}", request);
            final Node node =
                    jcrTools.findOrCreateNode(session,
                            "/fedora:system/fedora:storage_policy", "test");
            final String[] str = request.split(" "); // simple split for now
            node.setProperty(str[0], new String[] {str[1] + ":" + str[2]});

            // instantiate PolicyType based on ... mimeType->MimeType()
            PolicyDecisionService.getInstance().add(
                    getPolicyType(str[0], str[1], str[2]));
            session.save();
            LOGGER.debug("Saved PDS hint {}", request);
            return Response.ok().build();
        } catch (final Exception e) {
            throw e;
        } finally {
            session.logout();
        }
    }

    /**
     * For nodeType n get org.fcrepo.binary.Policy implementation. (Could use a
     * hashmap etc if all Policy o have minimal behavior).
     * 
     * @param nodeType
     * @param itemType
     * @param value
     * @return
     * @throws PolicyTypeException
     */
    public Policy getPolicyType(final String nodeType, final String itemType,
            final String value) throws PolicyTypeException {

        switch (nodeType) {
            case "mix:mimeType":
                return new MimeTypePolicy(itemType, value);
            default:
                throw new PolicyTypeException("Mapping not found");
        }
    }

    /**
     * No convenience class yet for specifying properties -- e.g. deleting
     * image/tiff, not image/jpeg
     */
    /**
     * Delete org.fcrepo.binary.PolicyType.
     * 
     * @param request
     * @return
     * @throws RepositoryException
     */
    @DELETE
    public Response delete(final String request) throws RepositoryException {
        try {
            LOGGER.debug("Deleting node property{}", request);
            final Node node =
                    jcrTools.findOrCreateNode(session,
                            "/fedora:system/fedora:storage_policy", "test");
            final String[] str = request.split(" ");
            node.getProperty(str[0]).remove();
            session.save();
            return Response.ok().build();
        } finally {
            session.logout();
        }
    }

    /**
     * TODO Future enhancement. Impl. would depend on the contract with
     * org.fcrepo.binary.PolicyDecisionPoint. Print hints by type (e.g.
     * "mimeType: => tiff_store, jpg_store) from JCR
     * 
     * @param policyType
     * @return
     * @throws RepositoryException
     */
    @GET
    @Produces(APPLICATION_JSON)
    public Response getHintsByPolicyType(final String type) throws Exception {
        try {
            // null
            final List<Policy> sb =
                    PolicyDecisionService.getInstance().getActivePolicyTypes();
            // see method impl.
            if (sb == null) {
                return Response.ok(
                        "No mapping yet!" + MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok(sb.toString() + MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (final Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * Verifies whether node type is valid
     * 
     * @param session
     * @param type
     * @return
     * @throws RepositoryException
     */
    public boolean checkPolicyType(final Session session,
            final String type) throws RepositoryException {
        final NodeTypeIterator iterator =
                session.getWorkspace().getNodeTypeManager().getAllNodeTypes();
        while (iterator.hasNext()) {
            if (iterator.next().toString().contains(type)) { // TODO or equals
                return true;
            }
        }
        return false;
    }
}
