package com.tutorial.concurrency.fundemental.modulec;

public class StopFlagBroken {

    private static boolean running = true;

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

            System.out.println("Worker thread stopped after " + iterations + " iterations.");
        });

        worker.start();

        Thread.sleep(2000);
        System.out.println("Main thread: setting running = false");

        running = false;

        worker.join(3000);

        if (worker.isAlive()) {
            System.out.println("!!! WORKER IS STILL RUNNING !!!");
            System.out.println("Visibility bug reproduced: the worker never saw the updated value.");
            worker.interrupt();
        } else {
            System.out.println("Worker finished (visibility bug did NOT reproduce on this JVM/arch).");
        }

        System.out.println("Demo finished.");
    }
}
