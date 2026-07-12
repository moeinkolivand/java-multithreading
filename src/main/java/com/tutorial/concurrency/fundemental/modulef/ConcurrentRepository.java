package com.tutorial.concurrency.fundemental.modulef;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRepository<T, ID> {
    /**
     * This repository is backed by a ConcurrentHashMap, making it safely usable
     * by multiple threads without external synchronization.
     *
     * Contrast this with a plain HashMap:
     * - A plain HashMap under concurrent modification can corrupt its internal
     *   linked-list/tree structures, leading to infinite loops (in JDK 7),
     *   lost put() operations, or NullPointerExceptions during resizing.
     * - Even if a plain HashMap appears to work in simple tests, it provides
     *   NO memory visibility guarantees between threads.
     *
     * ConcurrentHashMap avoids these issues by:
     * - Using fine-grained locking (or CAS operations for specific nodes)
     *   to protect internal structure during modifications.
     * - Guaranteeing that reads see the latest completed writes
     *   (happens-before relationship for put/get).
     * - Allowing safe concurrent iteration without throwing
     *   ConcurrentModificationException (though iteration may not see all
     *   concurrent updates).
     */
    private final ConcurrentHashMap<ID, T> concurrentHashMap = new ConcurrentHashMap<>();

    public void save(ID id, T entity) {
        concurrentHashMap.put(id, entity);
    }
    public T findById(ID id) {return concurrentHashMap.get(id);}
}
