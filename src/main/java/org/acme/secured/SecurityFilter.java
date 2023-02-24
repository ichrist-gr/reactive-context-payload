package org.acme.secured;

import org.acme.secured.model.CustomPrincipal;
import org.acme.secured.model.CustomSecurityContext;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestContext;

import javax.inject.Singleton;
import javax.ws.rs.Priorities;

@Singleton
public class SecurityFilter {

    @ServerRequestFilter(priority = Priorities.AUTHENTICATION)
    public void securityFilter(ResteasyReactiveContainerRequestContext requestContext) {
        requestContext.setSecurityContext(new CustomSecurityContext(new CustomPrincipal()));
    }
}
