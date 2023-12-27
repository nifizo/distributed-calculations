package com.winnie;

import java.util.concurrent.atomic.AtomicBoolean;


public class Forest {
    private final AtomicBoolean[] areas;
    private final int winnieLocationArea;
    
    public Forest(int size, int winnieLocationArea) {
        areas = new AtomicBoolean[size];
        for (int i = 0; i < size; i++) {
            areas[i] = new AtomicBoolean(false);
        }
        this.winnieLocationArea = winnieLocationArea;
    }
    
    public boolean isAreaOccupied(int index) {
        return areas[index].get();
    }
    
    public void occupyArea(int index) {
        areas[index].set(true);
    }
    
    public void clearArea(int index) {
        areas[index].set(false);
    }

    public Boolean checkWinnie(int index) {
        return index == winnieLocationArea;
    }
}