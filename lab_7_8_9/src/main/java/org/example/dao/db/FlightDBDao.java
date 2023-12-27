package org.example.dao.db;

import org.example.dao.FlightDao;
import org.example.model.Flight;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlightDBDao extends DBDao<Flight, UUID> implements FlightDao {
    protected FlightDBDao(Connection con, String tableName) throws SQLException {
        super(con, tableName);
    }

    @Override
    public Flight create(Flight entity) throws Exception {
        var statement = con.prepareStatement("INSERT INTO flight (flight_id, airline_id, origin, destination, flight_number, departure_time, arrival_time) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, ?, ?, ?);");
        statement.setString(1, entity.getFlight_id().toString());
        statement.setString(2, entity.getAirline_id().toString());
        statement.setString(3, entity.getOrigin());
        statement.setString(4, entity.getDestination());
        statement.setString(5, entity.getFlightNumber());

        // convert departureTime and arrivalTime to to datetime
        var departureTime = new java.sql.Timestamp(entity.getDepartureTime());
        var arrivalTime = new java.sql.Timestamp(entity.getArrivalTime());
        statement.setTimestamp(6, departureTime);
        statement.setTimestamp(7, arrivalTime);

        statement.executeUpdate();
        return entity;
    }

    @Override
    public Flight read(UUID uuid) throws Exception {
        var statement = con.prepareStatement("SELECT BIN_TO_UUID(flight_id), BIN_TO_UUID(airline_id), origin, destination, flight_number, departure_time, arrival_time FROM flight WHERE flight_id = UUID_TO_BIN(?);");
        statement.setString(1, uuid.toString());

        var resultSet = statement.executeQuery();
        if (resultSet.next()) {
            var flight = new Flight();
            flight.setFlight_id(UUID.fromString(resultSet.getString(1)));
            flight.setAirline_id(UUID.fromString(resultSet.getString(2)));
            flight.setOrigin(resultSet.getString(3));
            flight.setDestination(resultSet.getString(4));
            flight.setFlightNumber(resultSet.getString(5));

            // convert departureTime and arrivalTime to long
            flight.setDepartureTime(resultSet.getTimestamp(6).getTime());
            flight.setArrivalTime(resultSet.getTimestamp(7).getTime());
            return flight;
        }
        return null;
    }

    @Override
    public void update(Flight entity) throws Exception {
        var statement = con.prepareStatement("UPDATE flight SET airline_id = UUID_TO_BIN(?), origin = ?, destination = ?, flight_number= ?, departure_time = ?, arrival_time = ? WHERE flight_id = UUID_TO_BIN(?);");
        statement.setString(1, entity.getAirline_id().toString());
        statement.setString(2, entity.getOrigin());
        statement.setString(3, entity.getDestination());
        statement.setString(4, entity.getFlightNumber());

        // convert departureTime and arrivalTime to to datetime
        var departureTime = new java.sql.Timestamp(entity.getDepartureTime());
        var arrivalTime = new java.sql.Timestamp(entity.getArrivalTime());
        statement.setTimestamp(5, departureTime);
        statement.setTimestamp(6, arrivalTime);
        statement.setString(7, entity.getFlight_id().toString());

        statement.executeUpdate();
    }

    @Override
    public void delete(UUID uuid) throws Exception {
        var statement = con.prepareStatement("DELETE FROM flight WHERE flight_id = UUID_TO_BIN(?);");
        statement.setString(1, uuid.toString());

        statement.executeUpdate();
    }

    @Override
    public List<Flight> findAll() throws Exception {
        var statement = con.prepareStatement("SELECT BIN_TO_UUID(flight_id), BIN_TO_UUID(airline_id), origin, destination, flight_number, departure_time, arrival_time FROM flight;");

        var resultSet = statement.executeQuery();
        var flights = new java.util.ArrayList<Flight>();
        while (resultSet.next()) {
            var flight = new Flight();
            flight.setFlight_id(UUID.fromString(resultSet.getString(1)));
            flight.setAirline_id(UUID.fromString(resultSet.getString(2)));
            flight.setOrigin(resultSet.getString(3));
            flight.setDestination(resultSet.getString(4));
            flight.setFlightNumber(resultSet.getString(5));

            // convert departureTime and arrivalTime to long
            flight.setDepartureTime(resultSet.getTimestamp(6).getTime());
            flight.setArrivalTime(resultSet.getTimestamp(7).getTime());
            flights.add(flight);
        }
        return flights;
    }

    public ArrayList<Flight> findAllByAirline(UUID airlineId) throws Exception {
        var statement = con.prepareStatement("SELECT BIN_TO_UUID(flight_id), BIN_TO_UUID(airline_id), origin, destination, flight_number, departure_time, arrival_time FROM flight WHERE airline_id = UUID_TO_BIN(?);");
        statement.setString(1, airlineId.toString());

        var resultSet = statement.executeQuery();
        var flights = new java.util.ArrayList<Flight>();
        while (resultSet.next()) {
            var flight = new Flight();
            flight.setFlight_id(UUID.fromString(resultSet.getString(1)));
            flight.setAirline_id(UUID.fromString(resultSet.getString(2)));
            flight.setOrigin(resultSet.getString(3));
            flight.setDestination(resultSet.getString(4));
            flight.setFlightNumber(resultSet.getString(5));

            // convert departureTime and arrivalTime to long
            flight.setDepartureTime(resultSet.getTimestamp(6).getTime());
            flight.setArrivalTime(resultSet.getTimestamp(7).getTime());
            flights.add(flight);
        }
        return flights;
    }

    @Override
    protected void createTableIfNotExists() throws SQLException {
        var statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS flight (" +
                "flight_id BINARY(16) PRIMARY KEY," +
                "airline_id BINARY(16) NOT NULL," +
                "origin VARCHAR(255) NOT NULL," +
                "destination VARCHAR(255) NOT NULL," +
                "flight_number VARCHAR(255) NOT NULL," +
                "departure_time DATETIME NOT NULL," +
                "arrival_time DATETIME NOT NULL," +
                "FOREIGN KEY (airline_id) REFERENCES airline(airline_id)" +
                ");");
        statement.executeUpdate();
    }
}
