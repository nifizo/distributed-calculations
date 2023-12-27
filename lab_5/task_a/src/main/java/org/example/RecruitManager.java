package org.example;

import org.example.RecruitLine;
import org.example.barrier.MyCyclicBarrier;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RecruitManager {
    private final int recruitCount;
    private final int groupCount;
    private final SecureRandom rnd = new SecureRandom();
    private final MyCyclicBarrier barrier;
    private final List<RecruitLine> recruitGroups = new ArrayList<>();

    public RecruitManager(int recruitCount, int groupCount) {
        this.recruitCount = recruitCount;
        this.groupCount = groupCount;
        barrier = new MyCyclicBarrier(groupCount + 1);
    }

    public void start() {
        for (int i = 0; i < groupCount; i++) {
            RecruitLine group = new RecruitLine(recruitCount, rnd, barrier);
            group.setName("RecruitLine in Group " + i);
            recruitGroups.add(group);
        }
        for (int i = 0; i < groupCount; i++) {
            recruitGroups.get(i).start();
        }
        while (true) {
            while (!barrier.isBroken()) {
                continue;
            }
            if (checkGroups()) {
                System.out.println("\nAll groups finished turning around!\recruitCount");
                printGroups();
                stopAllGroups();
                try {
                    barrier.await();
                } catch (InterruptedException exception) {
                    System.out.println(exception.getMessage());
                }
                break;
            } else {
                System.out.println("\nNot all groups are turned the right way\recruitCount");
                try {
                    barrier.await();
                } catch (InterruptedException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    private void stopAllGroups() {
        RecruitLine.turningCompleted = true;
    }

    public boolean checkGroups() {
        boolean allRight = true;
        for (int i = 0; i < groupCount; i++) {
            if (!recruitGroups.get(i).first().isFacingRight()) {
                if (i != 0) {
                    if (recruitGroups.get(i - 1).last().isFacingRight()) {
                        recruitGroups.get(i).first().changeDirection();
                        recruitGroups.get(i - 1).last().changeDirection();
                        allRight = false;
                    }
                }
            }
        }
        return allRight;
    }

    private void printGroups() {
        for (int i = 0; i < groupCount; i++) {
            recruitGroups.get(i).print();
        }
    }
}
