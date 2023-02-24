package org.acme.retrieveusername.interceptor.processor;

import io.smallrye.mutiny.Uni;
import org.acme.retrieveusername.interceptor.annotation.UsernameInterceptor;
import org.acme.retrieveusername.interceptor.client.AuthorizeClient;
import org.acme.secured.model.CustomPrincipal;
import org.acme.secured.model.CustomSecurityContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.core.ResteasyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@ApplicationScoped
public class RetrieveUsernameProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveUsernameProcessor.class);

    private final SecurityContext securityContext;
    private final AuthorizeClient authorizeClient;
    private final SearchFieldsRetriever searchFieldsRetriever;

    public RetrieveUsernameProcessor(@Context SecurityContext securityContext,
                                     @RestClient AuthorizeClient authorizeClient,
                                     SearchFieldsRetriever searchFieldsRetriever) {
        this.securityContext = securityContext;
        this.authorizeClient = authorizeClient;
        this.searchFieldsRetriever = searchFieldsRetriever;
    }


    public Uni<Object> processSecurityCtx(InvocationContext invocationContext) {
        CustomPrincipal customPrincipal = (CustomPrincipal) securityContext.getUserPrincipal();
        LOGGER.info("Username before retrieval: {}", customPrincipal.getUsername());
        String searchCriteria = getSearchCriteria(invocationContext);
        LOGGER.info("Going to retrieve user's username with criteria: {}", searchCriteria);

        return authorizeClient.getUsername(searchCriteria)
                .invoke(customPrincipal::setUsername)
                .onItem()
                .transform(username -> new CustomSecurityContext(customPrincipal))
                .onItem()
                .transformToUni(securityCtx -> handleSecurityContext(securityCtx, invocationContext));
    }

    private String getSearchCriteria(InvocationContext invocationContext) {
        UsernameInterceptor annotation = invocationContext.getMethod().getAnnotation(UsernameInterceptor.class);
        List<String> searchFields = searchFieldsRetriever.retrieveSearchFields(annotation, invocationContext);

        return String.join(",", searchFields);
    }

    private Uni<Object> handleSecurityContext(SecurityContext securityContext, InvocationContext invocationContext) {
        ResteasyContext.pushContext(SecurityContext.class, securityContext);
        LOGGER.info("Updated security context is now pushed");

        try {
            return (Uni<Object>) invocationContext.proceed();
        } catch (Exception e) {
            throw new RuntimeException("Unable to register the updated security context");
        }
    }
}
