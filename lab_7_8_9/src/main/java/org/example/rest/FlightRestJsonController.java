package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.example.controller.ControllerFactoryImpl;
import org.example.controller.FlightController;
import org.example.dto.FlightDto;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Path("/flight")
public class FlightRestJsonController {
    private final FlightController flightController;

    public FlightRestJsonController() throws SQLException {
        var factory = new ControllerFactoryImpl();flightController = factory.getFlightController();
    }

    @GET
    @Path("/all/{airlineName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FlightDto> getAllFlightsByAirlineName(@PathParam("airlineName") String airlineName) {
        return flightController.findAllByAirline(airlineName);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FlightDto getFlightById(@PathParam("id") String id) {
        return flightController.get(UUID.fromString(id));
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FlightDto createFlight(FlightDto flightDto) throws Exception {
        return flightController.create(flightDto);
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateFlight(FlightDto flightDto) throws Exception {
        flightController.update(flightDto);
    }

    @DELETE
    @Path("/delete/{id}")
    public void deleteFlight(@PathParam("id") String id) throws Exception {
        flightController.delete(UUID.fromString(id));
    }
}
