package org.example.dto;

import lombok.Data;
import org.example.model.IId;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;


@Data
public class AirlineDto implements java.io.Serializable, IId<UUID> {
    @Nullable
    private UUID AirlineId = null;
    private String name;
    private String code;
    private String country;
    private ArrayList<FlightDto> flights;

    @Override
    public String toString() {
        return "AirlineDto{" +
                "AirlineId='" + (AirlineId != null ? AirlineId +"'"  : " ") +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", country='" + country + '\'' +
                ", flights=" + (flights != null ? flights.size() : 0) +
                '}';
    }

    @Override
    public UUID getId() {
        return AirlineId;
    }

    @Override
    public void setId(UUID id) {
        this.AirlineId = id;
    }
}
