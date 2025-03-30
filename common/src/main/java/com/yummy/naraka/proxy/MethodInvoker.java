package com.yummy.naraka.proxy;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MethodInvoker {
    private static final Set<Class<?>> SEARCHING_CLASSES = new HashSet<>();
    private static final Map<String, Method> CACHE = new HashMap<>();

    public static void register(Class<?> type) {
        SEARCHING_CLASSES.add(type);
    }

    private static String getKey(Class<?> type, String methodName) {
        return type.getName() + "#" + methodName;
    }

    private static Method getProxyMethod(Class<?> testType, String methodName) {
        String identifier = getKey(testType, methodName);
        if (CACHE.containsKey(identifier))
            return CACHE.get(identifier);

        for (Class<?> type : SEARCHING_CLASSES) {
            final Method method = searchTargetMethod(type.getMethods(), testType, methodName);
            if (method != null)
                return CACHE.computeIfAbsent(identifier, key -> method);
        }
        throw new IllegalStateException("Cannot find any proxy method for type " + testType.getName());
    }

    @Nullable
    private static Method searchTargetMethod(Method[] methods, Class<?> type, String methodName) {
        for (Method method : methods) {
            if (method.getName().equals(methodName) && checkAnnotation(method, type))
                return method;
        }
        return null;
    }

    private static boolean checkAnnotation(Method method, Class<?> type) {
        MethodProxy methodProxy = method.getAnnotation(MethodProxy.class);
        return methodProxy.value().equals(type);
    }

    /**
     * Invoke proxy method annotated with {@link MethodProxy} from registered classes<br>
     * Only {@link MethodProxy#value()} equals invoker class
     *
     * @param type      Class of invoker method
     * @param name      Method name
     * @param arguments Arguments
     * @throws RuntimeException If illegal access or proxy method throws some exception
     * @see MethodInvoker#register(Class)
     */
    public static void invoke(Class<?> type, String name, Object... arguments) {
        Method method = getProxyMethod(type, name);
        invokeMethod(method, arguments);
    }

    /**
     * Invoke proxy method annotated with {@link MethodProxy} from registered classes and return<br>
     * Be careful when using with generic types
     *
     * @param type       Class of invoker method
     * @param name       Method name
     * @param returnType {@link Class} of return value
     * @param arguments  Arguments
     * @param <T>        Type of return value
     * @return Return value from proxy method
     * @throws RuntimeException      If illegal access or proxy method throws some exception
     * @throws IllegalStateException If return type doesn't match
     * @see MethodInvoker#register(Class)
     */
    public static <T> T invoke(Class<?> type, String name, Class<T> returnType, Object... arguments) {
        Method method = getProxyMethod(type, name);
        Object result = invokeMethod(method, arguments);
        if (returnType.isInstance(result))
            return returnType.cast(result);
        throw new IllegalStateException("Return type of target method doesn't match");
    }

    private static Object invokeMethod(Method method, Object... arguments) {
        try {
            return method.invoke(null, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
