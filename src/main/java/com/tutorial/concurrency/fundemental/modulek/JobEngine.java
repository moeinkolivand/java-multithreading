package com.tutorial.concurrency.fundemental.modulek;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class JobEngine {
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final BlockingQueue<Job> tasks = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<UUID, JobStatus> jobStatus = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<String>> featureMap = new ConcurrentHashMap<>();
    private volatile boolean shutDownRequested = false;
    private final ThreadLocal<UUID> threadLocal = new ThreadLocal<>();
    private Thread threadDispatcher;

    public ConcurrentHashMap<UUID, JobStatus> getJobStatus() {
        return jobStatus;
    }

    public boolean isPoolTerminated() {
        return executorService.isTerminated();
    }

    public void startJobEngine() {
        Runnable dispatcher = () -> {
            while (!shutDownRequested) {
                try {
                    Job job = tasks.take();
                    jobStatus.put(job.getId(), JobStatus.RUNNING);
                    executorService.submit(() -> {
                        threadLocal.set(job.getId());
                        try {
                            String result = job.getTask().call();
                            jobStatus.put(job.getId(), JobStatus.DONE);
                            CompletableFuture<String> future = featureMap.get(job.getId());
                            if (future != null) {
                                future.complete(result);
                            }
                        } catch (Exception e) {
                            jobStatus.put(job.getId(), JobStatus.FAILED);
                            CompletableFuture<String> future = featureMap.get(job.getId());
                            if (future != null) {
                                future.completeExceptionally(e);
                            }
                        } finally {
                            threadLocal.remove();
                        }
                    });
                } catch (InterruptedException e) {
                    if (shutDownRequested) {
                        break;
                    }
                    Thread.currentThread().interrupt();
                }
            }
        };
        threadDispatcher = new Thread(dispatcher);
        threadDispatcher.start();
    }

    public CompletableFuture<String> submit(Job job) {
        jobStatus.put(job.getId(), JobStatus.PENDING);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        featureMap.put(job.getId(), completableFuture);
        try {
            tasks.put(job);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            completableFuture.completeExceptionally(e);
        }
        return completableFuture;

    }

    public record ShutdownSummary(long submitted, long completed, long failed) {
    }

    ;

    public ShutdownSummary shutDown() throws InterruptedException {
        shutDownRequested = true;

        if (threadDispatcher != null) {
            threadDispatcher.interrupt();
            threadDispatcher.join();
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
        long submitted = jobStatus.size();
        long completed = jobStatus.values().stream().filter(s -> s == JobStatus.DONE).count();
        long failed = jobStatus.values().stream().filter(s -> s == JobStatus.FAILED).count();

        return new ShutdownSummary(submitted, completed, failed);
    }

}
