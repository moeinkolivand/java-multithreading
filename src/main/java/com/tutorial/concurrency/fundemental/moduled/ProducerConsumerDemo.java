package com.tutorial.concurrency.fundemental.moduled;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger producedCount = new AtomicInteger(0);
        AtomicInteger consumerCount = new AtomicInteger(0);
        BoundedBuffer<String> boundedBuffer = new BoundedBuffer<>(5);
        int threadCount = 2;
        int itemsPerProducer = 10;
        int itemsPerConsumer = 10;
        ExecutorService executorServiceProducer = Executors.newFixedThreadPool(threadCount);
        ExecutorService executorServiceConsumer = Executors.newFixedThreadPool(threadCount);


        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorServiceProducer.submit(() -> {
                try {
                    for (int j = 0; j < itemsPerProducer; j++) {
                        String item = "Item-P" + finalI + "-" + j;
                        boundedBuffer.put(item);
                        producedCount.incrementAndGet();
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (int i = 0; i < threadCount; i++) {
            executorServiceConsumer.submit(() -> {
                try {
                    for (int j = 0; j < itemsPerConsumer; j++) {
                        boundedBuffer.take();
                        consumerCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executorServiceProducer.shutdown();
        executorServiceConsumer.shutdown();
        if (!executorServiceProducer.awaitTermination(60, TimeUnit.SECONDS)) {
            executorServiceProducer.shutdownNow();
        }
        if (!executorServiceConsumer.awaitTermination(60, TimeUnit.SECONDS)) {
            executorServiceConsumer.shutdownNow();
        }

        System.out.println("producedCount = " + producedCount.get());
        System.out.println("consumerCount = " + consumerCount.get());
        System.out.println("Are They Equal ? " + (producedCount.get() == consumerCount.get()));
    }
}
