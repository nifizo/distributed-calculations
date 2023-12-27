package org.example.service;

import org.example.dao.AirlineDao;
import org.example.dao.xml.AirlineXmlDao;
import org.example.model.Airline;
import org.example.model.Airlines;
import org.example.xml.dom.reader.AirlinesDomReader;
import org.example.xml.dom.reader.Reader;
import org.example.xml.dom.writer.AirlinesDomWriter;
import org.example.xml.dom.writer.Writer;
import org.example.xml.validator.ValidatorXSD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AirlineServiceXmlTest {
    private AirlineDao dao;
    private AirlineServiceImpl service;
    private final String filename = "src\\test\\resources\\test_airline_service.xml";
    private final String xsd = "src\\test\\resources\\schema1.xsd";

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize your Reader, Writer and Schema here
        Reader<Airlines> reader = new AirlinesDomReader();
        Writer<Airlines> writer = new AirlinesDomWriter();
        Schema schema = new ValidatorXSD(xsd).getSchema();
        dao = new AirlineXmlDao(filename, reader, writer, schema);
        service = new AirlineServiceImpl(dao);

        Airline airline = new Airline(UUID.randomUUID(), "Name", "Code", "Country", null);
        Airlines airlines = new Airlines();
        airlines.getAirlines().add(airline);

        writer.write(filename, airlines);
    }

    @Test
    public void testCreate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        Airline result = service.get(airline.getId());
        assertEquals(airline, result);
    }

    @Test
    public void testFindAll() {
        Airline airline1 = new Airline();
        airline1.setId(UUID.randomUUID());
        airline1.setName("Test Airline 1");
        airline1.setCode("TA1");
        airline1.setCountry("Test Country 1");

        Airline airline2 = new Airline();
        airline2.setId(UUID.randomUUID());
        airline2.setName("Test Airline 2");
        airline2.setCode("TA2");
        airline2.setCountry("Test Country 2");

        service.create(airline1);
        service.create(airline2);

        assertEquals(3, service.findAll().size());
    }

    @Test
    public void testFindAllByCountry() {
        Airline airline1 = new Airline();
        airline1.setId(UUID.randomUUID());
        airline1.setName("Test Airline 1");
        airline1.setCode("TA1");
        airline1.setCountry("Test Country 1");

        Airline airline2 = new Airline();
        airline2.setId(UUID.randomUUID());
        airline2.setName("Test Airline 2");
        airline2.setCode("TA2");
        airline2.setCountry("Test Country 2");

        service.create(airline1);
        service.create(airline2);

        assertEquals(1, service.findAllByCountry("Test Country 1").size());
    }

    @Test
    public void testDelete() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        assertTrue(service.delete(airline.getId()));
    }

    @Test
    public void testUpdate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        airline.setName("Updated Test Airline");
        service.update(airline);

        Airline result = service.get(airline.getId());
        assertEquals(airline, result);
    }

    @Test
    public void testContainsId() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        assertTrue(service.containsId(airline.getId()));
    }

    @Test
    public void testChangeSource() throws SAXException {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        Reader<Airlines> reader = new AirlinesDomReader();
        Writer<Airlines> writer = new AirlinesDomWriter();
        Schema schema = new ValidatorXSD(xsd).getSchema();
        AirlineDao newDao = new AirlineXmlDao(filename, reader, writer, schema);
        service.changeSource(newDao);

        Airline result = service.get(airline.getId());
        assertEquals(airline, result);
    }

    @Test
    public void testGet() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        Airline result = service.get(airline.getId());
        assertEquals(airline, result);
    }

    @Test
    public void testGetNull() throws Exception {
        // expect exception
        assertThrows(RuntimeException.class, () -> {
            service.get(UUID.randomUUID());
        });
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
