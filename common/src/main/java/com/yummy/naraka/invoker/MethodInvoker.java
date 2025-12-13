package com.yummy.naraka.invoker;

import com.yummy.naraka.NarakaMod;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Invoke method in registered class using {@link MethodInvoker#register(Class)}.
 * <pre><code>
 * public class InvokerClass {
 *     ...
 *     MethodInvoker.of(InvokerClass.class, "targetMethod")
 *             .invoke("Parameter");
 *     ...
 * }
 * </code></pre>
 *
 * <pre><code>  @MethodInvoker(InvokerClass.class)
 * public static void targetMethod(String parameter) {
 *     // implementation
 * }
 * </code></pre>
 * <p>
 * Class of targetMethod <b>MUST</b> be registered by {@link MethodInvoker#register(Class)} to search targetMethod.
 *
 * @see MethodInvoker#of(Class, String)
 * @see MethodInvoker#invoke(Object...)
 */
public class MethodInvoker {
    private static final Set<Class<?>> SEARCHING_CLASSES = new HashSet<>();
    private static final Map<String, Method> CACHE = new HashMap<>();
    private static final Set<Method> DELAYED_CALL_ALLOWED = new HashSet<>();

    private final Class<?> invokerType;
    private final String name;
    private boolean withParameterTypes = false;
    private Class<?>[] parameterTypes = new Class[0];
    @Nullable
    private Object result;

    /**
     * Prepare {@link MethodInvoker} for invoker type, target method name.
     * {@linkplain MethodInvoker} searches target method from <b>name only</b> by default.
     * To specify parameter types call {@link MethodInvoker#withParameterTypes(Class[])}.
     * Call {@link MethodInvoker#invoke(Object...)} to invoke.
     *
     * @param invokerType Class invoking method
     * @param name        Target method name
     * @return Returns new invoker
     * @see MethodInvoker#withParameterTypes(Class[])
     * @see MethodInvoker#invoke(Object...)
     * @see MethodInvoker#invoke(Class, String, Object...)
     */
    public static MethodInvoker of(Class<?> invokerType, String name) {
        return new MethodInvoker(invokerType, name);
    }

    private MethodInvoker(Class<?> invokerType, String name) {
        this.invokerType = invokerType;
        this.name = name;
    }

    /**
     * Specify parameter types for target method. Be careful using generic class for parameter type.
     *
     * @param parameterTypes Parameter types
     * @return Returns self
     */
    public MethodInvoker withParameterTypes(Class<?>... parameterTypes) {
        this.withParameterTypes = true;
        this.parameterTypes = parameterTypes;
        return this;
    }

    private Method getMethod() {
        if (withParameterTypes)
            return getProxyMethod(invokerType, name, parameterTypes);
        return getProxyMethod(invokerType, name);
    }

    /**
     * Search method targeting invoker type and having name
     *
     * @param parameters Parameters for target method
     * @return Returns self to be able to get result
     * @see MethodInvoker#withParameterTypes(Class[])
     * @see MethodInvoker#result(Class)
     */
    public MethodInvoker invoke(Object... parameters) {
        Method method = getMethod();
        this.result = invokeMethod(method, parameters);
        return this;
    }

    /**
     * Return result from invoked method. Call after {@link MethodInvoker#invoke(Object...)}.
     * Be careful using generic class for return type.
     *
     * @param returnType Type of return value
     * @param <T>        Type of return value
     * @return Return value from invoked method
     * @throws IllegalStateException If method is not invoked yet or has no return type or return type doesn't match.
     */
    public <T> T result(Class<T> returnType) {
        if (result == null)
            throw new IllegalStateException("Not invoked yet or has no return type");
        if (returnType.isInstance(result))
            return returnType.cast(result);
        throw new IllegalStateException("Return type of target method doesn't match");
    }

    /**
     * Register class search static methods to invoke.
     *
     * @param type Implementing class.
     */
    public static void register(Class<?> type) {
        SEARCHING_CLASSES.add(type);
    }

    private static String getKey(Class<?> type, String methodName) {
        return type.getName() + "#" + methodName;
    }

    private static String getKey(Class<?> type, String methodName, Class<?>[] parameterTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(type.getName()).append("#").append(methodName);
        if (parameterTypes.length == 0)
            return builder.toString();
        builder.append("(");
        for (int index = 0; index < parameterTypes.length; index++) {
            builder.append(parameterTypes[index].getName());
            if (index < parameterTypes.length - 1)
                builder.append(",");
        }
        builder.append(")");
        return builder.toString();
    }

    private static Method getProxyMethod(Class<?> invokerType, String methodName) {
        String identifier = getKey(invokerType, methodName);
        if (CACHE.containsKey(identifier))
            return CACHE.get(identifier);

        for (Class<?> type : SEARCHING_CLASSES) {
            final Method method = searchTargetMethod(type.getMethods(), invokerType, methodName);
            if (method != null)
                return CACHE.computeIfAbsent(identifier, key -> method);
        }
        throw new IllegalStateException("Cannot find any proxy method for type " + invokerType.getName());
    }

    private static Method getProxyMethod(Class<?> invokerType, String methodName, Class<?>[] parameterTypes) {
        String identifier = getKey(invokerType, methodName, parameterTypes);
        if (CACHE.containsKey(identifier))
            return CACHE.get(identifier);

        for (Class<?> type : SEARCHING_CLASSES) {
            try {
                final Method method = type.getMethod(methodName, parameterTypes);
                if (checkAnnotation(method, invokerType))
                    return CACHE.computeIfAbsent(identifier, key -> method);
            } catch (NoSuchMethodException ignored) {

            }
        }
        throw new IllegalStateException("Cannot find any proxy method for type " + invokerType.getName());
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
        if (methodProxy == null)
            return false;
        boolean matches = methodProxy.value().equals(type);
        if (matches && methodProxy.allowDelayedCall())
            DELAYED_CALL_ALLOWED.add(method);
        return matches;
    }

    /**
     * Invoke proxy method annotated with {@link MethodProxy} from registered classes<br>
     * Only {@link MethodProxy#value()} equals invoker class
     *
     * @param invokerType Class of invoker method
     * @param name        Proxy method name
     * @param parameters  Parameters
     * @throws RuntimeException If illegal access or proxy method throws some exception
     * @see MethodInvoker#register(Class)
     */
    public static void invoke(Class<?> invokerType, String name, Object... parameters) {
        of(invokerType, name).invoke(parameters);
    }

    private static Object invokeMethod(Method method, Object... parameters) {
        try {
            if (NarakaMod.isModLoaded && !DELAYED_CALL_ALLOWED.contains(method))
                NarakaMod.LOGGER.warn("Using method invoker after mod loaded ({})", method.getName());
            return method.invoke(null, parameters);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        } catch (InvocationTargetException exception) {
            NarakaMod.LOGGER.error("An exception occurred during invoking method {}.{}", method.getDeclaringClass().getName(), method.getName());
            throw new RuntimeException(exception.getTargetException());
        }
    }
}
