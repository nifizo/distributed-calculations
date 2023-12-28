package com.winnie;

import java.util.List;

class Bee implements Runnable {
    private final int beeId;
    private final Forest forest;
    private final List<Integer> taskQueue;

    public Bee(int beeId, Forest forest, List<Integer> taskQueue) {
        this.beeId = beeId;
        this.forest = forest;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            int areaToSearch = getNextTask();
            if (areaToSearch == -1) {
                break; // No more tasks
            }

            if (!forest.isAreaOccupied(areaToSearch)) {
                forest.occupyArea(areaToSearch);
                try {
                    System.out.println("Bee " + beeId + " exploring the area " + areaToSearch);
                    Thread.sleep(500); // Exploring area
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (forest.checkWinnie(areaToSearch)) {
                try {
                    this.punishWinnie(areaToSearch);
                    Thread.sleep(5000); // Punishing Winnie
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            }
        }
    }

    private int getNextTask() {
        synchronized (taskQueue) {
            if (!taskQueue.isEmpty()) {
                return taskQueue.remove(0);
            }
        }
        return -1;
    }

    private void punishWinnie(int areaToSearch) {
        System.out.println("Bee " + beeId + " found Winnie the Pooh at area " + areaToSearch + "\n Punishment performed:");

    }
}
