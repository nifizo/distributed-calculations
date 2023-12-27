package org.example.service;

import org.example.dao.AirlineDao;
import org.example.model.Airline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AirlineServiceTest {
    private AirlineDao dao;
    private AirlineServiceImpl service;

    @BeforeEach
    public void setUp() {
        dao = Mockito.mock(AirlineDao.class);
        service = new AirlineServiceImpl(dao);
    }

    @Test
    public void testCreate() throws Exception {
        Airline airline = new Airline();
        service.create(airline);
        verify(dao, times(1)).create(airline);
    }

    @Test
    public void testFindAll() throws Exception {
        Airline airline1 = new Airline();
        Airline airline2 = new Airline();
        when(dao.findAll()).thenReturn(Arrays.asList(airline1, airline2));
        List<Airline> result = service.findAll();
        assertEquals(2, result.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void testFindAllByCountry() throws Exception {
        Airline airline1 = new Airline();
        airline1.setCountry("Ukraine");
        Airline airline2 = new Airline();
        airline2.setCountry("USA");
        when(dao.findAll()).thenReturn(Arrays.asList(airline1, airline2));
        List<Airline> result = service.findAllByCountry("Ukraine");
        assertEquals(1, result.size());
        assertEquals("Ukraine", result.get(0).getCountry());
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
        Airline airline = new Airline();
        service.update(airline);
        verify(dao, times(1)).update(airline);
    }

    @Test
    public void testContainsId() throws Exception {
        UUID id = UUID.randomUUID();
        when(dao.read(id)).thenReturn(new Airline());
        assertTrue(service.containsId(id));
        verify(dao, times(1)).read(id);
    }

    @Test
    public void testChangeSource() {
        AirlineDao newDao = Mockito.mock(AirlineDao.class);
        service.changeSource(newDao);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testGet() throws Exception {
        UUID id = UUID.randomUUID();
        Airline airline = new Airline();
        when(dao.read(id)).thenReturn(airline);
        Airline result = service.get(id);
        assertEquals(airline, result);
        verify(dao, times(1)).read(id);
    }
}
