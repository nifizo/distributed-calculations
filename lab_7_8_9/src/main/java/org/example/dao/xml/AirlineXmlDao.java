package org.example.dao.xml;

import org.example.dao.AirlineDao;
import org.example.model.Airline;
import org.example.model.Airlines;
import org.example.xml.dom.reader.Reader;
import org.example.xml.dom.writer.Writer;

import javax.xml.validation.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AirlineXmlDao extends XmlDao<Airlines, Airline, UUID> implements AirlineDao {
    public AirlineXmlDao(String inputFileXML, Reader<Airlines> reader, Writer<Airlines> writer, Schema schema) {
        super(inputFileXML, reader, writer, schema);
    }

    @Override
    protected List<Airline> toCollection(Airlines container) {
        if (container == null) {
            return new ArrayList<>();
        }
        return container.getAirlines();
    }
}
