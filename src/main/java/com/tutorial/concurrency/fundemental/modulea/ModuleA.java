package com.tutorial.concurrency.fundemental.modulea;

public class ModuleA extends Thread {

    @Override
    public void run() {
        System.out.println("Thread Number Is " + Thread.currentThread().getName());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
