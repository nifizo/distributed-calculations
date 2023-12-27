package org.example;


import java.util.concurrent.Semaphore;

public class BarberShop {
    public static void main(String[] args) {
        Semaphore barber = new Semaphore(0);
        Semaphore customer = new Semaphore(0);
        Semaphore accessSeats = new Semaphore(1);
        int numberOfFreeSeats = 15;

        Barber barberThread = new Barber(barber, customer, accessSeats, numberOfFreeSeats);
        barberThread.start();

        int id = 0;

        while (true) {
            Customer customerThread = new Customer(id++, barber, customer, accessSeats, numberOfFreeSeats);
            customerThread.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
