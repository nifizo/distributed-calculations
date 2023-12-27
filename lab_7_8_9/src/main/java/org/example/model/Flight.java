package org.example.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Flight implements IId<UUID> {
    private UUID flight_id;
    private String origin;
    private String destination;
    private String flightNumber;
    private Long departureTime;
    private Long arrivalTime;
    private UUID airline_id;

    public Flight() {
        this.flight_id = UUID.randomUUID();
        this.origin = "";
        this.destination = "";
        this.flightNumber = "";
        this.departureTime = 0L;
        this.arrivalTime = 0L;
        this.airline_id = null;
    }

    public Flight(UUID uuid, String origin, String destination, String flightNumber, long l, long l1, UUID uuid1) {
        this.flight_id = uuid;
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.departureTime = l;
        this.arrivalTime = l1;
        this.airline_id = uuid1;
    }

    @Override
    public UUID getId() {
        return flight_id;
    }

    @Override
    public void setId(UUID id) {
        this.flight_id = id;
    }
}
