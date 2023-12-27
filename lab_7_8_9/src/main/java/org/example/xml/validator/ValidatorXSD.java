package org.example.xml.validator;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class ValidatorXSD {
    private final Schema schema;

    public ValidatorXSD(String xsd) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = schemaFactory.newSchema(new File(xsd));
    }
    public boolean isValid(String xml) {
        try {
            validate(xml);
        } catch (SAXException | IOException e) {
            return false;
        }
        return true;
    }

    public Schema getSchema() {
        return schema;
    }

    public void validate(String xml) throws SAXException, IOException {
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new File(xml)));
    }
}