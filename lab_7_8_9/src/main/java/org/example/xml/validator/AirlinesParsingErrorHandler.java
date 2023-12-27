package org.example.xml.validator;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class AirlinesParsingErrorHandler implements ErrorHandler {
    @Override
    public void warning(@NotNull SAXParseException e) throws SAXException {
        System.out.println("Warning: Line " + e.getLineNumber() + ":");
        System.out.println(e.getMessage());
    }

    @Override
    public void error(@NotNull SAXParseException e) throws SAXException {
        System.out.println("Error: Line " + e.getLineNumber() + ":");
        System.out.println(e.getMessage());
    }

    @Override
    public void fatalError(@NotNull SAXParseException e) throws SAXException {
        System.out.println("Fatal error: Line " + e.getLineNumber() + ":");
        System.out.println(e.getMessage());
    }
}
