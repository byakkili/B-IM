package com.github.byakkili.bim.core.util;

import cn.hutool.core.util.ReflectUtil;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Guannian Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProtobufUtils {

    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();

    public static byte[] serialize(MessageLite messageLite) {
        return messageLite == null ? null : messageLite.toByteArray();
    }

    @SuppressWarnings("PrimitiveArrayArgumentToVarargsMethod")
    public static <T extends Message> T deserialize(byte[] bytes, Class<T> requestClass) {
        @SuppressWarnings("unchecked")
        Class<Message> clazz = (Class<Message>) requestClass;
        Method method = getParseFormMethod(clazz);
        if (method == null) {
            return null;
        }
        return ReflectUtil.invoke(null, method, bytes);
    }


    private static Method getParseFormMethod(Class<Message> clazz) {
        String className = clazz.getName();
        Method method = METHOD_CACHE.get(className);
        if (method == null) {
            method = ReflectUtil.getMethod(clazz, "parseFrom", byte[].class);
            Method previousValue = METHOD_CACHE.putIfAbsent(className, method);
            if (previousValue != null) {
                method = previousValue;
            }
        }
        return method;
    }
}
