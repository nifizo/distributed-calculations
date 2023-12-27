package org.example;

import org.example.barrier.MyCyclicBarrier;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RecruitLine extends Thread {
    private final List<Recruit> recruits = new ArrayList<>();
    private final MyCyclicBarrier barrier;
    public static Boolean turningCompleted = false;

    public RecruitLine(int groupCount, SecureRandom random, MyCyclicBarrier barrier) {
        this.barrier = barrier;
        for (int i = 0; i < groupCount; i++) {
            recruits.add(new Recruit(random.nextBoolean()));
        }
    }

    public void shuffle() {
        System.out.println("\n" + getName() + " started turning around \n");
        while (true) {
            boolean allRight = true;
            for (int i = 0; i < recruits.size(); i++) {
                if (!recruits.get(i).isFacingRight()) {
                    if (i != 0) {
                        if (recruits.get(i - 1).isFacingRight()) {
                            recruits.get(i - 1).changeDirection();
                            recruits.get(i).changeDirection();
                            allRight = false;
                        }
                    }
                } else {
                    if (i != recruits.size() - 1) {
                        if (!recruits.get(i + 1).isFacingRight()) {
                            recruits.get(i + 1).changeDirection();
                            recruits.get(i).changeDirection();
                            allRight = false;
                        }
                    }
                }
            }
            if (allRight) {
                break;
            }
        }
    }

    public Recruit first() {
        return recruits.get(0);
    }

    public Recruit last() {
        return recruits.get(recruits.size() - 1);
    }

    @Override
    public void run() {
        do {
            shuffle();
            try {
                System.out.println("\n" + getName() + " finished turning around\n");
                barrier.await();
            } catch (InterruptedException exception) {
                System.out.println(exception.getMessage());
            }
        } while (!turningCompleted);
    }

    public void print() {
        System.out.println(getName() + " : \n");
        for (Recruit recruit : recruits) {
            int myInt = recruit.isFacingRight() ? 1 : 0;
            System.out.print(myInt + " ");
        }
        System.out.println("\n");
    }
}
