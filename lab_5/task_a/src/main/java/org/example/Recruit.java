package org.example;

public class Recruit {
    private boolean direction;

    public Recruit(boolean direction){
        this.direction = direction;
    }

    public void changeDirection(){
        direction = !direction;
    }

    public boolean isFacingRight(){
        return this.direction;
    }
}