package org.example.controller;

import org.example.dto.AirlineDto;
import org.example.mapper.AirlineMapper;
import org.example.model.Airline;
import org.example.service.AirlineService;

import java.util.List;
import java.util.UUID;

public class AirlineController extends BaseController<Airline, AirlineDto, UUID>{
    public AirlineController(AirlineService service, AirlineMapper mapper) {
        super(service, mapper);
    }

    public List<AirlineDto> findAll() throws Exception {
        return ((AirlineService)service).findAll().stream().map(mapper::toDTO).toList();
    }
}
