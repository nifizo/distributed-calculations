package org.example.xml.dom.writer;

import org.example.model.Airlines;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.example.model.Flight;
import org.example.model.Airline;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class AirlinesDomWriter implements Writer<Airlines> {
    @Override
    public void write(String filename, @NotNull Airlines airlines) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement("airlines");
        doc.appendChild(rootElement);

        for (Airline airline : airlines.getAirlines()) {
            Element airlineElement = createAirlineElement(doc, airline);
            rootElement.appendChild(airlineElement);

            var flights = airline.getFlights();
            if (flights == null) {
                continue;
            }

            for (Flight flight : flights) {
                Element flightElement = createFlightElement(doc, flight);
                airlineElement.appendChild(flightElement);
            }
        }

        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(filename));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "WINDOWS-1251");
        transformer.transform(domSource, fileResult);
    }

    private @NotNull Element createFlightElement(@NotNull Document document, @NotNull Flight flight) {
        Element flightElement = document.createElement("flight");
        flightElement.setAttribute("flight_id", flight.getFlight_id().toString());
        flightElement.setAttribute("origin", flight.getOrigin());
        flightElement.setAttribute("destination", flight.getDestination());
        flightElement.setAttribute("flightNumber", flight.getFlightNumber());
        flightElement.setAttribute("departureTime", flight.getDepartureTime().toString());
        flightElement.setAttribute("arrivalTime", flight.getArrivalTime().toString());
        flightElement.setAttribute("airline_id", flight.getAirline_id().toString());
        return flightElement;
    }

    private @NotNull Element createAirlineElement(@NotNull Document document, @NotNull Airline airline) {
        Element airlineElement = document.createElement("airline");
        airlineElement.setAttribute("airline_id", airline.getAirline_id().toString());
        airlineElement.setAttribute("name", airline.getName());
        airlineElement.setAttribute("code", airline.getCode());
        airlineElement.setAttribute("country", airline.getCountry());
        return airlineElement;
    }
}
