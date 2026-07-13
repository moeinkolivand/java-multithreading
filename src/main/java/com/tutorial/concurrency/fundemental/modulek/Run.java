package com.tutorial.concurrency.fundemental.modulek;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Run {

    public static void main(String[] args) throws Exception {
        JobEngine engine = new JobEngine();
        engine.startJobEngine();

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            boolean isFailing = (i == 3);

            Job job = new Job(
                    () -> {
                        if (isFailing) {
                            System.out.println("  [Job " + taskId + "] THROWING EXCEPTION!");
                            throw new RuntimeException("Simulated failure in job " + taskId);
                        }

                        long sleepTime = (taskId % 3 == 0) ? 1500 : 200;
                        System.out.println("  [Job " + taskId + "] Starting... (sleep " + sleepTime + "ms)");
                        Thread.sleep(sleepTime);
                        return "Result from Job " + taskId;
                    }
            );

            CompletableFuture<String> future = engine.submit(job);
            futures.add(future);

            if (taskId % 2 == 0) {
                future.thenAccept(result -> {
                    System.out.println("[CALLBACK] Job " + taskId + " finished async: " + result);
                }).exceptionally(ex -> {
                    System.out.println("[CALLBACK] Job " + taskId + " failed async: " + ex.getMessage());
                    return null;
                });
            }
        }

        System.out.println("Main: Waiting 2 seconds for jobs to start...");
        Thread.sleep(2000);

        System.out.println("--- MID-RUN STATUS SNAPSHOT ---");
        var statusMap = engine.getJobStatus();
        long pending = statusMap.values().stream().filter(s -> s == JobStatus.PENDING).count();
        long running = statusMap.values().stream().filter(s -> s == JobStatus.RUNNING).count();
        long done = statusMap.values().stream().filter(s -> s == JobStatus.DONE).count();
        long failed = statusMap.values().stream().filter(s -> s == JobStatus.FAILED).count();
        System.out.println("PENDING: " + pending);
        System.out.println("RUNNING: " + running);
        System.out.println("DONE:    " + done);
        System.out.println("FAILED:  " + failed);

        System.out.println("Main: Blocking on odd-numbered jobs with .get()...");
        for (int i = 0; i < futures.size(); i++) {
            if (i % 2 != 0) {
                try {
                    String result = futures.get(i).get(3, TimeUnit.SECONDS);
                    System.out.println("[BLOCKING] Job " + i + " result: " + result);
                } catch (Exception e) {
                    System.out.println("[BLOCKING] Job " + i + " failed: " + e.getMessage());
                }
            }
        }

        System.out.println("Main: Shutting down engine...");
        JobEngine.ShutdownSummary summary = engine.shutDown();

        System.out.println(" FINAL SHUTDOWN SUMMARY ");
        System.out.println("  Submitted: " + summary.submitted());
        System.out.println("  Completed: " + summary.completed());
        System.out.println("  Failed:    " + summary.failed());
        System.out.println("  Pool terminated? " + engine.isPoolTerminated());
    }
}