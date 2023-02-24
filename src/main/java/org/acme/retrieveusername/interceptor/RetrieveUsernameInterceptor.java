package org.acme.retrieveusername.interceptor;

import org.acme.retrieveusername.interceptor.annotation.UsernameInterceptor;
import org.acme.retrieveusername.interceptor.processor.RetrieveUsernameProcessor;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Singleton
@Interceptor
@UsernameInterceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class RetrieveUsernameInterceptor {
    private final RetrieveUsernameProcessor retrieveUsernameProcessor;

    public RetrieveUsernameInterceptor(RetrieveUsernameProcessor retrieveUsernameProcessor) {
        this.retrieveUsernameProcessor = retrieveUsernameProcessor;
    }

    @AroundInvoke
    public Object interceptRequest(InvocationContext invocationContext) {
        return retrieveUsernameProcessor.processSecurityCtx(invocationContext);
    }
}
