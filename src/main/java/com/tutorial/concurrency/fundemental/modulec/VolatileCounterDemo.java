package com.tutorial.concurrency.fundemental.modulec;


public class VolatileCounterDemo {
    private static volatile int count = 0;

    public static void increment() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 10;
        int incrementsPerThread = 1000;
        int expectedTotal = numThreads * incrementsPerThread;

        for (int run = 1; run <= 3; run++) {
            count = 0;
            Thread[] threads = new Thread[numThreads];

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        try {
                            Thread.sleep(0, 1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        increment();
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            System.out.println("Run " + run + " - Expected: " + expectedTotal +
                    ", Actual: " + count +
                    " (Lost " + (expectedTotal - count) + " updates)");
        }

        System.out.println("\nEven with 'volatile', we never hit 10000.");
        System.out.println("Conclusion: volatile solves visibility (cache coherency),");
        System.out.println("but NOT atomicity (compound operations). You need synchronized or AtomicInteger.");
    }
}
