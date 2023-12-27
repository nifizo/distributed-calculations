package org.example;

public class HoneyPot {
    private final int capacity;
    private int currentAmount;
    private final BinarySemaphore semaphore; // used to signal bear that pot is full

    public HoneyPot(int capacity) {
        this.capacity = capacity;
        this.currentAmount = 0;
        this.semaphore = new BinarySemaphore();
    }

    public synchronized void fill() throws InterruptedException {
        while (currentAmount == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        currentAmount++;

        System.out.println("Filling pot. Current amount: " + currentAmount);
        if (currentAmount >= capacity) {
            semaphore.take();
        }
    }

    public synchronized void empty() {
        semaphore.release();

        currentAmount = 0;
        notifyAll();
    }
}

