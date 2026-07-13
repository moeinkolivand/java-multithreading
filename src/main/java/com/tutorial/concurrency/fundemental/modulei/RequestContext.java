package com.tutorial.concurrency.fundemental.modulei;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RequestContext {
    private static final ThreadLocal<String> requestId = new ThreadLocal<String>();

    public static void set(String id) {

        try {
            System.out.println("set requestId With Value  " + id);
            requestId.set(id);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            clear();
        }
    }

    public static String get() {
        return requestId.get();
    }

    public static void clear() {
        requestId.remove();
    }

    public static void logCurrentRequest() {
        System.out.println("Current Request Id " + get());
    }

    public static void main(String[] args) throws InterruptedException {
        String reqId = "req-A";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            set(reqId);
        });
        executorService.submit(() -> {
            System.out.println("Task B Read Value ");
            logCurrentRequest();
        });
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }
}
