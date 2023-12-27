package org.example;

import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Nature extends Thread {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final SecureRandom random = new SecureRandom();
    private final Lock writeLock = lock.writeLock();
    private final Garden garden;

    public Nature(Garden garden) {
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
                System.out.println("\n The Nature is doing its thing shuffling all the flowers in the garden.");
                shuffleStates();
            }finally{
                writeLock.unlock();
            }
        }
    }

    private void shuffleStates(){
        for(int i = 0; i < garden.gardenMap.length; i++){
            for(int j = 0; j < garden.gardenMap[i].length; j++){
                garden.gardenMap[i][j] = random.nextInt(3);
            }
        }
    }
}
