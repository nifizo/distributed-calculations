package com.example;

import javax.swing.JSlider;

class ThreadManager {
    public static final int thread1TargetSliderValue = 10;
    public static final int thread2TargetSliderValue = 90;

    private int semaphore = 0;
    private CustomThread thread1;
    private CustomThread thread2;
    private JSlider sharedSlider;

    public ThreadManager(JSlider sharedSlider) {
        this.sharedSlider = sharedSlider;
    }

    public void setThreadPriority(int threadNumber, int priority) {
        if (threadNumber == 1) {
            if (thread1 != null) {
                thread1.setPriority(priority);
            }
        } else if (threadNumber == 2) {
            if (thread2 != null) {
                thread2.setPriority(priority);
            }
        }
    }

    public void startThreads(int priority1, int priority2) {
        thread1.setPriority(priority1);
        thread2.setPriority(priority2);
        thread1.start();
        thread2.start();
    }

    public void stopThreads() {
        thread1.interrupt();
        thread2.interrupt();
    }

        public int getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(int semaphore) {
        this.semaphore = semaphore;
    }

    public void startThread(int threadNumber) {
        if (threadNumber == 1) {
            thread1.setTargetSliderValue(thread1TargetSliderValue);
            thread1.start();
        } else if (threadNumber == 2) {
            thread2.setTargetSliderValue(thread2TargetSliderValue);
            thread2.start();
        }
    }

    public void stopThread(int threadNumber) {
        if (threadNumber == 1) {
            thread1.interrupt();
        } else if (threadNumber == 2) {
            thread2.interrupt();
        }
    }

    public void createThread(int threadNumber) {
        if (threadNumber == 1) {
            thread1 = new CustomThread(sharedSlider);
            thread1.setTargetSliderValue(thread1TargetSliderValue);
        } else if (threadNumber == 2) {
            thread2 = new CustomThread(sharedSlider);
            thread2.setTargetSliderValue(thread2TargetSliderValue);
        }
    }
}

