package com.github.byakkili.bim.core.util;

import cn.hutool.core.util.ReflectUtil;
import com.github.byakkili.bim.core.cmd.CmdHandler;
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
public class CmdHandlerUtils {

    private static final Map<Class<?>, Class<?>> CACHE = new ConcurrentHashMap<>();

    private static final Method FIND0_METHOD = ReflectUtil.getMethod(TypeParameterMatcher.class, "find0", Object.class, Class.class, String.class);

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getMsgClass(final CmdHandler object) {
        Class<? extends CmdHandler> cmdHandlerClass = object.getClass();
        Class<?> msgClass = CACHE.get(cmdHandlerClass);
        if (msgClass == null) {
            msgClass = ReflectUtil.invokeStatic(FIND0_METHOD, object, CmdHandler.class, "I");

            Class<?> prevClass = CACHE.putIfAbsent(cmdHandlerClass, msgClass);
            if (prevClass != null) {
                msgClass = prevClass;
            }
        }
        return (Class<T>) msgClass;
    }
}
