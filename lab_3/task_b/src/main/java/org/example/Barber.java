package org.example;

import java.util.concurrent.Semaphore;

public class Barber extends Thread {
    private Semaphore barber;
    private Semaphore customer;
    private Semaphore accessSeats;
    private int numberOfFreeSeats;

    public Barber(Semaphore barber, Semaphore customer, Semaphore accessSeats, int numberOfFreeSeats) {
        this.barber = barber;
        this.customer = customer;
        this.accessSeats = accessSeats;
        this.numberOfFreeSeats = numberOfFreeSeats;
    }

    public void run() {
        while (true) {
            try {
                customer.acquire(); // Wait for a customer to arrive and wake me up
                accessSeats.acquire(); // I need to change the number of free seats
                numberOfFreeSeats++; // One chair gets free
                barber.release(); // I'm ready to cut hair now
                accessSeats.release(); // I don't need to lock the chairs anymore
                this.cutHair(); // Cutting...
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cutHair() {
        System.out.println("The barber is cutting hair");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
