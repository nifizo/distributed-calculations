package org.example.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.example.controller.ControllerFactoryImpl;
import org.example.controller.FlightController;

import java.sql.SQLException;

//@Path("/flight")
//public class FlightRestController {
//    private final FlightController flightController;
//
//    public FlightRestController() throws SQLException {
//        var factory = new ControllerFactoryImpl();flightController = factory.getFlightController();
//    }
//
//    @GET
//    @Path("/all/{airlineName}")
//    public Response getAllFlightsByAirlineName(@PathParam("airlineName") String airlineName) {
//        return Response.ok(flightController.findAllByAirline(airlineName)).build();
//    }
//}
