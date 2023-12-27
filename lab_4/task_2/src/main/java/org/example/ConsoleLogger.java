package org.example;

import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConsoleLogger extends Thread{
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Garden garden;
    private final SecureRandom random = new SecureRandom();

    public ConsoleLogger(Garden garden){
        this.garden = garden;
    }

    @Override
    public void run(){
        while(true){
            try{
                sleep(random.nextInt(1000) + 1000);
            }catch(InterruptedException exception){
                System.out.println(exception.getMessage());
            }

            try{
                readLock.lock();
                System.out.println("\nThe logger is logging logs in the console:");
                log();
            }finally{
                readLock.unlock();
            }
        }
    }

    private void log(){
        System.out.println("\n");
        for(int i = 0; i < garden.gardenMap.length; i++){
            for(int j = 0; j < garden.gardenMap[i].length; j++){
                if(garden.gardenMap[i][j] == 0){
                    System.out.print(String.format("%20s","dehydrated"));
                }else if(garden.gardenMap[i][j] == 1){
                    System.out.print(String.format("%20s","growing"));
                }else if(garden.gardenMap[i][j] == 2){
                    System.out.print(String.format("%20s","blooming"));
                }else{
                    System.out.print(String.format("%20s","unknown"));
                }
            }
            System.out.println("\n");
        }
    }
}