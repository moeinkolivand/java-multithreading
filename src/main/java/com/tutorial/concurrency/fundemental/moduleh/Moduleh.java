package com.tutorial.concurrency.fundemental.moduleh;

import java.util.concurrent.*;

public class Moduleh {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<Integer> integerCallable = () -> {
            return 0;
        };
        Callable<Integer> integerCallableSlow = () -> {
            Thread.sleep(2000);
            return 1;
        };
        Future<Integer> submit = executorService.submit(integerCallable);
        try {
            System.out.println("submit.get() = " + submit.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        Future<Integer> slowTaskSubmit = executorService.submit(integerCallableSlow);
        try {
            Integer slowTaskSubmitResult = slowTaskSubmit.get(1, TimeUnit.SECONDS);
            System.out.println("slowTaskSubmitResult = " + slowTaskSubmitResult);
        } catch (TimeoutException e) {
            System.out.println("e = " + e);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                    return 5;
                })
                .thenApply(res -> {
                    return res * 2;
                }).thenCombine(
                        CompletableFuture.supplyAsync(() -> {
                            return 20;
                        })
                        , Integer::sum).exceptionally(throwable -> {
                    return -999;
                });

        Integer normalResult = completableFuture.join();
        System.out.println("Normal chain result: " + normalResult);
        System.out.println("Expected: 30");

        CompletableFuture<Integer> completableFutureReturnTen = CompletableFuture.supplyAsync(() -> {return 10;});
        CompletableFuture<Integer> completableFutureReturnTwenty = CompletableFuture.supplyAsync(() -> {return 20;});
        CompletableFuture<Integer> completableFutureReturnThirty = CompletableFuture.supplyAsync(() -> {return 30;});

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutureReturnTen, completableFutureReturnTwenty, completableFutureReturnThirty);

        allFutures.join();


        int result1 = completableFutureReturnTen.join();
        int result2 = completableFutureReturnTwenty.join();
        int result3 = completableFutureReturnThirty.join();

        int total = result1 + result2 + result3;

        System.out.println("total must be equal 30 the total is  " + total);

    }
}
