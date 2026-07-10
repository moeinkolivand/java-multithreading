package com.tutorial.concurrency.fundemental.moduleb;

public class Counter {
    private int counter = 0;
    private final Object lock = new Object();


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

}
