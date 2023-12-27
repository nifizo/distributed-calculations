package org.example.controller;

import org.example.dao.AirlineDao;
import org.example.dao.FlightDao;
import org.example.dao.db.DAOManager;
import org.example.dto.FlightDto;
import org.example.mapper.FlightMapper;
import org.example.model.Flight;
import org.example.service.AirlineService;
import org.example.service.AirlineServiceImpl;
import org.example.service.FlightService;
import org.example.service.FlightServiceImpl;

import java.sql.SQLException;
import java.util.*;

@javax.jws.WebService(endpointInterface = "org.example.controller.FlightController")
public class FlightController extends BaseController<Flight, FlightDto, UUID> {
    private final AirlineService airlineService;
    public FlightController() throws SQLException {
        super();
        var daoManager = DAOManager.getInstance();

        FlightDao flightDao = (FlightDao) daoManager.getDAO(DAOManager.Table.FLIGHT);
        AirlineDao airlineDao = (AirlineDao) daoManager.getDAO(DAOManager.Table.AIRLINE);

        this.service = new FlightServiceImpl(flightDao, airlineDao);
        this.airlineService = new AirlineServiceImpl(airlineDao);
        this.mapper = new FlightMapper();
    }
    public FlightController(FlightService service, FlightMapper mapper, AirlineService airlineService) {
        super(service, mapper);
        this.airlineService = airlineService;
    }
    public List<FlightDto> findAllByAirlineId(UUID airlineId) throws NoSuchElementException {
        return ((FlightService)service).findAllByAirline(airlineId).stream().map(mapper::toDTO).toList();
    }

    public List<FlightDto> findAllByAirline(String airlineName) throws NoSuchElementException {
        return ((FlightService)service).findAllByAirline(airlineService.findByName(airlineName).get(0).getId()).stream().map(mapper::toDTO).toList();
    }
}
