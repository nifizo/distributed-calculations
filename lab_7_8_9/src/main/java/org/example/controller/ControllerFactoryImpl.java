package org.example.controller;

import org.example.dao.AirlineDao;
import org.example.dao.FlightDao;
import org.example.dao.db.DAOManager;
import org.example.mapper.AirlineMapper;
import org.example.mapper.FlightMapper;
import org.example.service.AirlineService;
import org.example.service.AirlineServiceImpl;
import org.example.service.FlightService;
import org.example.service.FlightServiceImpl;

import java.sql.SQLException;

public class ControllerFactoryImpl implements ControllerFactory{
    @Override
    public AirlineController getAirlineController() throws SQLException {
        var daoManager = DAOManager.getInstance();

        AirlineDao airlineDao = (AirlineDao) daoManager.getDAO(DAOManager.Table.AIRLINE);
        AirlineService airlineService = new AirlineServiceImpl(airlineDao);
        AirlineMapper airlineMapper = new AirlineMapper();

        return new AirlineController(airlineService, airlineMapper);
    }

    @Override
    public FlightController getFlightController() throws SQLException {
        var daoManager = DAOManager.getInstance();

        FlightDao flightDao = (FlightDao) daoManager.getDAO(DAOManager.Table.FLIGHT);
        AirlineDao airlineDao = (AirlineDao) daoManager.getDAO(DAOManager.Table.AIRLINE);

        FlightService flightService = new FlightServiceImpl(flightDao, airlineDao);
        AirlineService airlineService = new AirlineServiceImpl(airlineDao);

        FlightMapper flightMapper = new FlightMapper();

        return new FlightController(flightService, flightMapper, airlineService);
    }
}
