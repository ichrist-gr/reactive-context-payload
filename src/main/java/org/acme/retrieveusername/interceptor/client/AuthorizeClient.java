package org.acme.retrieveusername.interceptor.client;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Singleton
@RegisterRestClient(configKey = "AuthorizeClient")
public interface AuthorizeClient {
    @GET
    @Path("/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<String> getUsername(@QueryParam("searchCriteria") String searchCriteria);
}
