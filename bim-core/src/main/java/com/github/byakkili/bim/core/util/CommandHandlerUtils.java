package com.github.byakkili.bim.core.util;

import cn.hutool.core.util.ReflectUtil;
import com.github.byakkili.bim.core.command.CommandHandler;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Guannian Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandHandlerUtils {

    private static final Map<Class<?>, Class<?>> CACHE = new ConcurrentHashMap<>();

    private static final Method FIND0_METHOD = ReflectUtil.getMethod(TypeParameterMatcher.class, "find0", Object.class, Class.class, String.class);

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getMsgClass(final CommandHandler object) {
        Class<? extends CommandHandler> handlerClass = object.getClass();
        Class<?> msgClass = CACHE.get(handlerClass);
        if (msgClass == null) {
            msgClass = ReflectUtil.invokeStatic(FIND0_METHOD, object, CommandHandler.class, "I");

            Class<?> prevClass = CACHE.putIfAbsent(handlerClass, msgClass);
            if (prevClass != null) {
                msgClass = prevClass;
            }
        }
        return (Class<T>) msgClass;
    }
}
