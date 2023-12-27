package org.example.xml.dom.writer;

import org.example.model.Airline;
import org.example.model.Airlines;
import org.example.model.Flight;
import org.example.xml.validator.ValidatorXSD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AirlinesDomWriterTest {
    private AirlinesDomWriter writer;
    private Airlines airlines;
    private ValidatorXSD validator;
    private String filename = "src\\test\\resources\\test_writer.xml";
    private String xsd = "src\\test\\resources\\schema1.xsd";

    @BeforeEach
    public void setUp() throws SAXException {
        writer = new AirlinesDomWriter();
        airlines = new Airlines();
        validator = new ValidatorXSD(xsd);

        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(new Flight(UUID.randomUUID(), "Origin", "Destination", "FlightNumber", 100L, 200L, UUID.randomUUID()));

        Airline airline = new Airline(UUID.randomUUID(), "Name", "Code", "Country", flights);
        airlines.getAirlines().add(airline);
    }

    @Test
    public void testWrite() throws Exception {
        writer.write(filename, airlines);

        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("airline");
        assertEquals(1, nList.getLength());

        Element airlineElement = (Element) nList.item(0);
        assertEquals(airlines.getAirlines().get(0).getAirline_id().toString(), airlineElement.getAttribute("airline_id"));
        assertEquals(airlines.getAirlines().get(0).getName(), airlineElement.getAttribute("name"));
        assertEquals(airlines.getAirlines().get(0).getCode(), airlineElement.getAttribute("code"));
        assertEquals(airlines.getAirlines().get(0).getCountry(), airlineElement.getAttribute("country"));

        NodeList flightList = airlineElement.getElementsByTagName("flight");
        assertEquals(1, flightList.getLength());

        Element flightElement = (Element) flightList.item(0);
        Flight flight = airlines.getAirlines().get(0).getFlights().get(0);
        assertEquals(flight.getFlight_id().toString(), flightElement.getAttribute("flight_id"));
        assertEquals(flight.getOrigin(), flightElement.getAttribute("origin"));
        assertEquals(flight.getDestination(), flightElement.getAttribute("destination"));
        assertEquals(flight.getFlightNumber(), flightElement.getAttribute("flightNumber"));
        assertEquals(flight.getDepartureTime().toString(), flightElement.getAttribute("departureTime"));
        assertEquals(flight.getArrivalTime().toString(), flightElement.getAttribute("arrivalTime"));
        assertEquals(flight.getAirline_id().toString(), flightElement.getAttribute("airline_id"));
    }

    @AfterEach
    public void testValidator() {
        assertTrue(validator.isValid(filename));
    }
}

