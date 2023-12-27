package org.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class Airline implements IId<UUID>{
    private UUID airline_id;
    private String name;
    private String code;
    private String country;
    private ArrayList<Flight> flights;

    public Airline() {
        this.airline_id = UUID.randomUUID();
        this.name = "";
        this.code = "";
        this.country = "";
        this.flights = new ArrayList<>();
    }

    public Airline(UUID uuid, String name, String code, String country, ArrayList<Flight> flights) {
        this.airline_id = uuid;
        this.name = name;
        this.code = code;
        this.country = country;
        this.flights = flights;
    }

    @Override
    public UUID getId() {
        return airline_id;
    }

    @Override
    public void setId(UUID id) {
        this.airline_id = id;
    }
}
