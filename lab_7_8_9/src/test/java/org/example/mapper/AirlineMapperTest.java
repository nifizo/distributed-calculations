package org.example.mapper;

import org.example.dto.AirlineDto;
import org.example.model.Airline;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AirlineMapperTest {
    private AirlineMapper airlineMapper;

    @BeforeEach
    public void setUp() throws Exception {
        airlineMapper = new AirlineMapper();
    }

    @Test
    public void testToDTO() {
        var airline = new Airline();
        airline.setName("Test Name");
        airline.setCode("Test Code");
        airline.setCountry("Test Country");
        var dto = airlineMapper.toDTO(airline);
        assertEquals(airline.getName(), dto.getName());
        assertEquals(airline.getCode(), dto.getCode());
        assertEquals(airline.getCountry(), dto.getCountry());
    }

    @Test
    public void testToEntity() {
        var dto = new AirlineDto();
        dto.setName("Test Name");
        dto.setCode("Test Code");
        dto.setCountry("Test Country");

        var airline = airlineMapper.toEntity(dto);
        assertEquals(dto.getName(), airline.getName());
        assertEquals(dto.getCode(), airline.getCode());
        assertEquals(dto.getCountry(), airline.getCountry());
    }
}
