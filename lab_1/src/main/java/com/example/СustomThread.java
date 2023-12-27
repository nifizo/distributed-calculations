package com.example;

import javax.swing.JSlider;

class CustomThread extends Thread {
    private JSlider sharedSlider;
    private int targetSliderValue;
    private final int sleepTime = 10;

    public CustomThread(JSlider sharedSlider) {
        this.sharedSlider = sharedSlider;
        this.setPriority(Thread.MIN_PRIORITY);
    }

    public void setTargetSliderValue(int targetSliderValue) {
        this.targetSliderValue = targetSliderValue;
    }

    public int getTargetSliderValue() {
        return this.targetSliderValue;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            synchronized (sharedSlider) {
                int targetSliderValue = getTargetSliderValue();
                int currentSliderValue = sharedSlider.getValue();
                if (currentSliderValue < targetSliderValue) {
                    sharedSlider.setValue(currentSliderValue + 1);
                } else if (currentSliderValue > targetSliderValue) {
                    sharedSlider.setValue(currentSliderValue - 1);
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}