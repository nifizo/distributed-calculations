package org.example.rmi.server;

import org.example.controller.AirlineController;
import org.example.controller.ControllerFactory;
import org.example.controller.ControllerFactoryImpl;
import org.example.controller.FlightController;
import org.example.dto.AirlineDto;
import org.example.dto.FlightDto;

import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {
    private final AirlineController airlineController;
    private final FlightController flightController;

    /**
     * Creates and exports a new UnicastRemoteObject object using an
     * anonymous port.
     *
     * <p>The object is exported with a server socket
     * created using the {@link RMISocketFactory} class.
     *
     * @throws RemoteException if failed to export object
     * @since 1.1
     */
    protected RMIServerImpl() throws RemoteException, SQLException {
        ControllerFactory fac = new ControllerFactoryImpl();
        this.airlineController = fac.getAirlineController();
        this.flightController = fac.getFlightController();
    }

    @Override
    public AirlineDto createAirline(AirlineDto airlineDto) throws RemoteException {
        try {
            airlineDto = airlineController.create(airlineDto);
        } catch (Exception e) {
            Logger.getLogger(RMIServerImpl.class.getName()).info(e.getMessage());
        }
        return airlineDto;
    }

    @Override
    public AirlineDto getAirline(UUID id) {
        return airlineController.get(id);
    }


    @Override
    public void updateAirline(AirlineDto airlineDto) {
        airlineController.update(airlineDto);
    }

    @Override
    public boolean deleteAirline(UUID id) {
        return airlineController.delete(id);
    }

    @Override
    public List<AirlineDto> findAllAirlines() throws RemoteException {
        List<AirlineDto> airl = null;
        try {
            airl = airlineController.findAll();
        }
        catch (Exception e) {
            Logger.getLogger(RMIServerImpl.class.getName()).info(e.getMessage());
        }
        return airl;
    }

    @Override
    public FlightDto createFlight(FlightDto flightDto) throws Exception {
        return flightController.create(flightDto);
    }

    @Override
    public FlightDto getFlight(UUID id) {
        return flightController.get(id);
    }

    @Override
    public void updateFlight(FlightDto flightDto) {
        flightController.update(flightDto);
    }

    @Override
    public boolean deleteFlight(UUID id) {
        return flightController.delete(id);
    }

    @Override
    public List<FlightDto> findAllByAirlineId(UUID airlineId) throws Exception {
        return flightController.findAllByAirlineId(airlineId);
    }

    @Override
    public List<FlightDto> findAllByAirline(String airlineName) throws Exception {
        return flightController.findAllByAirline(airlineName);
    }
}
