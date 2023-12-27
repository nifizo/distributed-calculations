package org.example.dao;

import org.example.dao.db.AirlineDBDao;
import org.example.dao.db.DAOManager;
import org.example.model.Airline;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class AirlineDBDaoTest {

        private AirlineDBDao airlineDBDao;
        private int dataBaseSize;

        @BeforeEach
        public void setUp() throws Exception {
            var manager = DAOManager.getInstance();
            airlineDBDao = (AirlineDBDao) manager.getDAO(DAOManager.Table.AIRLINE);
            dataBaseSize = airlineDBDao.findAll().size();
        }

        @Test
        public void testCreate() throws Exception {
            var airline = new Airline();
            airline.setName("Test Airline");
            airline.setCode("Test Code");
            airline.setCountry("Test Country");

            airlineDBDao.create(airline);
            var airlines = airlineDBDao.findAll();

            assertEquals(dataBaseSize + 1, airlines.size());
            // get airline with the set index
            for (var getAirline : airlines) {
                if (getAirline.getAirline_id() == airline.getAirline_id()) {
                    assertEquals("Test Airline", getAirline.getName());
                    assertEquals("Test Code", getAirline.getCode());
                    assertEquals("Test Country", getAirline.getCountry());
                }
            }
            dataBaseSize++;
        }

        @Test
        public void testUpdate() throws Exception {
            var airline = new Airline();
            airline.setName("Test Airline");
            airline.setCode("Test Code");
            airline.setCountry("Test Country");

            airlineDBDao.create(airline);
            var airlines = airlineDBDao.findAll();
            assertEquals(dataBaseSize + 1, airlines.size());
            dataBaseSize++;

            for (var getAirline : airlines) {
                if (getAirline.getAirline_id() == airline.getAirline_id()) {
                    assertEquals("Test Airline", getAirline.getName());
                    assertEquals("Test Code", getAirline.getCode());
                    assertEquals("Test Country", getAirline.getCountry());
                }
            }
            airline.setName("Updated Airline");
            airline.setCountry("Updated Country");
            airline.setCode("Updated Code");

            airlineDBDao.update(airline);
            airlines = airlineDBDao.findAll();
            assertEquals(dataBaseSize, airlines.size());
            for (var getAirline : airlines) {
                if (getAirline.getAirline_id() == airline.getAirline_id()) {
                    assertEquals("Updated Airline", getAirline.getName());
                    assertEquals("Updated Country", getAirline.getCountry());
                    assertEquals("Updated Code", getAirline.getCode());
                }
            }
        }

        @Test
        public void testDelete() throws Exception {
            var airline = new Airline();
            airline.setName("Test Airline");
            airline.setCountry("Test Country");
            airline.setCode("Test Code");
            airlineDBDao.create(airline);
            dataBaseSize++;
            var airlines = airlineDBDao.findAll();
            assertEquals(dataBaseSize, airlines.size());
            for (var getAirline : airlines) {
                if (getAirline.getAirline_id() == airline.getAirline_id()) {
                    assertEquals("Test Airline", getAirline.getName());
                    assertEquals("Test Country", getAirline.getCountry());
                    assertEquals("Test Code", getAirline.getCode());
                }
            }

            airlineDBDao.delete(airline.getId());
            dataBaseSize--;

            airlines = airlineDBDao.findAll();
            assertEquals(dataBaseSize, airlines.size());
        }

        @Test
        public void testFindAll() throws Exception {
            var airline = new Airline();
            airline.setName("Test Airline");
            airline.setCountry("Test Country");
            airline.setCode("Test Code");

            airlineDBDao.create(airline);
            var airlines = airlineDBDao.findAll();
            assertEquals(dataBaseSize + 1, airlines.size());
            dataBaseSize++;
        }

        @Test
        public void testRead() throws Exception {
            var airline = new Airline();
            airline.setName("Test Airline");
            airline.setCountry("Test Country");
            airline.setCode("Test Code");

            airlineDBDao.create(airline);
            var airlines = airlineDBDao.findAll();
            assertEquals(dataBaseSize + 1, airlines.size());
            dataBaseSize++;

            for (var getAirline : airlines) {
                if (getAirline.getAirline_id() == airline.getAirline_id()) {
                    assertEquals("Test Airline", getAirline.getName());
                    assertEquals("Test Country", getAirline.getCountry());
                    assertEquals("Test Code", getAirline.getCode());
                }
            }

            var airline2 = airlineDBDao.read(airline.getId());
            assertEquals("Test Airline", airline2.getName());
            assertEquals("Test Country", airline2.getCountry());
            assertEquals("Test Code", airline2.getCode());
        }

        @AfterAll
        public static void tearDown() throws Exception {
            var manager = DAOManager.getInstance();
            manager.close();
        }
}
