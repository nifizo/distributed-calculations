package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Integer N = null;

        // Get the number of bees from the console
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of bees: ");
            N = scanner.nextInt();
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of bees");
            System.exit(1);
        }

        HoneyPot pot = new HoneyPot(N);
        Bear bear = new Bear(pot);

        // Create N bees
        for (int i = 0; i < N; i++) {
            Bee bee = new Bee(pot);
            bee.start();
        }

        bear.start();

        // Wait for the bear to finish
        try {
            bear.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
