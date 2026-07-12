package com.tutorial.concurrency.fundemental.modulef;

import com.tutorial.concurrency.fundemental.moduled.BoundedBuffer;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentRepository<String, Integer> concurrentRepository = new ConcurrentRepository<>();
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    String itemName = "Item" + j;
                    int id = j;
                    concurrentRepository.save(id, itemName);
                    System.out.println("Item With Id " + id + " With Value " + itemName + "Is Saved");
                }
            });
        }

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    String byId = concurrentRepository.findById(j);
                    System.out.println("byId = " + byId);
                }
            });
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            System.out.println("Some tasks timed out!");
        }


        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Item-" + i);
        }

        Thread iteratorThread = new Thread(() -> {
            try {
                for (String item : list) {
                    System.out.println("Reading: " + item);
                    Thread.sleep(100);
                }
                System.out.println("Iterator finished (this line should NOT print)");
            } catch (ConcurrentModificationException e) {
                System.out.println("PROOF OF FAILURE: ConcurrentModificationException thrown!");
                System.out.println("Exception details: " + e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        iteratorThread.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Main thread: Modifying the list while iterator is running...");
        list.add("New-Item-1");
        list.add("New-Item-2");

        try {
            iteratorThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Demo finished. The failure was successfully demonstrated.");


        CopyOnWriteArrayList<String> concurrentList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            concurrentList.add("Item-" + i);
        }

        Thread iteratorThreadSafe = new Thread(() -> {
            try {
                for (String item : concurrentList) {
                    System.out.println("Reading: " + item);
                    Thread.sleep(100);
                }
                System.out.println("Iterator finished");
            } catch (ConcurrentModificationException e) {
                System.out.println("PROOF OF FAILURE: ConcurrentModificationException thrown!");
                System.out.println("Exception details: " + e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        iteratorThreadSafe.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Main thread: Modifying the list while iterator is running...");
        concurrentList.add("New-Item-1");
        concurrentList.add("New-Item-2");

        try {
            iteratorThreadSafe.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Demo finished. The failure was successfully demonstrated.");

        BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(5);
        ExecutorService executorServiceProducer = Executors.newFixedThreadPool(2);
        ExecutorService executorServiceConsumer = Executors.newFixedThreadPool(2);
        AtomicInteger produceCount = new AtomicInteger(0);
        AtomicInteger consumerCount = new AtomicInteger(0);

        executorServiceProducer.execute(() -> {
            for (int i = 0; i < 100; i++) {
                boundedBuffer.putWithLinkedBlockingQueue(i);
                System.out.println("Producer Written Value " + i);
                produceCount.incrementAndGet();
            }
        });

        executorServiceConsumer.execute(() -> {
            for (int i = 0; i < 100; i++) {
                Integer j = boundedBuffer.takeWithLinkedBlockingQueue();
                System.out.println("Consumer Taken Value " + j);
                consumerCount.incrementAndGet();
            }
        });

        executorServiceProducer.shutdown();
        executorServiceConsumer.shutdown();

        if (!executorServiceProducer.awaitTermination(10, TimeUnit.SECONDS)) {
            executorServiceProducer.shutdownNow();
        }
        if (!executorServiceConsumer.awaitTermination(10, TimeUnit.SECONDS)) {
            executorServiceConsumer.shutdownNow();
        }
        System.out.println("Does Produce And Consumer Count Is Equal ? " + (consumerCount.get() == produceCount.get()));
    }
}