package org.example.service;

import org.example.dao.AirlineDao;
import org.example.dao.db.DAOManager;
import org.example.dao.FlightDao;
import org.example.model.Airline;
import org.example.model.Flight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FlightServiceDBTest {
    private FlightDao dao;
    private AirlineDao airlinesDao;
    private FlightService service;
    private AirlineService airlineService;

    @BeforeEach
    public void setUp() throws Exception {
        var manager = DAOManager.getInstance();
        dao = (FlightDao) manager.getDAO(DAOManager.Table.FLIGHT);
        airlinesDao = (AirlineDao) manager.getDAO(DAOManager.Table.AIRLINE);
        service = new FlightServiceImpl(dao, airlinesDao);
        airlineService = new AirlineServiceImpl(airlinesDao);

        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);
    }

    @Test
    public void testCreate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Origin");
        flight.setDestination("Destination");
        flight.setFlightNumber("FlightNumber");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Flight result = service.get(flight.getId());
        Assertions.assertEquals(flight, result);
    }

    @Test
    public void testGet() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Origin");
        flight.setDestination("Destination");
        flight.setFlightNumber("FlightNumber");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Flight result = service.get(flight.getId());
        Assertions.assertEquals(flight, result);
    }

    @Test
    public void testUpdate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Origin");
        flight.setDestination("Destination");
        flight.setFlightNumber("FlightNumber");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Flight result = service.get(flight.getId());
        Assertions.assertEquals(flight, result);

        flight.setOrigin("Updated Origin");
        service.update(flight);

        result = service.get(flight.getId());
        Assertions.assertEquals(flight.getFlight_id(), result.getFlight_id());
        Assertions.assertEquals(flight.getOrigin(), result.getOrigin());
        Assertions.assertEquals(flight.getDestination(), result.getDestination());
        Assertions.assertEquals(flight.getFlightNumber(), result.getFlightNumber());
        Assertions.assertEquals(flight.getDepartureTime() / 1000, result.getDepartureTime() / 1000);
        Assertions.assertEquals(flight.getArrivalTime() / 1000, result.getArrivalTime() / 1000);
        Assertions.assertEquals(flight.getAirline_id(), result.getAirline_id());
    }

    @Test
    public void testDelete() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Origin");
        flight.setDestination("Destination");
        flight.setFlightNumber("FlightNumber");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Assertions.assertTrue(service.delete(flight.getId()));
    }

    @Test
public void testContainsId() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Origin");
        flight.setDestination("Destination");
        flight.setFlightNumber("FlightNumber");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Assertions.assertTrue(service.containsId(flight.getId()));
    }

//    @Test
//public void testFindAll() {
//        Airline airline = new Airline();
//        airline.setId(UUID.randomUUID());
//        airlineService.create(airline);
//
//        var previousFlights = service.findAll();
//
//        Flight flight1 = new Flight();
//        flight1.setId(UUID.randomUUID());
//        flight1.setOrigin("Origin1");
//        flight1.setDestination("Destination1");
//        flight1.setFlightNumber("FlightNumber1");
//        flight1.setDepartureTime(0L);
//        flight1.setArrivalTime(0L);
//        flight1.setAirline_id(airline.getId());
//
//        Flight flight2 = new Flight();
//        flight2.setId(UUID.randomUUID());
//        flight2.setOrigin("Origin2");
//        flight2.setDestination("Destination2");
//        flight2.setFlightNumber("FlightNumber2");
//        flight2.setDepartureTime(0L);
//        flight2.setArrivalTime(0L);
//        flight2.setAirline_id(airline.getId());
//
//        service.create(flight1);
//        service.create(flight2);
//
//        List<Flight> flights = new ArrayList<>(previousFlights);
//        flights.add(flight1);
//        flights.add(flight2);
//
//        List<Flight> result = service.findAll();
//        Assertions.assertEquals(flights, result);
//    }

    @Test
public void testFindAllByAirline() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight1 = new Flight();
        flight1.setId(UUID.randomUUID());
        flight1.setOrigin("Origin1");
        flight1.setDestination("Destination1");
        flight1.setFlightNumber("FlightNumber1");
        flight1.setDepartureTime(0L);
        flight1.setArrivalTime(0L);
        flight1.setAirline_id(airline.getId());

        Flight flight2 = new Flight();
        flight2.setId(UUID.randomUUID());
        flight2.setOrigin("Origin2");
        flight2.setDestination("Destination2");
        flight2.setFlightNumber("FlightNumber2");
        flight2.setDepartureTime(0L);
        flight2.setArrivalTime(0L);
        flight2.setAirline_id(airline.getId());

        service.create(flight1);
        service.create(flight2);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);

        List<Flight> result = service.findAllByAirline(airline.getId());
        Assertions.assertEquals(flights.size(), result.size());
    }

    @Test
public void testFindAllByOrigin() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight1 = new Flight();
        flight1.setId(UUID.randomUUID());
        var origin = UUID.randomUUID().toString();
        flight1.setOrigin(origin);
        flight1.setDestination("Destination1");
        flight1.setFlightNumber("FlightNumber1");
        flight1.setDepartureTime(0L);
        flight1.setArrivalTime(0L);
        flight1.setAirline_id(airline.getId());

        Flight flight2 = new Flight();
        flight2.setId(UUID.randomUUID());
        flight2.setOrigin("Origin2");
        flight2.setDestination("Destination2");
        flight2.setFlightNumber("FlightNumber2");
        flight2.setDepartureTime(0L);
        flight2.setArrivalTime(0L);
        flight2.setAirline_id(airline.getId());

        service.create(flight1);
        service.create(flight2);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);

        List<Flight> result = service.findAllByOrigin(origin);
        Assertions.assertEquals(flights, result);
    }

    @Test
public void testFindAllByDestination() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineService.create(airline);

        Flight flight1 = new Flight();
        flight1.setId(UUID.randomUUID());
        flight1.setOrigin("Origin1");
        // random destination
        var destination = UUID.randomUUID().toString();
        flight1.setDestination(destination);
        flight1.setFlightNumber("FlightNumber1");
        flight1.setDepartureTime(0L);
        flight1.setArrivalTime(0L);
        flight1.setAirline_id(airline.getId());

        Flight flight2 = new Flight();
        flight2.setId(UUID.randomUUID());
        flight2.setOrigin("Origin2");
        flight2.setDestination("Destination2");
        flight2.setFlightNumber("FlightNumber2");
        flight2.setDepartureTime(0L);
        flight2.setArrivalTime(0L);
        flight2.setAirline_id(airline.getId());

        service.create(flight1);
        service.create(flight2);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);

        List<Flight> result = service.findAllByDestination(destination);
        Assertions.assertEquals(flights, result);
    }

    @AfterEach
    public void tearDown() {

    }
}

