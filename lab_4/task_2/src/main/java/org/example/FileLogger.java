package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileLogger extends Thread {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final String file_path = "logger.txt";
    private final Lock readLock = lock.readLock();
    private final SecureRandom random = new SecureRandom();
    private final Garden garden;

    public FileLogger(Garden garden) {
        this.garden = garden;
        try{
            File file = new File(file_path);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.close();
        }catch(IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(random.nextInt(1000) + 1000);
            } catch (InterruptedException exception) {
                System.out.println(exception.getMessage());
            }

            try {
                readLock.lock();
                System.out.println("\n File Logger is logging log to the logs file");
                log();
            } finally {
                readLock.unlock();
            }
        }
    }

    private void log() {
        File file = new File(file_path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write("\n\n");
            for (int i = 0; i < garden.gardenMap.length; i++) {
                for (int j = 0; j < garden.gardenMap[i].length; j++) {
                    if (garden.gardenMap[i][j] == 0) {
                        writer.write(String.format("%20s","dehydrated"));
                    } else if (garden.gardenMap[i][j] == 1) {
                        writer.write(String.format("%20s","growing"));
                    } else if (garden.gardenMap[i][j] == 2) {
                        writer.write(String.format("%20s","blooming"));
                    } else {
                        writer.write(String.format("%20s","unknown"));
                    }
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
