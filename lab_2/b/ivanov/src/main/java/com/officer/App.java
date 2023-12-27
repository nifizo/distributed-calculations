package com.officer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class App 
{
    public static void main(String[] args) {
        BlockingQueue<Integer> storage = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> car = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Ivanov ivanov = new Ivanov(storage);
        Petrov petrov = new Petrov(storage, car);
        Nechiporchuk nechiporchuk = new Nechiporchuk(car);

        executor.submit(ivanov);
        executor.submit(petrov);
        executor.submit(nechiporchuk);

        executor.shutdown();
    }
}