package com.github.byakkili.bim.core.util;

import cn.hutool.core.io.IORuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

/**
 * @author Guannian Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * JSON序列化
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String stringify(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * JSON反序列化
     *
     * @param jsonStr JSON字符串
     * @return Map对象
     */
    public static Map<?, ?> parseMap(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 对象转字节
     *
     * @param object 对象
     * @return 字节
     */
    public static byte[] serialize(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 字节转对象
     *
     * @param bytes 字节
     * @return Map对象
     */
    public static Map<?, ?> deserialize(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readValue(bytes, Map.class);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
