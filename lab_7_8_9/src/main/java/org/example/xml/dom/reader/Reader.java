package org.example.xml.dom.reader;

import javax.xml.validation.Schema;

public interface Reader<T> {

    T read(String filename, Schema schema) throws Exception;
}
