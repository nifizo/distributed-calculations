package org.example.mapper;

import org.example.dto.AirlineDto;
import org.example.model.Airline;

import java.util.ArrayList;
import java.util.List;

public class AirlineMapper extends Mapper<Airline, AirlineDto> {
    private final FlightMapper flightMapper = new FlightMapper();
    @Override
    public AirlineDto toDTO(Airline entity) {
        AirlineDto airlineDto = new AirlineDto();
        airlineDto.setName(entity.getName());
        airlineDto.setCode(entity.getCode());
        airlineDto.setCountry(entity.getCountry());

        if (entity.getFlights() == null) {
            airlineDto.setFlights(new ArrayList<>());
        } else {
            airlineDto.setFlights(new ArrayList<>());
            for (int i = 0; i < entity.getFlights().size(); i++) {
                airlineDto.getFlights().add(flightMapper.toDTO(entity.getFlights().get(i)));
            }
        }
        return airlineDto;
    }

    @Override
    public Airline toEntity(AirlineDto airlineDto) {
        Airline airline = new Airline();
        if (airlineDto.getAirlineId() != null) {
            airline.setId(airlineDto.getAirlineId());
        }
        airline.setName(airlineDto.getName());
        airline.setCode(airlineDto.getCode());
        airline.setCountry(airlineDto.getCountry());

        if (airlineDto.getFlights() == null) {
            airlineDto.setFlights(new ArrayList<>());
        } else {
            airline.setFlights(new ArrayList<>());
            for (int i = 0; i < airlineDto.getFlights().size(); i++) {
                airline.getFlights().add(flightMapper.toEntity(airlineDto.getFlights().get(i)));
            }
        }
        return airline;
    }
}
