package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Database database = new Database();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            try {
                database.write("name" + i, "phone" + i);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<String> removed = new ArrayList<>();

        while (true) {
            try {

                ExecutorService executor = Executors.newFixedThreadPool(100);

                for (int i = 0; i < 10; i++) {

                    int type = random.nextInt(10);

                    if (type < 5) {
                        int action = random.nextInt(2);
                        Reader.ReaderAction readerAction = action == 0 ? Reader.ReaderAction.READ : Reader.ReaderAction.READ_BY_PHONE;
                        String phone = "phone" + random.nextInt(10);
                        while (removed.contains(phone)) {
                            phone = "phone" + random.nextInt(10);
                        }
                        String name = "name" + random.nextInt(10);
                        while (removed.contains(name)) {
                            name = "name" + random.nextInt(10);
                        }
                        Reader reader = new Reader(database, readerAction, name, phone);
                        executor.submit(reader);
                    } else {
                        int action = random.nextInt(4);
                        if (action < 2) {
                            Reader.ReaderAction readerAction = action == 0 ? Reader.ReaderAction.READ : Reader.ReaderAction.READ_BY_PHONE;
                            String phone = "phone" + random.nextInt(10);
                            while (removed.contains(phone)) {
                                phone = "phone" + random.nextInt(10);
                            }
                            String name = "name" + random.nextInt(10);
                            while (removed.contains(name)) {
                                name = "name" + random.nextInt(10);
                            }
                            Writer reader = new Writer(database, readerAction, name, phone);
                            executor.submit(reader);
                        } else {
                            Writer.WriterAction writerAction = action == 2 ? Writer.WriterAction.WRITE : Writer.WriterAction.DELETE;
                            int ii = random.nextInt(10);
                            String name = "name" + ii;
                            String phone = "phone" + ii;
                            if (writerAction == Writer.WriterAction.DELETE) {
                                removed.add(name);
                                removed.add(database.read(name));
                            } else if (writerAction == Writer.WriterAction.WRITE) {
                                while (removed.contains(name)) {
                                    ii = random.nextInt(10);
                                    name = "name" + ii;
                                    phone = "phone" + ii;
                                }
                                removed.remove(name);
                                removed.remove(phone);
                            }
                            Writer writer = new Writer(database, writerAction, name, phone);
                            executor.submit(writer);
                        }
                    }
                }
                Thread.sleep(10000);
                System.out.println("_____________________________________________________________");

                executor.shutdown();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
