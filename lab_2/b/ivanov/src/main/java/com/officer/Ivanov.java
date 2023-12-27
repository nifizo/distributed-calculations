package com.officer;

import java.util.concurrent.BlockingQueue;

public class Ivanov implements Runnable {
    private BlockingQueue<Integer> storage;

    public Ivanov(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Ivanov steals item " + i + " from the storage");
                Thread.sleep(100);
                storage.put(i); // Put item to the storage
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
