package com.tutorial.concurrency.fundemental;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Moduleg {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== MODULE I: EXECUTOR SERVICE DEMO ===\n");

        System.out.println("=== PART 1: FixedThreadPool (4 threads) ===");
        ExecutorService fixedPool = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 20; i++) {
            int taskId = i;
            fixedPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("  [FIXED] Task " + taskId + " START on " + threadName);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("  [FIXED] Task " + taskId + " END on " + threadName);
            });
        }

        shutdownAndAwait(fixedPool, "FixedThreadPool");

        System.out.println("\n=== PART 2: CachedThreadPool ===");
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        Map<String, Integer> threadCounter = new ConcurrentHashMap<>();

        for (int i = 0; i < 20; i++) {
            int taskId = i;
            cachedPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                threadCounter.compute(threadName, (k, v) -> v == null ? 1 : v + 1);
                System.out.println("  [CACHED] Task " + taskId + " on " + threadName);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        shutdownAndAwait(cachedPool, "CachedThreadPool");
        System.out.println("  Number of threads created by cached pool: " + threadCounter.keySet().size());
        System.out.println("  WARNING: With 1000 tasks, this would create 1000 threads and exhaust memory!");

        System.out.println("\n=== PART 3: SingleThreadExecutor ===");
        ExecutorService singleThread = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
            int taskId = i;
            singleThread.execute(() -> {
                System.out.println("  [SINGLE] Task " + taskId + " START on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("  [SINGLE] Task " + taskId + " END");
            });
        }

        shutdownAndAwait(singleThread, "SingleThreadExecutor");

        System.out.println("\n=== PART 4: ScheduledThreadPool ===");
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);

        scheduledPool.schedule(() -> {
            System.out.println("  [SCHEDULED] One-shot task executed after 2 seconds.");
        }, 2, TimeUnit.SECONDS);

        AtomicInteger counter = new AtomicInteger(0);
        AtomicReference<ScheduledFuture<?>> futureRef = new AtomicReference<>();

        Runnable repeatingTask = () -> {
            int currentCount = counter.incrementAndGet();
            System.out.println("  [SCHEDULED] Repeating task: Execution #" + currentCount);

            if (currentCount >= 3) {
                System.out.println("  [SCHEDULED] Reached 3 executions. Cancelling future.");
                ScheduledFuture<?> future = futureRef.get();
                if (future != null) {
                    future.cancel(false);
                }
            }
        };

        ScheduledFuture<?> future = scheduledPool.scheduleAtFixedRate(repeatingTask, 0, 1, TimeUnit.SECONDS);
        futureRef.set(future);

        System.out.println("  Main: Waiting for scheduled tasks to execute (5 seconds)...");
        Thread.sleep(5000);

        shutdownAndAwait(scheduledPool, "ScheduledThreadPool");

        System.out.println("\n All executor services demonstrated and cleaned up correctly.");
    }

    private static void shutdownAndAwait(ExecutorService pool, String poolName) throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println(poolName + " timed out. Forcing shutdown...");
            pool.shutdownNow();
        }
        System.out.println("  " + poolName + " isTerminated(): " + pool.isTerminated());
    }
}