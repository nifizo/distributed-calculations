package org.example.xml.dom.reader;

import org.example.model.Airline;
import org.example.model.Airlines;
import org.example.model.Flight;
import org.example.xml.dom.writer.AirlinesDomWriter;
import org.example.xml.validator.ValidatorXSD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AirlinesDomReaderTest {
    private AirlinesDomReader reader;
    private ValidatorXSD validator;
    private final String filename = "src\\test\\resources\\test_reader.xml";
    private final String xsd = "src\\test\\resources\\schema1.xsd";

    @BeforeEach
    public void setUp() throws SAXException, ParserConfigurationException, TransformerException {
        reader = new AirlinesDomReader();
        validator = new ValidatorXSD(xsd);

        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(new Flight(UUID.randomUUID(), "Origin", "Destination", "FlightNumber", 100L, 200L, UUID.randomUUID()));

        Airline airline = new Airline(UUID.randomUUID(), "Name", "Code", "Country", flights);
        Airlines airlines = new Airlines();
        airlines.getAirlines().add(airline);

        AirlinesDomWriter writer = new AirlinesDomWriter();
        writer.write(filename, airlines);
    }

    @Test
    public void testRead() throws ParserConfigurationException, IOException, SAXException {
        Airlines airlines = reader.read(filename, validator.getSchema());

        assertEquals(1, airlines.getAirlines().size());

        Airline airline = airlines.getAirlines().get(0);
        assertEquals("Name", airline.getName());
        assertEquals("Code", airline.getCode());
        assertEquals("Country", airline.getCountry());

        assertEquals(1, airline.getFlights().size());

        Flight flight = airline.getFlights().get(0);
        assertEquals("Origin", flight.getOrigin());
        assertEquals("Destination", flight.getDestination());
        assertEquals("FlightNumber", flight.getFlightNumber());
    }

    @AfterEach
    public void testValidator() {
        assertTrue(validator.isValid(filename));
    }
}
