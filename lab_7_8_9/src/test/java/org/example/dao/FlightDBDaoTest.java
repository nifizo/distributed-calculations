package org.example.dao;

import org.example.dao.db.AirlineDBDao;
import org.example.dao.db.DAOManager;
import org.example.dao.db.FlightDBDao;
import org.example.model.Airline;
import org.example.model.Flight;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class FlightDBDaoTest {

        private FlightDBDao flightDBDao;
        private AirlineDBDao airlineDBDao;
        private int dataBaseSize;

        @BeforeEach
        public void setUp() throws Exception {
            var manager = DAOManager.getInstance();
            flightDBDao = (FlightDBDao) manager.getDAO(DAOManager.Table.FLIGHT);
            airlineDBDao = (AirlineDBDao) manager.getDAO(DAOManager.Table.AIRLINE);

            var airline = new Airline();
            airline.setName("Test Airline");
            airlineDBDao.create(airline);
            dataBaseSize = flightDBDao.findAll().size();
        }

        @Test
        public void testCreate() throws Exception {
            var Airline = airlineDBDao.findAll().get(0);
            var flight = new Flight();
            flight.setOrigin("Test Origin");
            flight.setDestination("Test Destination");
            flight.setFlightNumber("Test Flight Number");
            flight.setDepartureTime(0L);
            flight.setArrivalTime(0L);
            flight.setAirline_id(Airline.getId());
            flightDBDao.create(flight);
            dataBaseSize++;
            var readFlight = flightDBDao.read(flight.getId());
            assertEquals(flight.getId(), readFlight.getId());
            assertEquals(flight.getOrigin(), readFlight.getOrigin());
            assertEquals(flight.getDestination(), readFlight.getDestination());
            assertEquals(flight.getFlightNumber(), readFlight.getFlightNumber());
            assertEquals(flight.getDepartureTime() / 1000, readFlight.getDepartureTime() / 1000);
            assertEquals(flight.getArrivalTime() / 1000, readFlight.getArrivalTime() / 1000);
            assertEquals(flight.getAirline_id(), readFlight.getAirline_id());
        }

        @Test
        public void testRead() throws Exception {
            var Airline = airlineDBDao.findAll().get(0);
            var flight = new Flight();
            flight.setOrigin("Test Origin");
            flight.setDestination("Test Destination");
            flight.setFlightNumber("Test Flight Number");
            flight.setDepartureTime(0L);
            flight.setArrivalTime(0L);
            flight.setAirline_id(Airline.getId());
            flightDBDao.create(flight);
            dataBaseSize++;
            var flights = flightDBDao.findAll();
            assertEquals(dataBaseSize, flights.size());
            var getFlight = flights.get(flights.size() - 1);
            var readFlight = flightDBDao.read(getFlight.getId());
            assertEquals(getFlight.getId(), readFlight.getId());
            assertEquals(getFlight.getOrigin(), readFlight.getOrigin());
            assertEquals(getFlight.getDestination(), readFlight.getDestination());
            assertEquals(getFlight.getFlightNumber(), readFlight.getFlightNumber());
            assertEquals(getFlight.getDepartureTime(), readFlight.getDepartureTime());
            assertEquals(getFlight.getArrivalTime(), readFlight.getArrivalTime());
            assertEquals(getFlight.getAirline_id(), readFlight.getAirline_id());
        }

    @Test
    public void testUpdate() throws Exception {
        var Airline = airlineDBDao.findAll().get(0);
        var flight = new Flight();
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("Test Flight Number");
        flight.setDepartureTime(System.currentTimeMillis());
        flight.setArrivalTime(System.currentTimeMillis() + 3600000); // 1 hour later
        flight.setAirline_id(Airline.getId());
        flightDBDao.create(flight);
        dataBaseSize++;
        var flights = flightDBDao.findAll();
        assertEquals(dataBaseSize, flights.size());
        var getFlight = flights.get(flights.size() - 1);
        getFlight.setOrigin("Test Origin 2");
        getFlight.setDestination("Test Destination 2");
        getFlight.setFlightNumber("Test Flight Number 2");
        getFlight.setDepartureTime(System.currentTimeMillis());
        getFlight.setArrivalTime(System.currentTimeMillis() + 3600000); // 1 hour later
        getFlight.setAirline_id(Airline.getId());
        flightDBDao.update(getFlight);
        var readFlight = flightDBDao.read(getFlight.getId());
        assertEquals(getFlight.getId(), readFlight.getId());
        assertEquals(getFlight.getOrigin(), readFlight.getOrigin());
        assertEquals(getFlight.getDestination(), readFlight.getDestination());
        assertEquals(getFlight.getFlightNumber(), readFlight.getFlightNumber());
        assertEquals(getFlight.getDepartureTime() / 10000, readFlight.getDepartureTime() / 10000);
        assertEquals(getFlight.getArrivalTime() / 10000, readFlight.getArrivalTime() / 10000);
        assertEquals(getFlight.getAirline_id(), readFlight.getAirline_id());
    }


    @Test
        public void testDelete() throws Exception {
            var Airline = airlineDBDao.findAll().get(0);
            var flight = new Flight();
            flight.setOrigin("Test Origin");
            flight.setDestination("Test Destination");
            flight.setFlightNumber("Test Flight Number");
            flight.setDepartureTime(0L);
            flight.setArrivalTime(0L);
            flight.setAirline_id(Airline.getId());
            flightDBDao.create(flight);
            dataBaseSize++;
            var flights = flightDBDao.findAll();
            assertEquals(dataBaseSize, flights.size());
            var getFlight = flights.get(flights.size() - 1);
            flightDBDao.delete(getFlight.getId());
            dataBaseSize--;
            flights = flightDBDao.findAll();
            assertEquals(dataBaseSize, flights.size());
        }

        @Test
        public void testFindAll() throws Exception {
            var Airline = airlineDBDao.findAll().get(0);
            var flight = new Flight();
            flight.setOrigin("Test Origin");
            flight.setDestination("Test Destination");
            flight.setFlightNumber("Test Flight Number");
            flight.setDepartureTime(0L);
            flight.setArrivalTime(0L);
            flight.setAirline_id(Airline.getId());
            flightDBDao.create(flight);
            dataBaseSize++;
            var flights = flightDBDao.findAll();
            assertEquals(dataBaseSize, flights.size());
            var getFlight = flights.get(flights.size() - 1);
            var readFlight = flightDBDao.read(getFlight.getId());
            assertEquals(getFlight.getId(), readFlight.getId());
            assertEquals(getFlight.getOrigin(), readFlight.getOrigin());
            assertEquals(getFlight.getDestination(), readFlight.getDestination());
            assertEquals(getFlight.getFlightNumber(), readFlight.getFlightNumber());
            assertEquals(getFlight.getDepartureTime(), readFlight.getDepartureTime());
            assertEquals(getFlight.getArrivalTime(), readFlight.getArrivalTime());
            assertEquals(getFlight.getAirline_id(), readFlight.getAirline_id());
        }

        @AfterAll
        public static void tearDown() throws Exception {
            var manager = DAOManager.getInstance();
            manager.close();
        }
}
