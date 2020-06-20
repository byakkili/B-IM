package com.github.byakkili.bim.core.maintain;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import com.github.byakkili.bim.core.util.ReadWriteLockMap;
import lombok.Getter;

import java.util.Set;

/**
 * @author Guannian Li
 */
@Getter
public abstract class BaseObjectBind<T> {
    /**
     * key, set
     */
    private ReadWriteLockMap<String, Set<T>> strSetMap = new ReadWriteLockMap<>();

    /**
     * 绑定
     *
     * @param key    键
     * @param object 对象
     * @return 是否绑定成功
     */
    protected boolean bind(String key, T object) {
        if (StrUtil.isBlank(key)) {
            return false;
        }
        Set<T> objects = strSetMap.get(key);
        if (objects == null) {
            objects = new ConcurrentHashSet<>();
            Set<T> previousValue = strSetMap.putIfAbsent(key, objects);
            if (previousValue != null) {
                objects = previousValue;
            }
        }
        return objects.add(object);
    }

    /**
     * 解绑
     *
     * @param key    键
     * @param object 对象
     * @return 是否解绑成功
     */
    protected boolean unbind(String key, T object) {
        if (StrUtil.isBlank(key)) {
            return false;
        }
        strSetMap.getWriteLock().lock();
        try {
            Set<T> objects = strSetMap.get(key);
            if (objects != null && objects.remove(object)) {
                if (objects.isEmpty()) {
                    strSetMap.remove(key);
                }
                return true;
            }
        } finally {
            strSetMap.getWriteLock().unlock();
        }
        return false;
    }
}
