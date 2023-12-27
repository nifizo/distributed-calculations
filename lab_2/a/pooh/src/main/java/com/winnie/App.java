package com.winnie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
public static void main(String[] args) {
        Random random = new Random();
        int forestSize = random.nextInt(200);        
        Forest forest = new Forest(forestSize, random.nextInt(forestSize));
        List<Integer> taskQueue = new ArrayList<>();

        // Initialize the task queue with areas to search
        for (int i = 0; i < forestSize; i++) {
            taskQueue.add(i);
        }

        int numBees = 5; // Number of bees to search the forest

        List<Thread> beeThreads = new ArrayList<>();

        for (int i = 0; i < numBees; i++) {
            Bee bee = new Bee(i, forest, taskQueue);
            Thread beeThread = new Thread(bee);
            beeThreads.add(beeThread);
            beeThread.start();
        }

        // Wait for all bee threads to finish
        for (Thread beeThread : beeThreads) {
            try {
                beeThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
