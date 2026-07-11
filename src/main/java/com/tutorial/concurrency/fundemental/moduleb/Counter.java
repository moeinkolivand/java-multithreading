package com.tutorial.concurrency.fundemental.moduleb;

import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int counter = 0;
    private final Object lock = new Object();
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public void increment() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    /*
    Thread Safe Methods
     */
    public synchronized void synchronizedIncrementMethod() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        counter++;
    }

    public void synchronizedIncrementBlockMethod() {
        synchronized (this) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
    }

    public void synchronizedCounterPrivateLock() {
        synchronized (lock) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
    }


    public void reentrantLockLockCounterPrivateLock() {
        reentrantLock.lock();
        try {
            counter++;
        } finally {
            reentrantLock.unlock();
        }
    }


}
