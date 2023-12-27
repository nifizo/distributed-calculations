package org.example.barrier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyCyclicBarrierTest {
    private MyCyclicBarrier barrier;

    @BeforeEach
    public void setUp() {
        barrier = new MyCyclicBarrier(3);
    }

    @Test
    public void testGetParties() {
        assertEquals(3, barrier.getParties());
    }

    @Test
    public void testGetNumberWaiting() {
        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    public void testAwait() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    public void testIsBroken() {
        assertFalse(barrier.isBroken());
    }

    @Test
    public void testReset() {
        barrier.reset();
        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    public void testResetWhileWaiting() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();

        Thread.sleep(1000); // wait for t1 to start and call await()

        barrier.reset();

        assertEquals(0, barrier.getNumberWaiting());
    }
}
