package org.acme.retrieveusername.interceptor.annotation;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface UsernameInterceptor {
    @Nonbinding String[] payloadFields() default "";

    @Nonbinding Class<?> classType() default Object.class;
}
