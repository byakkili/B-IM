package com.github.byakkili.bim.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.IORuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author Guannian Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER;

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
     * @param clazz   类型
     * @return 对象
     */
    public static <T> T parse(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
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
     * @param clazz 类型
     * @return 对象
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
    }
}
