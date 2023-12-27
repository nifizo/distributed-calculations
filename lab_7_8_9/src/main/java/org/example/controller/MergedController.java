package org.example.controller;

import org.example.dto.AirlineDto;
import org.example.dto.FlightDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface MergedController extends Remote {
    AirlineDto createAirline(AirlineDto airlineDto) throws RemoteException;
    AirlineDto getAirline(UUID id) throws Exception;
    void updateAirline(AirlineDto airlineDto) throws Exception;
    boolean deleteAirline(UUID id) throws Exception;
    List<AirlineDto> findAllAirlines() throws RemoteException;
    FlightDto createFlight(FlightDto flightDto) throws Exception;
    FlightDto getFlight(UUID id) throws Exception;
    void updateFlight(FlightDto flightDto) throws Exception;
    boolean deleteFlight(UUID id) throws Exception;
    List<FlightDto> findAllByAirlineId(UUID airlineId) throws Exception;
    List<FlightDto> findAllByAirline(String airlineName) throws Exception;
}
