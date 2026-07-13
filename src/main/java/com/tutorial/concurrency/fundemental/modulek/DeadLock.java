package com.tutorial.concurrency.fundemental.modulek;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

public class DeadLock {
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Thread monitor = new Thread(() -> {
            System.out.println("Monitor started. Waiting 3 seconds for deadlock to form...");
            try {
                Thread.sleep(3000); // Give threads time to deadlock
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            while (true) {
                long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
                if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                    ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreads, true, true);
                    for (ThreadInfo t : threadInfos) {
                        System.out.println("Thread : " + t.getThreadName());
                        System.out.println("State : " + t.getThreadState());
                        System.out.println("Holds lock: " + t.getLockInfo());
                        System.out.println("Waiting for lock owned by: " + t.getLockOwnerName());
                        System.out.println("Stack trace:");
                        for (StackTraceElement element : t.getStackTrace()) {
                            System.out.println("  " + element);
                        }

                    }
                    break;
                } else {
                    System.out.println("No Dead Lock Found");
                }
            }
        });
        monitor.setDaemon(true);
        monitor.start();
        executorService.submit(() -> {
            synchronized (lockA) {
                System.out.println("T1 holds A");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lockB) {
                    System.out.println("T1 holds A and B");
                }
            }
        });
        executorService.submit(() -> {
            synchronized (lockB) {
                System.out.println("T2 holds b");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lockA) {
                    System.out.println("T2 holds A and B");
                }
            }
        });

        executorService.shutdown();
    }

}
