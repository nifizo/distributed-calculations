package org.example;

import java.util.concurrent.Semaphore;

public class Customer extends Thread {
    private int id;
    private Semaphore barber;
    private Semaphore customer;
    private Semaphore accessSeats;
    private int numberOfFreeSeats;

    public Customer(Integer id, Semaphore barber, Semaphore customer, Semaphore accessSeats, int numberOfFreeSeats) {
        this.barber = barber;
        this.customer = customer;
        this.accessSeats = accessSeats;
        this.numberOfFreeSeats = numberOfFreeSeats;
        this.id = id;
    }

    public void run() {
        try {
            accessSeats.acquire(); // Try to get access to the chairs
            if (numberOfFreeSeats > 0) { // If there are any free seats
                numberOfFreeSeats--; // sitting down
                customer.release(); // notify the barber that there is a customer
                accessSeats.release(); // don't need to lock the chairs anymore
                try {
                    barber.acquire(); // now it's this customers turn but we have to wait if the barber is busy
                    this.get_haircut(); // cutting...
                } catch (InterruptedException ex) {
                }
            } else { // there are no free seats
                System.out.println("There are no free seats. I'm leaving.");
                accessSeats.release(); // release the lock on the seats
            }
        } catch (InterruptedException ex) {
        }
    }

    public void get_haircut() {
        System.out.println("Customer " + id + " is getting his hair cut");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
