package org.example;

public class Bee extends Thread{
    private final HoneyPot pot;

    public Bee(HoneyPot pot) {
        this.pot = pot;
    }

    @Override
    public void run() {
        while (true) {
            try {
                pot.fill();
                System.out.println("Bee is filling honey");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
