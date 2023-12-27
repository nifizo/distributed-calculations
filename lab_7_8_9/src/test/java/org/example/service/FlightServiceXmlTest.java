package org.example.service;

import org.example.dao.AirlineDao;
import org.example.dao.xml.AirlineXmlDao;
import org.example.dao.FlightDao;
import org.example.dao.xml.FlightXmlDao;
import org.example.model.Airline;
import org.example.model.Airlines;
import org.example.model.Flight;
import org.example.xml.dom.reader.AirlinesDomReader;
import org.example.xml.dom.reader.Reader;
import org.example.xml.dom.writer.AirlinesDomWriter;
import org.example.xml.dom.writer.Writer;
import org.example.xml.validator.ValidatorXSD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FlightServiceXmlTest {
    private FlightDao dao;
    private AirlineDao airlinesDao;
    private FlightServiceImpl service;
    private AirlineServiceImpl airlineServiceXml;
    private final String filename = "src\\test\\resources\\test_flight_service.xml";
    private final String xsd = "src\\test\\resources\\schema1.xsd";

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize your Reader, Writer and Schema here
        Reader<Airlines> reader = new AirlinesDomReader();
        Writer<Airlines> writer = new AirlinesDomWriter();
        Schema schema = new ValidatorXSD(xsd).getSchema();
        dao = new FlightXmlDao(filename, reader, writer, schema);
        airlinesDao = new AirlineXmlDao(filename, reader, writer, schema);
        service = new FlightServiceImpl(dao, airlinesDao);
        airlineServiceXml = new AirlineServiceImpl(new AirlineXmlDao(filename, reader, writer, schema));

        Flight flight = new Flight(UUID.randomUUID(), "Origin", "Destination", "FlightNumber", 0L, 0L, UUID.randomUUID());
        Airlines airlines = new Airlines();
        airlines.getAirlines().add(new Airline(UUID.randomUUID(), "Name", "Code", "Country", new ArrayList<>(List.of(flight))));

        writer.write(filename, airlines);
    }

    @Test
    public void testCreate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("TFN");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Flight result = service.get(flight.getId());
        Assertions.assertEquals(flight, result);
    }

    @Test
    public void testFindAll() {
        List<Flight> result = service.findAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testFindAllByAirline() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight1 = new Flight();
        flight1.setAirline_id(airline.getId());
        service.create(flight1);
        List<Flight> result = service.findAllByAirline(airline.getId());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(airline.getId(), result.get(0).getAirline_id());

    }

    @Test
    public void testFindAllByOrigin() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight1 = new Flight();
        flight1.setOrigin("Kyiv");
        flight1.setAirline_id(airline.getId());
        Flight flight2 = new Flight();
        flight2.setOrigin("Lviv");
        flight2.setAirline_id(airline.getId());
        service.create(flight1);
        service.create(flight2);

        List<Flight> result = service.findAllByOrigin("Kyiv");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Kyiv", result.get(0).getOrigin());
    }

    @Test
    public void testFindAllByDestination() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight1 = new Flight();
        flight1.setDestination("Kyiv");
        flight1.setAirline_id(airline.getId());
        Flight flight2 = new Flight();
        flight2.setDestination("Lviv");
        flight2.setAirline_id(airline.getId());
        service.create(flight1);
        service.create(flight2);

        List<Flight> result = service.findAllByDestination("Kyiv");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Kyiv", result.get(0).getDestination());
    }

    @Test
    public void testDelete() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("TFN");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Assertions.assertTrue(service.delete(flight.getId()));
    }

    @Test
    public void testUpdate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("TFN");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        flight.setOrigin("Updated Origin");
        service.update(flight);

        Flight result = service.get(flight.getId());
        Assertions.assertEquals(flight, result);
    }

    @Test
    public void testContainsId() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("TFN");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Assertions.assertTrue(service.containsId(flight.getId()));
    }

    @Test
    public void testChangeSource() throws SAXException {
        FlightDao dao = new FlightXmlDao(filename, new AirlinesDomReader(), new AirlinesDomWriter(), new ValidatorXSD(xsd).getSchema());
        service.changeSource(dao);
        Assertions.assertEquals(1, service.findAll().size());
    }

    @Test
    public void testGet() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airlineServiceXml.create(airline);

        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("TFN");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        flight.setAirline_id(airline.getId());

        service.create(flight);

        Flight result = service.get(flight.getId());
        Assertions.assertEquals(flight, result);
    }

    @AfterEach
    public void tearDown() {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

