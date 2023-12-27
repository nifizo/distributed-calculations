package org.example.service;

public interface Service<T, Id> {
    void create(T t);
    T get(Id id);
    void update(T t);
    boolean delete(Id id);
}
