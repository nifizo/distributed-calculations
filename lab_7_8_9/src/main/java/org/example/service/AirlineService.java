package org.example.service;

import org.example.dao.AirlineDao;
import org.example.model.Airline;

import java.util.List;
import java.util.UUID;

public interface AirlineService extends Service<Airline, UUID> {
    @Override
    void create(Airline airline);
    List<Airline> findAll();
    List<Airline> findAllByCountry(String country);
    List<Airline> findByName(String name);
    @Override
    boolean delete(UUID id);
    @Override
    void update(Airline airline);
    boolean containsId(UUID id);
    void changeSource(AirlineDao dao);
    @Override
    Airline get(UUID id);
}
