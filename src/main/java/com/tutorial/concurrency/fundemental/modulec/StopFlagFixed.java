package com.tutorial.concurrency.fundemental.modulec;


public class StopFlagFixed {
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            long iterations = 0;
            System.out.println("Worker thread started. Looping...");

            while (running) {
                iterations++;
                try {
                    Thread.sleep(0, 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Worker thread stopped cleanly after " + iterations + " iterations.");
        });

        worker.start();

        Thread.sleep(2000);
        System.out.println("Main thread: setting running = false");

        running = false;

        worker.join();
        System.out.println("Worker finished. Volatile fixed the visibility issue.");
    }
}
