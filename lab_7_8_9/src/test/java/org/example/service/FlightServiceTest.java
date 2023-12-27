package org.example.service;

import org.example.dao.AirlineDao;
import org.example.dao.FlightDao;
import org.example.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class FlightServiceTest {
    private FlightDao dao;
    private AirlineDao airlineDao;
    private FlightServiceImpl service;

    @BeforeEach
    public void setUp() {
        dao = Mockito.mock(FlightDao.class);
        airlineDao = Mockito.mock(AirlineDao.class);
        service = new FlightServiceImpl(dao, airlineDao);
    }
    @Test
    public void testCreate() throws Exception {
        Flight flight = new Flight();
        service.create(flight);
        verify(dao, times(1)).create(flight);
    }

    @Test
    public void testFindAll() throws Exception {
        Flight flight1 = new Flight();
        Flight flight2 = new Flight();
        when(dao.findAll()).thenReturn(Arrays.asList(flight1, flight2));
        List<Flight> result = service.findAll();
        assertEquals(2, result.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void testFindAllByOrigin() throws Exception {
        Flight flight1 = new Flight();
        flight1.setOrigin("Kyiv");
        Flight flight2 = new Flight();
        flight2.setOrigin("Lviv");
        when(dao.findAll()).thenReturn(Arrays.asList(flight1, flight2));
        List<Flight> result = service.findAllByOrigin("Kyiv");
        assertEquals(1, result.size());
        assertEquals("Kyiv", result.get(0).getOrigin());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void testFindAllByDestination() throws Exception {
        Flight flight1 = new Flight();
        flight1.setDestination("Kyiv");
        Flight flight2 = new Flight();
        flight2.setDestination("Lviv");
        when(dao.findAll()).thenReturn(Arrays.asList(flight1, flight2));
        List<Flight> result = service.findAllByDestination("Kyiv");
        assertEquals(1, result.size());
        assertEquals("Kyiv", result.get(0).getDestination());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void testDelete() throws Exception {
        UUID id = UUID.randomUUID();
        service.delete(id);
        verify(dao, times(1)).delete(id);
    }

    @Test
    public void testUpdate() throws Exception {
        Flight flight = new Flight();
        service.update(flight);
        verify(dao, times(1)).update(flight);
    }

    @Test
    public void testContainsId() throws Exception {
        UUID id = UUID.randomUUID();
        when(dao.read(id)).thenReturn(new Flight());
        assertTrue(service.containsId(id));
        verify(dao, times(1)).read(id);
    }

    @Test
    public void testChangeSource() {
        FlightDao newDao = Mockito.mock(FlightDao.class);
        service.changeSource(newDao);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testGet() throws Exception {
        UUID id = UUID.randomUUID();
        Flight flight = new Flight();
        when(dao.read(id)).thenReturn(flight);
        Flight result = service.get(id);
        assertEquals(flight, result);
        verify(dao, times(1)).read(id);
    }
}
