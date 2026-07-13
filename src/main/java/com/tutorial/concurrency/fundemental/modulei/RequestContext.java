package com.tutorial.concurrency.fundemental.modulei;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RequestContext {

    private static final ThreadLocal<String> requestId = new ThreadLocal<>();

    public static void setBuggy(String id) {
        System.out.println("[BUGGY] Setting requestId to: " + id);
        requestId.set(id);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[BUGGY] Finished WITHOUT clearing.");
    }

    public static void setFixed(String id) {
        try {
            System.out.println("[FIXED] Setting requestId to: " + id);
            requestId.set(id);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            clear();
            System.out.println("[FIXED] Cleared ThreadLocal.");
        }
    }

    public static String get() {
        return requestId.get();
    }

    public static void clear() {
        requestId.remove();
    }

    public static void logCurrentRequest() {
        System.out.println("Current RequestId: " + get());
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("PART 1: BUGGY DEMO (Leak)");
        ExecutorService pool1 = Executors.newSingleThreadExecutor();

        pool1.submit(() -> {
            setBuggy("req-A");
        });

        pool1.submit(() -> {
            System.out.println("Task B reading...");
            logCurrentRequest();
        });

        pool1.shutdown();
        pool1.awaitTermination(2, TimeUnit.SECONDS);

        System.out.println("PART 2: FIXED DEMO (No Leak) ===");
        ExecutorService pool2 = Executors.newSingleThreadExecutor();

        pool2.submit(() -> {
            setFixed("req-C");
        });
        pool2.submit(() -> {
            System.out.println("  Task D reading...");
            logCurrentRequest();
        });

        pool2.shutdown();
        if (!pool2.awaitTermination(5, TimeUnit.SECONDS)) {
            pool2.shutdownNow();
        }

        System.out.println("Bug demonstrated: Task B saw 'req-A' from Task A.");
        System.out.println("Fix demonstrated: Task D saw 'null' because Task C cleared.");
    }
}