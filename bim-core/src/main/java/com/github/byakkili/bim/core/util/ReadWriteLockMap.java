package com.github.byakkili.bim.core.util;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Guannian Li
 */
public class ReadWriteLockMap<K, V> implements Map<K, V> {
    /**
     * 锁
     */
    private final @Getter ReadWriteLock lock = new ReentrantReadWriteLock();
    private final @Getter Lock readLock = lock.readLock();
    private final @Getter Lock writeLock = lock.writeLock();
    /**
     * 委托对象
     */
    private final Map<K, V> delegate = new HashMap<>();

    @Override
    public int size() {
        readLock.lock();
        try {
            return delegate.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return delegate.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        readLock.lock();
        try {
            return delegate.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        readLock.lock();
        try {
            return delegate.containsValue(value);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        readLock.lock();
        try {
            return delegate.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        writeLock.lock();
        try {
            return delegate.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        writeLock.lock();
        try {
            return delegate.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        writeLock.lock();
        try {
            delegate.putAll(m);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            delegate.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        readLock.lock();
        try {
            return delegate.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        writeLock.lock();
        try {
            return delegate.putIfAbsent(key, value);
        } finally {
            writeLock.unlock();
        }
    }
}
