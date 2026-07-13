package com.tutorial.concurrency.fundemental.modulek;

import java.util.UUID;
import java.util.concurrent.Callable;

public class Job {
    private final UUID id = UUID.randomUUID();
    private final Callable<String> task;

    public Job(Callable<String> task) {
        this.task = task;
    }


    public UUID getId() {
        return id;
    }


    public Callable<String> getTask() {
        return task;
    }
}
