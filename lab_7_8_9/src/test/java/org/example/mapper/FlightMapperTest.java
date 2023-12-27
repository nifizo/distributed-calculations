package org.example.mapper;

import org.example.model.Flight;
import org.example.dto.FlightDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class FlightMapperTest {
    private FlightMapper flightMapper;

    @BeforeEach
    public void setUp() throws Exception {
        flightMapper = new FlightMapper();
    }

    @Test
    public void testToDTO() {
        var flight = new Flight();
        flight.setOrigin("Test Origin");
        flight.setDestination("Test Destination");
        flight.setFlightNumber("Test Flight Number");
        flight.setDepartureTime(0L);
        flight.setArrivalTime(0L);
        var dto = flightMapper.toDTO(flight);
        assertEquals(flight.getOrigin(), dto.getOrigin());
        assertEquals(flight.getDestination(), dto.getDestination());
        assertEquals(flight.getFlightNumber(), dto.getFlightNumber());
        assertEquals(flight.getAirline_id(), dto.getAirlineId());
        assertEquals(flight.getDepartureTime() / 1000, dto.getDepartureTime() / 1000);
        assertEquals(flight.getArrivalTime() / 1000, dto.getArrivalTime() / 1000);
    }

    @Test
    public void testToEntity() {
        var dto = new FlightDto();
        dto.setOrigin("Test Origin");
        dto.setDestination("Test Destination");
        dto.setFlightNumber("Test Flight Number");
        dto.setDepartureTime(0L);
        dto.setArrivalTime(0L);
        var flight = flightMapper.toEntity(dto);
        assertEquals(dto.getOrigin(), flight.getOrigin());
        assertEquals(dto.getDestination(), flight.getDestination());
        assertEquals(dto.getFlightNumber(), flight.getFlightNumber());
        assertEquals(dto.getAirlineId(), flight.getAirline_id());
        assertEquals(dto.getDepartureTime() / 1000, flight.getDepartureTime() / 1000);
        assertEquals(dto.getArrivalTime() / 1000, flight.getArrivalTime() / 1000);
    }
}
