package org.example;

import java.io.IOException;

public class Writer extends Reader{
    public enum WriterAction {
        READER_ACTION,
        WRITE,
        DELETE
    }
    private WriterAction action;

    public Writer(Database database, WriterAction action, String name, String phone) {
        super(database, name, phone);
        this.action = action;
    }

    public Writer(Database database, ReaderAction action, String name, String phone) {
        super(database, action, name, phone);
        this.action = WriterAction.READER_ACTION;
    }

    @Override
    public void run() {
        try {
            switch (action) {
                case WRITE:
                    System.out.println("Write: " + name + " " + phone);
                    database.write(name, phone);
                    break;
                case DELETE:
                    System.out.println("Delete: " + name);
                    database.delete(name);
                    break;
                case READER_ACTION:
                    super.run();
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
