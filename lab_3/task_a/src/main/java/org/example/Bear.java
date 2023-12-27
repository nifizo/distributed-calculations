package org.example;

public class Bear extends Thread {
    private final HoneyPot pot;

    public Bear(HoneyPot pot) {
        this.pot = pot;
    }

    @Override
    public void run() {
        while (true) {
            try {
                pot.empty(); // wait until pot is full
                System.out.println("Bear is eating honey");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
