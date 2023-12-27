package org.example.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ReadWriteLockTest {
    private ReadWriteLock lock;

    @BeforeEach
    void setUp() {
        lock = new ReadWriteLock();
    }

    @Test
    void testLockRead() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            service.submit(() -> {
                try {
                    lock.lockRead();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(10, lock.getReaders());
    }

    @Test
    void testUnlockRead() throws InterruptedException {
        lock.lockRead();
        lock.unlockRead();
        assertEquals(0, lock.getReaders());
    }

    @Test
    void testLockWrite() throws InterruptedException {
        lock.lockWrite();
        assertEquals(1, lock.getWriters());
        lock.unlockWrite();
    }

    @Test
    void testUnlockWrite() throws InterruptedException {
        lock.lockWrite();
        lock.unlockWrite();
        assertEquals(0, lock.getWriters());
    }

    @Test
    void testPotentialDeadlock() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(() -> {
            try {
                lock.lockRead();
                Thread.sleep(1000);
                lock.unlockRead();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.submit(() -> {
            try {
                lock.lockWrite();
                Thread.sleep(1000);
                lock.unlockWrite();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
        assertTrue(service.awaitTermination(10, TimeUnit.SECONDS));
    }
}
