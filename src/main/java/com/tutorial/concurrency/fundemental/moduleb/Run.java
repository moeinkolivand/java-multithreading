package com.tutorial.concurrency.fundemental.moduleb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        int threadCount = 10;
        int incrementCallTime = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        Runnable callIncrement = () -> {
            for (int i = 0; i < incrementCallTime; i++) {
                counter.increment();
            }
        };
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(callIncrement);
        }

        Counter synchronizedCounterPrivateLock = new Counter();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
             for (int t = 0; t < incrementCallTime; t++) {
                 synchronizedCounterPrivateLock.synchronizedCounterPrivateLock();
             }
            });
        }


        Counter synchronizedIncrementBlockMethod = new Counter();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                for (int t = 0; t < incrementCallTime; t++) {
                    synchronizedIncrementBlockMethod.synchronizedIncrementBlockMethod();
                }
            });
        }

        Counter synchronizedIncrementMethod = new Counter();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                for (int t = 0; t < incrementCallTime; t++) {
                    synchronizedIncrementMethod.synchronizedIncrementMethod();
                }
            });
        }
        Counter reentrantLock = new Counter();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                for (int t = 0; t < incrementCallTime; t++) {
                    reentrantLock.reentrantLock();
                }
            });
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
        System.out.println("Total processed In Thread Safe Version With Method synchronizedIncrementMethod " + synchronizedIncrementMethod.getCounter());
        System.out.println("Total processed In Thread Safe Version With Method synchronizedIncrementBlockMethod " + synchronizedIncrementBlockMethod.getCounter());
        System.out.println("Total processed In Thread Safe Version With Method SynchronizedCounterPrivateLock " + synchronizedCounterPrivateLock.getCounter());
        System.out.println("Total processed In Thread Safe Version With Method ReenTrantLock " + reentrantLock.getCounter());
        System.out.println("Total processed In Not Thread Safe Version: " + counter.getCounter());

    }

}
