package org.example;

public class BinarySemaphore {
    private boolean signal = false;

    public synchronized void take() throws InterruptedException {
        while(!this.signal) wait();
        this.signal = false;
    }

    public synchronized void release() {
        this.signal = true;
        this.notify();
    }
}

