package org.acme.retrieveusername.filter;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import org.acme.retrieveusername.filter.annotation.UsernameFilter;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Context;

@Singleton
public class RetrieveUsernameFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveUsernameFilter.class);

    private final HttpServerRequest httpServerRequest;

    public RetrieveUsernameFilter(@Context HttpServerRequest httpServerRequest) {
        this.httpServerRequest = httpServerRequest;
    }

    @ServerRequestFilter(priority = Priorities.AUTHENTICATION + 2)
    @UsernameFilter
    public Uni<Void> payloadFilter(ResteasyReactiveContainerRequestContext requestContext) {
        LOGGER.info("Trying to read payload in jax-rs filter");

        return Uni.createFrom()
                .emitter(uniEmitter ->
                        httpServerRequest.body()
                                .onComplete(bufferAsyncResult -> {
                                    if (bufferAsyncResult.failed()) {
                                        uniEmitter.complete("");
                                    } else {
                                        uniEmitter.complete(bufferAsyncResult.result().toString());
                                    }
                                })
                )
                .replaceWithVoid();
    }
}
