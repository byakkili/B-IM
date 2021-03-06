package com.github.byakkili.bim.core.util;

import cn.hutool.core.map.MapUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Guannian Li
 */
public class JsonUtilsTest {
    @Test
    public void stringify() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("username", "admin");
        map.put("password", "12345");

        Assert.assertEquals(JsonUtils.stringify(map), "{\"username\":\"admin\",\"password\":\"12345\"}");
    }

    @Test
    public void parseMap() {
        Map map = JsonUtils.parse("{\"username\":\"admin\",\"password\":\"12345\"}", Map.class);

        Assert.assertEquals(MapUtil.getStr(map, "username"), "admin");
        Assert.assertEquals(MapUtil.getStr(map, "password"), "12345");
    }

    @Test
    public void serialize() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("username", "admin");
        map.put("password", "12345");

        Assert.assertArrayEquals(JsonUtils.serialize(map), JsonUtils.stringify(map).getBytes());
    }

    @Test
    public void deserialize() {
        String jsonStr = "{\"username\":\"admin\",\"password\":\"12345\"}";
        Map map = JsonUtils.deserialize(jsonStr.getBytes(), Map.class);

        Assert.assertEquals(map, JsonUtils.parse(jsonStr, Map.class));
    }
}
