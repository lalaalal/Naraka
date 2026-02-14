package com.yummy.naraka.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Require platform specific invoking
 */
@Target(ElementType.FIELD)
public @interface PlatformInvokeRequired {
}
