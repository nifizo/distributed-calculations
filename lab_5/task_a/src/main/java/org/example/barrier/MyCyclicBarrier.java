package org.example.barrier;

/**
 * A simple implementation of a CyclicBarrier.
 */
public class MyCyclicBarrier {
    private final int finalThreadCount;
    private volatile int currentThreadCount;

    /**
     * Constructor for MyCyclicBarrier.
     *
     * @param finalThreadCount The number of threads that must invoke await() before threads are released.
     */
    public MyCyclicBarrier(int finalThreadCount) {
        this.finalThreadCount = finalThreadCount;
        this.currentThreadCount = 0;
    }

    /**
     * Causes the current thread to wait until a set number of parties (threads) have invoked await on this barrier.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    public synchronized void await() throws InterruptedException{
        currentThreadCount++;
        if(currentThreadCount < finalThreadCount){
            this.wait();
        }
        currentThreadCount = 0;
        notifyAll();
    }

    /**
     * Checks if the barrier is in a broken state.
     *
     * @return true if the barrier is broken, false otherwise.
     */
    public synchronized Boolean isBroken(){
        return currentThreadCount == finalThreadCount - 1;
    }

    /**
     * Resets the barrier to its initial state.
     */
    public synchronized void reset(){
        currentThreadCount = 0;
    }

    /**
     * Returns the number of parties required to trip this barrier.
     *
     * @return the number of parties required to trip this barrier.
     */
    public synchronized int getParties(){
        return finalThreadCount;
    }

    /**
     * Returns the number of parties currently waiting at the barrier.
     *
     * @return the number of parties currently waiting at the barrier.
     */
    public synchronized int getNumberWaiting(){
        return currentThreadCount;
    }
}
