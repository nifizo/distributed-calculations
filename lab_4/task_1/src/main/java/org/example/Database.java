package org.example;

import org.example.lock.ReadWriteLock;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Database {
    private final Map<String, String> data = new HashMap<>();
    private final ReadWriteLock lock = new ReadWriteLock();
    private final String fileName = "database.txt";

    public Database() throws FileNotFoundException {
        File file = new File(fileName);
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] entry = scanner.nextLine().split(",");
                data.put(entry[0], entry[1]);
            }
            scanner.close();
        }
    }

    public String read(String name) throws InterruptedException, IOException {
        lock.lockRead();
        try {
            return data.get(name);
        } finally {
            lock.unlockRead();
        }
    }

    public String readByPhone(String phone) throws InterruptedException {
        lock.lockRead();
        try {
            return data.entrySet().stream()
                    .filter(entry -> phone.equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);
        } finally {
            lock.unlockRead();
        }
    }

    public void write(String name, String phone) throws InterruptedException, IOException {
        lock.lockWrite();
        try {
            data.put(name, phone);
            saveToFile();
        } finally {
            lock.unlockWrite();
        }
    }

    public void delete(String name) throws InterruptedException, IOException {
        lock.lockWrite();
        try {
            data.remove(name);
            saveToFile();
        } finally {
            lock.unlockWrite();
        }
    }

    private void saveToFile() throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue() + "\n");
        }
        writer.close();
    }
}
