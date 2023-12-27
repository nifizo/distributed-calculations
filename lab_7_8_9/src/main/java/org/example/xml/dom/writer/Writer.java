package org.example.xml.dom.writer;

public interface Writer<T> {
    void write(String filename, T data) throws Exception;
}
