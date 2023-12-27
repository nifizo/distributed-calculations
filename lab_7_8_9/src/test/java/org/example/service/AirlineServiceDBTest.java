package org.example.service;

import org.example.dao.AirlineDao;
import org.example.dao.db.AirlineDBDao;
import org.example.dao.db.DAOManager;
import org.example.model.Airline;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AirlineServiceDBTest {
    private AirlineDBDao dao;
    private AirlineService service;

    @BeforeEach
    public void setUp() throws Exception {
        var manager = DAOManager.getInstance();
        dao = (AirlineDBDao) manager.getDAO(DAOManager.Table.AIRLINE);
        service = new AirlineServiceImpl(dao);
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
    public void testUpdate() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        Airline result = service.get(airline.getId());
        assertEquals(airline, result);

        airline.setName("Updated Name");
        service.update(airline);

        result = service.get(airline.getId());
        assertEquals(airline, result);
    }

    @Test
    public void testDelete() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setName("Test Airline");
        airline.setCode("TA");
        airline.setCountry("Test Country");

        service.create(airline);

        Airline result = service.get(airline.getId());
        assertEquals(airline, result);

        service.delete(airline.getId());

        assertNull(service.get(airline.getId()));
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
    public void testFindAllByCountry() {
        Airline airline1 = new Airline();
        airline1.setId(UUID.randomUUID());
        airline1.setName("Test Airline 1");
        airline1.setCode("TA1");
        // random country
        var country = UUID.randomUUID().toString();
        airline1.setCountry(country);

        Airline airline2 = new Airline();
        airline2.setId(UUID.randomUUID());
        airline2.setName("Test Airline 2");
        airline2.setCode("TA2");
        airline2.setCountry("Test Country 2");

        service.create(airline1);
        service.create(airline2);

        assertEquals(1, service.findAllByCountry(country).size());
    }

    @AfterEach
    public void tearDown() {

    }
}
