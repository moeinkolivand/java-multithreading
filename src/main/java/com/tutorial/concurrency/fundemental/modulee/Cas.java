package com.tutorial.concurrency.fundemental.modulee;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Cas {

    private static final AtomicInteger atomicCounter = new AtomicInteger(0);
    private static final AtomicInteger manualCasCounter = new AtomicInteger(0);

    public static void manualIncrement() {
        int current;
        do {
            current = manualCasCounter.get();
        } while (!manualCasCounter.compareAndSet(current, current + 1));
    }

    public static class ConfigSnapshot {
        private final String theme;
        private final int timeout;

        public ConfigSnapshot(String theme, int timeout) {
            this.theme = theme;
            this.timeout = timeout;
        }

        public String getTheme() {
            return theme;
        }

        public int getTimeout() {
            return timeout;
        }
    }

    private static final AtomicReference<ConfigSnapshot> configRef =
            new AtomicReference<>(new ConfigSnapshot("Dark", 30));

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 10;
        int incrementsPerThread = 1000;
        int expectedTotal = numThreads * incrementsPerThread;

        System.out.println("=== TEST 1: AtomicInteger.incrementAndGet() ===");
        for (int run = 1; run <= 3; run++) {
            atomicCounter.set(0);
            Thread[] threads = new Thread[numThreads];

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        atomicCounter.incrementAndGet();
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) t.join();
            System.out.println("Run " + run + " - Expected: " + expectedTotal +
                    ", Actual: " + atomicCounter.get());
        }

        System.out.println("\n=== TEST 2: Manual CAS Retry Loop ===");
        for (int run = 1; run <= 3; run++) {
            manualCasCounter.set(0);
            Thread[] threads = new Thread[numThreads];

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        manualIncrement();
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) t.join();
            System.out.println("Run " + run + " - Expected: " + expectedTotal +
                    ", Actual: " + manualCasCounter.get());
        }

        System.out.println("\n=== TEST 3: AtomicReference (Config Swap) ===");

        Thread writer1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                ConfigSnapshot newConfig = new ConfigSnapshot("Light", 30 + i);
                configRef.set(newConfig);
                System.out.println("Writer 1 set: " + newConfig.getTheme() + ", " + newConfig.getTimeout());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        });

        Thread writer2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                ConfigSnapshot newConfig = new ConfigSnapshot("Retro", 60 - i);
                configRef.set(newConfig);
                System.out.println("Writer 2 set: " + newConfig.getTheme() + ", " + newConfig.getTimeout());
                try {
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                }
            }
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                ConfigSnapshot snapshot = configRef.get();
                System.out.println("Reader sees: " + snapshot.getTheme() +
                        " & Timeout: " + snapshot.getTimeout());
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                }
            }
        });

        writer1.start();
        writer2.start();
        reader.start();

        writer1.join();
        writer2.join();
        reader.join();

    }
}