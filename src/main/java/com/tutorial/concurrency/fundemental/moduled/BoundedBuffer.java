package com.tutorial.concurrency.fundemental.moduled;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBuffer<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            this.wait();
        }
        queue.offer(item);
        System.out.println("Produced: " + item + " | Queue Size " + queue.size());
        // Use notifyAll() to wake up both producers and consumers.
        // notify() could wake the wrong thread type and cause a deadlock.
        this.notifyAll();
    }

    synchronized T take() throws InterruptedException {
        /*
        Spurious wakeups are allowed by the JVM specification—a thread can wake from wait() without ever receiving a notify(). Additionally, even with notifyAll,
         when multiple threads wake up, the first one might consume the only item. Using while forces the thread to re-check the condition.
          If we used if, the thread would assume the condition is true, leading to trying to take from an empty queue or add to a full queue, crashing the program.
         */
        while (queue.isEmpty()) {
            this.wait();
        }
        T item = queue.poll();
        System.out.println("Consumed: " + item + " | Queue Size " + queue.size());
        this.notifyAll();
        return item;
    }
}
