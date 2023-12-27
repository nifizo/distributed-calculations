package org.example;


import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Gardener extends Thread{
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final SecureRandom random = new SecureRandom();
    private final Garden garden;

    public Gardener(Garden garden){
        this.garden = garden;
    }

    @Override
    public void run() {
        while(true){
            try{
                sleep(random.nextInt(1000) + 500);
            }catch(InterruptedException exception){
                System.out.println(exception.getMessage());
            }

            try{
                writeLock.lock();
                System.out.println("\nThe Gardener is watering the flowers");;
                water();
            }finally{
                writeLock.unlock();
            }
        }
    }

    private void water(){
        for(int i = 0; i < garden.gardenMap.length; i++){
            for(int j = 0; j < garden.gardenMap[i].length; j++){
                if(garden.gardenMap[i][j] == 0){
                    garden.gardenMap[i][j] = 1;
                }
            }
        }
    }
}
