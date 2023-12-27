package org.example;

import java.io.IOException;

public class Reader extends Thread{
    protected final Database database;

    public enum ReaderAction {
        READ,
        READ_BY_PHONE
    }
    protected ReaderAction action;
    protected final String name;
    protected final String phone;

    public Reader(Database database, ReaderAction action, String name, String phone) {
        this.database = database;
        this.action = action;
        this.name = name;
        this.phone = phone;
    }

    public Reader(Database database, String name, String phone) {
        this.database = database;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public void run() {
        try {
            switch (action) {
                case READ:
                    System.out.println("Read: " + database.read(name) + " " + name);
                    break;
                case READ_BY_PHONE:
                    System.out.println("Read by phone: " + database.readByPhone(phone) + " " + phone);
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
