package org.example.mapper;

import org.example.dto.FlightDto;
import org.example.model.Flight;

public class FlightMapper extends Mapper<Flight, FlightDto> {
    @Override
    public FlightDto toDTO(Flight entity) {
        FlightDto dto = new FlightDto();
        dto.setOrigin(entity.getOrigin());
        dto.setFlightNumber(entity.getFlightNumber());
        dto.setDestination(entity.getDestination());
        dto.setAirlineId(entity.getAirline_id());
        dto.setDepartureTime(entity.getDepartureTime());
        dto.setArrivalTime(entity.getArrivalTime());
        return dto;
    }

    @Override
    public Flight toEntity(FlightDto dto) {
        Flight entity = new Flight();
        if (dto.getFlightId() != null) {
            entity.setId(dto.getFlightId());
        }
        entity.setOrigin(dto.getOrigin());
        entity.setDestination(dto.getDestination());
        entity.setFlightNumber(dto.getFlightNumber());
        entity.setAirline_id(dto.getAirlineId());
        entity.setDepartureTime(dto.getDepartureTime());
        entity.setArrivalTime(dto.getArrivalTime());
        return entity;
    }
}
