package org.example.dto;

import lombok.Data;
import org.example.model.IId;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Data
public class FlightDto implements java.io.Serializable, IId<UUID> {
    @Nullable
    private UUID flightId = null;
    private String origin;
    private String destination;
    private String flightNumber;
    private UUID airlineId;
    private Long departureTime;
    private Long arrivalTime;

    @Override
    public String toString() {
        return "FlightDto{" +
                "flightId='" + (flightId != null ? flightId + "'"  : " ") +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", airlineId=" + airlineId +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }

    @Override
    public UUID getId() {
        return flightId;
    }

    @Override
    public void setId(UUID id) {
        this.flightId = id;
    }
}
