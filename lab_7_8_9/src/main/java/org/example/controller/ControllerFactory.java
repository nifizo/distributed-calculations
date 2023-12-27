package org.example.controller;

import java.sql.SQLException;

public interface ControllerFactory {
    AirlineController getAirlineController() throws SQLException;
    FlightController getFlightController() throws SQLException;
}
