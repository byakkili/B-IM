package com.github.byakkili.bim.core.util;

import cn.hutool.core.util.ReflectUtil;
import com.google.protobuf.Message;
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
    /**
     * 方法缓存
     */
    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * 序列化
     *
     * @param messageLite Protobuf对象
     * @return 字节
     */
    public static byte[] serialize(Message messageLite) {
        return messageLite == null ? null : messageLite.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param clazz Protobuf对象类型
     * @param <T>   Protobuf对象类型
     * @return Protobuf对象
     */
    @SuppressWarnings("PrimitiveArrayArgumentToVarargsMethod")
    public static <T extends Message> T deserialize(byte[] bytes, Class<T> clazz) {
        Method method = getParseFormMethod(clazz);
        if (method == null) {
            return null;
        }
        return ReflectUtil.invokeStatic(method, bytes);
    }

    /**
     * 获取parseForm静态方法
     *
     * @param clazz Protobuf对象类型
     * @param <T>   Protobuf对象类型
     * @return parseForm方法
     */
    private static <T extends Message> Method getParseFormMethod(Class<T> clazz) {
        String className = clazz.getName();
        Method method = METHOD_CACHE.get(className);
        if (method == null) {
            method = ReflectUtil.getPublicMethod(clazz, "parseFrom", byte[].class);
            Method previousValue = METHOD_CACHE.putIfAbsent(className, method);
            if (previousValue != null) {
                method = previousValue;
            }
        }
        return method;
    }
}
