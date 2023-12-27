package org.example.socket.server.handler;

import org.example.controller.AirlineController;
import org.example.controller.FlightController;
import org.example.dto.Request;
import org.example.dto.Response;

import java.util.UUID;

public class RequestHandler {
    private final AirlineController airlineController;
    private final FlightController flightController;

    public RequestHandler(AirlineController airlineController, FlightController flightController) {
        this.airlineController = airlineController;
        this.flightController = flightController;
    }

    public Response handle(Request request) throws Exception {
        switch (request.getMethod()) {
            case GET:
                if (request.getPath().equals("/airline")) {
                    return new Response(Response.ResponseStatus.SUCCESS, airlineController.findAll());
                } else if (request.getPath().startsWith("/airline/name/") && request.getPath().endsWith("/flights")) {
                    var airlineName = request.getPath().split("/")[3];
                    return new Response(Response.ResponseStatus.SUCCESS, flightController.findAllByAirline(airlineName));
                } else if (request.getPath().startsWith("/airline/") && request.getPath().endsWith("/flights")) {
                    var airlineId = UUID.fromString(request.getPath().split("/")[2]);
                    return new Response(Response.ResponseStatus.SUCCESS, flightController.findAllByAirlineId(airlineId));
                } else if (request.getPath().startsWith("/flight/")) {
                    var flightId = UUID.fromString(request.getPath().split("/")[2]);
                    return new Response(Response.ResponseStatus.SUCCESS, flightController.get(flightId));
                } else if (request.getPath().startsWith("/airline/")) {
                    var airlineId2 = UUID.fromString(request.getPath().split("/")[2]);
                    return new Response(Response.ResponseStatus.SUCCESS, airlineController.get(airlineId2));
                } else {
                    return new Response(Response.ResponseStatus.ERROR, "Not found");
                }
            case POST:
                if (request.getPath().equals("/airline")) {
                    var airlineDto = (org.example.dto.AirlineDto) request.getBody();
                    var dto = this.airlineController.create(airlineDto);
                    return new Response(Response.ResponseStatus.SUCCESS, dto);
                } else if (request.getPath().equals("/flight")) {
                    var flightDto = (org.example.dto.FlightDto) request.getBody();
                    var dto = this.flightController.create(flightDto);
                    return new Response(Response.ResponseStatus.SUCCESS, dto);
                } else {
                    return new Response(Response.ResponseStatus.ERROR, "Not found");
                }
            case PUT:
                switch (request.getPath()) {
                    case "/airline" -> {
                        var airlineDto = (org.example.dto.AirlineDto) request.getBody();
                        this.airlineController.update(airlineDto);
                        return new Response(Response.ResponseStatus.SUCCESS, null);
                    }
                    case "/flight" -> {
                        var flightDto = (org.example.dto.FlightDto) request.getBody();
                        this.flightController.update(flightDto);
                        return new Response(Response.ResponseStatus.SUCCESS, null);
                    }
                    default -> new Response(Response.ResponseStatus.ERROR, "Not found");
                }
                break;
            case DELETE:
                if (request.getPath().startsWith("/airline/")) {
                    var airlineId = UUID.fromString(request.getPath().split("/")[2]);
                    this.airlineController.delete(airlineId);
                    return new Response(Response.ResponseStatus.SUCCESS, null);
                } else if (request.getPath().startsWith("/flight/")) {
                    var flightId = UUID.fromString(request.getPath().split("/")[2]);
                    this.flightController.delete(flightId);
                    return new Response(Response.ResponseStatus.SUCCESS, null);
                } else {
                    return new Response(Response.ResponseStatus.ERROR, "Not found");
                }
            case PATCH:
                break;
            case HEAD:
                break;
            case OPTIONS:
                break;
            case TRACE:
                break;
            case CONNECT:
                break;
            default:
                throw new Exception("Unknown request method");

        }
        return new Response(Response.ResponseStatus.ERROR, "Not found");
    }
}
