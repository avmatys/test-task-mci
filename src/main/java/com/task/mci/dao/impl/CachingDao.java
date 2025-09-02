package com.task.mci.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.task.mci.dao.CrudDao;

public class CachingDao<T, ID> implements CrudDao<T, ID> {

    private final CrudDao<T, ID> delegate;
    private final Function<T, ID> keyExtractor;
    private volatile Map<ID, T> cache;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public CachingDao(CrudDao<T, ID> delegate, Function<T, ID> keyExtractor) {
        this.delegate = delegate;
        this.keyExtractor = keyExtractor;
    }

    @Override
    public List<T> findAll() throws SQLException {
        lock.readLock().lock();
        try {
            if (cache != null) {
                return new ArrayList<>(cache.values());
            }
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            if (cache == null) {
                List<T> all = delegate.findAll();
                cache = Collections.unmodifiableMap(
                    all.stream().collect(Collectors.toMap(keyExtractor, Function.identity()))
                );
            }
            return new ArrayList<>(cache.values());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public T findById(ID id) throws SQLException {
        findAll();
        return cache.get(id);
    }

    @Override
    public T insert(T entity) throws SQLException {
        T created = delegate.insert(entity);
        invalidateCache();
        return created;
    }

    private void invalidateCache() {
        lock.writeLock().lock();
        try {
            cache = null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
