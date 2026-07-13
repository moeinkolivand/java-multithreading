# Java Concurrency Learning Project

> A hands-on implementation of Java's concurrency APIs, progressing from basic thread creation to building a concurrent job-processing engine.

## Overview

This repository is my implementation of a comprehensive Java Concurrency roadmap. Rather than focusing on theory alone, each module demonstrates a specific concurrency concept through practical examples that can be executed and explored independently.

The project follows a progressive learning path. Every module introduces a new concept while building upon the knowledge gained in previous modules. By the end of the project, all of these concepts are combined into a small concurrent job-processing engine that resembles a real-world system.

The objective is not only to learn **how** Java concurrency utilities work, but also **why** they exist, **what problems they solve**, and **when** they should be used.

---

## Learning Roadmap

```
Thread Fundamentals
        │
        ▼
Race Conditions
        │
        ▼
Synchronization (synchronized)
        │
        ▼
ReentrantLock
        │
        ▼
volatile & Memory Visibility
        │
        ▼
wait() / notify()
        │
        ▼
Atomic Variables & CAS
        │
        ▼
Concurrent Collections
        │
        ▼
ExecutorService
        │
        ▼
Future & CompletableFuture
        │
        ▼
ThreadLocal
        │
        ▼
Deadlocks
        │
        ▼
Concurrent Job Processing Engine
```

---

# Modules

| Module | Topic                      | Description                                                       |
| ------ | -------------------------- | ----------------------------------------------------------------- |
| A      | Thread Fundamentals        | Thread creation, lifecycle, join(), interrupt(), start() vs run() |
| B      | Race Conditions            | Shared mutable state and lost updates                             |
| C      | synchronized               | Intrinsic locking and monitor synchronization                     |
| D      | ReentrantLock              | Explicit locking, tryLock(), fairness, lock inspection            |
| E      | volatile                   | Memory visibility and the Java Memory Model                       |
| F      | wait() / notify()          | Producer-Consumer using low-level synchronization                 |
| G      | Atomic Variables           | Lock-free programming with Compare-And-Swap (CAS)                 |
| H      | Concurrent Collections     | ConcurrentHashMap, BlockingQueue, CopyOnWriteArrayList            |
| I      | ExecutorService            | Thread pools and task execution                                   |
| J      | Future & CompletableFuture | Asynchronous programming and task composition                     |
| K      | ThreadLocal                | Thread-local storage and request context                          |
| L      | Deadlocks                  | Deadlock creation, detection, and prevention                      |
| M      | Capstone                   | Concurrent Job Processing Engine                                  |

---

# Project Structure

```
src/
└── main/
    └── java/
        └── com/
            └── tutorial/
                └── concurrency/
                    ├── modulea/
                    ├── moduleb/
                    ├── modulec/
                    ├── moduled/
                    ├── modulee/
                    ├── modulef/
                    ├── moduleg/
                    ├── moduleh/
                    ├── modulei/
                    ├── modulej/
                    ├── modulek/
                    ├── modulel/
                    └── modulem/
```

Each module contains independent examples and can be executed separately.

---

# Concepts Covered

## Thread Management

* Creating threads
* Thread lifecycle
* Thread states
* Thread interruption
* Thread coordination
* join()

## Synchronization

* synchronized methods
* synchronized blocks
* Intrinsic locks
* ReentrantLock
* Lock ordering
* Deadlock prevention

## Java Memory Model

* Memory visibility
* volatile
* Atomicity
* Shared mutable state

## Lock-Free Programming

* AtomicInteger
* AtomicReference
* Compare-And-Swap (CAS)

## Concurrent Collections

* ConcurrentHashMap
* CopyOnWriteArrayList
* BlockingQueue

## High-Level Concurrency

* ExecutorService
* Fixed Thread Pool
* Cached Thread Pool
* Scheduled Thread Pool
* Callable
* Future
* CompletableFuture

## Advanced Topics

* ThreadLocal
* Producer-Consumer Pattern
* Deadlock Detection
* Graceful Shutdown

---

# Learning Goals

This repository demonstrates:

* Why race conditions occur
* How synchronization prevents data corruption
* The difference between intrinsic and explicit locks
* Memory visibility problems and the role of `volatile`
* Low-level thread communication using `wait()` and `notifyAll()`
* Lock-free programming with atomic classes
* Safe concurrent data structures
* Efficient thread management using thread pools
* Asynchronous programming with `CompletableFuture`
* Thread-local storage in pooled environments
* Deadlock detection and prevention
* Building a realistic concurrent application

---

# Running the Examples

Each module includes its own `main()` method and can be executed independently.

Example:

```bash
Run ThreadBasicsDemo
Run RaceConditionDemo
Run ProducerConsumerDemo
Run ExecutorServiceDemo
Run JobEngineDemo
```

The output order may vary between executions because thread scheduling is nondeterministic.

---

# Capstone Project

The final module combines everything learned throughout the project into a simplified concurrent job-processing engine.

Features include:

* Fixed thread pool
* Blocking queue
* Dispatcher thread
* Concurrent job status tracking
* CompletableFuture results
* ThreadLocal request context
* Graceful shutdown
* Failure handling

This module demonstrates how multiple concurrency utilities work together in a production-style application.

---

# Technologies

* Java
* Maven
* java.util.concurrent
* CompletableFuture API

---

# What You'll Learn

By completing this project, you will gain practical experience with:

* Thread lifecycle
* Thread synchronization
* Race conditions
* Locks
* Atomic operations
* Concurrent collections
* Thread pools
* Futures
* CompletableFuture
* ThreadLocal
* Deadlock prevention
* Producer-Consumer pattern
* Java Memory Model

---

# Repository Purpose

This project was created as a structured learning journey through Java concurrency.

Instead of presenting isolated code snippets, it focuses on understanding the evolution of Java's concurrency APIs—from creating a single thread to coordinating complex concurrent systems using the utilities provided by `java.util.concurrent`.

Each module builds upon the previous one, making the repository suitable for developers preparing for interviews, strengthening their understanding of concurrent programming, or transitioning into backend development with Java.

---

# Future Improvements

Possible extensions to this project include:

* Fork/Join Framework
* Parallel Streams
* CountDownLatch
* CyclicBarrier
* Phaser
* Semaphore
* ReadWriteLock
* StampedLock
* Virtual Threads (Project Loom)
* Structured Concurrency
* Reactive Programming

