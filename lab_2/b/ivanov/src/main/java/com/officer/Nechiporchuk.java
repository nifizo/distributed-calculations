package com.officer;

import java.util.concurrent.BlockingQueue;

public class Nechiporchuk implements Runnable {
        private BlockingQueue<Integer> car;

    public Nechiporchuk(BlockingQueue<Integer> car) {
        this.car = car;
    }

    @Override
    public void run() {
        int totalValue = 0;

        try {
            while (true) {
                int item = car.take(); // Take item from the storage, and block if car is empty
                int itemValue = calculateItemValue(item); // Calculate item value
                totalValue += itemValue;
                Thread.sleep(100);
                System.out.println("Nechiporchuk takes item " + item + " from the car. Item value: " + itemValue);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Nechiporchuk stole items from the storage for total value: " + totalValue);
    }

    private int calculateItemValue(int item) {
        return item * 10; // example of some calculation
    }
}
