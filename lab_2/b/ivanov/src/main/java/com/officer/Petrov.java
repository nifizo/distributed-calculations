package com.officer;

import java.util.concurrent.BlockingQueue;

public class Petrov implements Runnable {
        private BlockingQueue<Integer> storage;
        private BlockingQueue<Integer> car;

    public Petrov(BlockingQueue<Integer> storage, BlockingQueue<Integer> car) {
        this.storage = storage;
        this.car = car;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int item = storage.take(); // Take item from the Ivanov
                System.out.println("Petrov takes item " + item + " from the Ivanov");
                Thread.sleep(200);
                car.put(item); // Put item to the car
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
