package org.acme.resource;

import io.smallrye.mutiny.Uni;
import org.acme.model.User;
import org.acme.retrieveusername.filter.annotation.UsernameFilter;
import org.acme.retrieveusername.interceptor.annotation.UsernameInterceptor;
import org.acme.secured.model.CustomPrincipal;
import org.jboss.resteasy.reactive.RestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/example")
public class SecurityResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityResource.class);

    private final SecurityContext securityContext;

    public SecurityResource(@Context SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @POST
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UsernameFilter
    public Uni<Principal> retrieveUsernameFilter(@RestHeader("source") String source, User user) {
        LOGGER.info("Entering jax-rs endpoint");
        return Uni.createFrom().item(securityContext::getUserPrincipal);
    }

    @POST
    @Path("/interceptor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UsernameInterceptor(payloadFields = {".phoneNumber"}, classType = User.class)
    public Uni<Principal> retrieveUsernameInterceptor(@RestHeader("source") String source, User user) {
        LOGGER.info("Entering jax-rs endpoint");
        CustomPrincipal customPrincipal = (CustomPrincipal) securityContext.getUserPrincipal();
        LOGGER.info("Username after retrieval: {}", customPrincipal.getUsername());

        return Uni.createFrom().item(() -> customPrincipal);
    }
}