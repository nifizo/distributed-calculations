package org.example.service;

import org.example.dao.FlightDao;
import org.example.model.Flight;

import java.util.List;
import java.util.UUID;

public interface FlightService extends Service<Flight, UUID> {
    @Override
    void create(Flight flight);
    List<Flight> findAll();
    List<Flight> findAllByAirline(UUID airlineId);
    List<Flight> findAllByOrigin(String origin);
    List<Flight> findAllByDestination(String destinationn);
    @Override
    boolean delete(UUID id);
    @Override
    void update(Flight flight);
    boolean containsId(UUID id);
    void changeSource(FlightDao dao);
    @Override
    Flight get(UUID id);
}
