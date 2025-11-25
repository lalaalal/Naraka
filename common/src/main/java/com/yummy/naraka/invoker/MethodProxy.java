package com.yummy.naraka.invoker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a static method with {@link MethodProxy}
 *
 * @see MethodInvoker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodProxy {
    Class<?> value();

    boolean allowDelayedCall() default false;
}
