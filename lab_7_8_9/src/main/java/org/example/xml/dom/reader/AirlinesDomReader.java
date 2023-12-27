package org.example.xml.dom.reader;

import org.example.model.Airline;
import org.example.model.Airlines;
import org.example.model.Flight;
import org.example.xml.validator.AirlinesParsingErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AirlinesDomReader implements Reader<Airlines> {
    @Override
    public Airlines read(String filename, Schema schema) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setSchema(schema);
        db = dbf.newDocumentBuilder();
        db.setErrorHandler(new AirlinesParsingErrorHandler());
        Document document = db.parse(new File(filename));
        return parseDocument(document);
    }

    private Airlines parseDocument(Document document) {
        Airlines airlines = new Airlines();
        Element root = document.getDocumentElement();
        if (root.getTagName().equals("airlines")) {
            NodeList airlineNodes = root.getElementsByTagName("airline");
            for (int i = 0; i < airlineNodes.getLength(); i++) {
                Element airlineElement = (Element) airlineNodes.item(i);
                airlines.getAirlines().add(parseAirline(airlineElement));
            }
        } else {
            throw new RuntimeException("Root element is not airlines");
        }
        return airlines;
    }

    private Airline parseAirline(Element airlineElement) {
        var airline = new Airline();
        airline.setAirline_id(UUID.fromString(airlineElement.getAttribute("airline_id")));
        airline.setName(airlineElement.getAttribute("name"));
        airline.setCode(airlineElement.getAttribute("code"));
        airline.setCountry(airlineElement.getAttribute("country"));
        airline.setFlights(new ArrayList<>());

        NodeList flightNodes = airlineElement.getElementsByTagName("flight");
        for (int i = 0; i < flightNodes.getLength(); i++) {
            Element flight = (Element) flightNodes.item(i);
            airline.getFlights().add(parseFlight(flight));
        }

        return airline;
    }

    private Flight parseFlight(Element flightElement) {
        Flight flight = new Flight();
        flight.setFlight_id(UUID.fromString(flightElement.getAttribute("flight_id")));
        flight.setOrigin(flightElement.getAttribute("origin"));
        flight.setDestination(flightElement.getAttribute("destination"));
        flight.setFlightNumber(flightElement.getAttribute("flightNumber"));
        flight.setDepartureTime(Long.parseLong(flightElement.getAttribute("departureTime")));
        flight.setArrivalTime(Long.parseLong(flightElement.getAttribute("arrivalTime")));
        flight.setAirline_id(UUID.fromString(flightElement.getAttribute("airline_id")));
        return flight;
    }
}
