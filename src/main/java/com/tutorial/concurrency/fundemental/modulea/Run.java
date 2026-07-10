package com.tutorial.concurrency.fundemental.modulea;

public class Run {

    public static void main(String[] args) throws InterruptedException {
        /*
        Example With Extending Thread Class
         */
        ModuleA myThread = new ModuleA();
        System.out.println("Custom Thread After Creation State " + myThread.getState());
        myThread.start();
        System.out.println("Custom Thread After Start State " + myThread.getState());
        myThread.join();
        System.out.println("Custom Thread After Join State " + myThread.getState());

        System.out.println("\n---------------------------------------------------\n");

        /*
        Example With Functional Interface Runnable
         */

        Runnable myRunnableThread = () -> {
            System.out.println("Thread Name In Runnable Functional Interface Is " + Thread.currentThread().getName());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread runnableThread = new Thread(myRunnableThread);
        System.out.println("Runnable Thread After Creation State " + runnableThread.getState());
        runnableThread.start();
        System.out.println("Runnable Thread After Start State " + runnableThread.getState());
        runnableThread.join();
        System.out.println("Runnable Thread After Join State " + runnableThread.getState());

        System.out.println("\n---------------------------------------------------\n");
        /*
        Example With Interrupted Thread
         */
        Runnable runnableInterruptedThread = () -> {
            System.out.println("Interrupted Thread Is Running On Thread " + Thread.currentThread().getName());
            int iteration = 0;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    iteration++;
                    System.out.println("iteration = " + iteration);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException caught. Cleaning up...");
                    break;
                }
            }
        };

        Thread thirdThread = new Thread(runnableInterruptedThread);
        System.out.println("Interrupted Thread After Creation State " + thirdThread.getState());
        thirdThread.start();
        System.out.println("Interrupted Thread After Creation State " + thirdThread.getState());

        Thread.sleep(3000);

        thirdThread.interrupt();
        System.out.println("Interrupted Thread After Interrupt Is Interrupt ? " + thirdThread.isInterrupted());
        System.out.println("Interrupted Thread After Interrupt State " + thirdThread.getState());

        thirdThread.join();
        System.out.println("Interrupted Thread After Join State " + runnableThread.getState());

        System.out.println("Main Thread : All Done");

        /*
        Example With Calling Method Run That Proves Method Run By Self Dos`nt Create A Thread
         */
        Runnable runnableMainThread = () -> {
            System.out.println("Main Thread Is Running On Thread " + Thread.currentThread().getName());
            int iteration = 0;
            while (iteration < 15) {
                iteration++;
                try {
                    System.out.println("iteration " + iteration + " on Thread " + Thread.currentThread().getName());
                    Thread.sleep(1000);
                    if (iteration == 10) {
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        runnableMainThread.run();
    }


}
