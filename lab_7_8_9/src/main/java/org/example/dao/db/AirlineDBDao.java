package org.example.dao.db;

import org.example.dao.AirlineDao;
import org.example.model.Airline;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AirlineDBDao extends DBDao<Airline, UUID> implements AirlineDao {
    private final FlightDBDao flightDao;
    protected AirlineDBDao(Connection con, String tableName) throws SQLException {
        super(con, tableName);
        this.flightDao = new FlightDBDao(con, "flight");
    }

    @Override
    protected void createTableIfNotExists() throws SQLException {
        var statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS airline (" +
                "airline_id BINARY(16) PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "code VARCHAR(255) NOT NULL," +
                "country VARCHAR(255) NOT NULL" +
                ");");
        statement.executeUpdate();
    }

    @Override
    public Airline create(Airline entity) throws Exception {
        var statement = con.prepareStatement("INSERT INTO airline (airline_id, name, code, country) VALUES (UUID_TO_BIN(?), ?, ?, ?);");
        statement.setString(1, entity.getAirline_id().toString());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getCode());
        statement.setString(4, entity.getCountry());

        statement.executeUpdate();

        if (entity.getFlights() == null) {
            entity.setFlights(new ArrayList<>());
        }
        for (var flight : entity.getFlights()) {
            flight.setAirline_id(entity.getAirline_id());
            flightDao.create(flight);
        }

        return entity;
    }

    @Override
    public Airline read(UUID uuid) throws Exception {
        var statement = con.prepareStatement("SELECT BIN_TO_UUID(airline_id), name, code, country FROM airline WHERE airline_id = UUID_TO_BIN(?);");
        statement.setString(1, uuid.toString());

        var resultSet = statement.executeQuery();
        if (resultSet.next()) {
            var airline = new Airline();
            airline.setAirline_id(UUID.fromString(resultSet.getString(1)));
            airline.setName(resultSet.getString(2));
            airline.setCode(resultSet.getString(3));
            airline.setCountry(resultSet.getString(4));
            airline.setFlights(flightDao.findAllByAirline(airline.getAirline_id()));
            return airline;
        }
        return null;
    }

    @Override
    public void update(Airline entity) throws Exception {
        var statement = con.prepareStatement("UPDATE airline SET name = ?, code = ?, country = ? WHERE airline_id = UUID_TO_BIN(?);");
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getCode());
        statement.setString(3, entity.getCountry());
        statement.setString(4, entity.getAirline_id().toString());

        statement.executeUpdate();

        if (entity.getFlights() == null) {
            entity.setFlights(new ArrayList<>());
        }
        for (var flight : entity.getFlights()) {
            if (flightDao.read(flight.getFlight_id()) == null) {
                flightDao.create(flight);
                continue;
            }
            flight.setAirline_id(entity.getAirline_id());
            flightDao.update(flight);
        }
    }

    @Override
    public void delete(UUID uuid) throws Exception {
        var statement = con.prepareStatement("DELETE FROM airline WHERE airline_id = UUID_TO_BIN(?);");
        statement.setString(1, uuid.toString());

        statement.executeUpdate();
    }

    @Override
    public List<Airline> findAll() throws Exception {
        var statement = con.prepareStatement("SELECT BIN_TO_UUID(airline_id), name, code, country FROM airline;");
        var resultSet = statement.executeQuery();

        var airlines = new ArrayList<Airline>();
        while (resultSet.next()) {
            var airline = new Airline();
            airline.setAirline_id(UUID.fromString(resultSet.getString(1)));
            airline.setName(resultSet.getString(2));
            airline.setCode(resultSet.getString(3));
            airline.setCountry(resultSet.getString(4));
            // airline.setFlights(flightDao.findAllByAirline(airline.getAirline_id()));
            airlines.add(airline);
        }
        return airlines;
    }
}
