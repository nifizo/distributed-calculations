package org.example.dao.xml;

import org.example.dao.FlightDao;
import org.example.model.Airlines;
import org.example.model.Flight;
import org.example.xml.dom.reader.Reader;
import org.example.xml.dom.writer.Writer;

import javax.xml.validation.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlightXmlDao extends XmlDao<Airlines, Flight, UUID> implements FlightDao {
    public FlightXmlDao(String inputFileXML, Reader<Airlines> reader, Writer<Airlines> writer, Schema schema) {
        super(inputFileXML, reader, writer, schema);
    }

    @Override
    protected List<Flight> toCollection(Airlines container) {
        var result = new ArrayList<Flight>();
        for (var airline : container.getAirlines()) {
            result.addAll(airline.getFlights());
        }
        return result;
    }
}
