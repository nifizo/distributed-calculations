package org.example.socket.server;

import org.example.controller.AirlineController;
import org.example.controller.ControllerFactory;
import org.example.controller.ControllerFactoryImpl;
import org.example.controller.FlightController;
import org.example.dao.AirlineDao;
import org.example.dao.FlightDao;
import org.example.dao.db.DAOManager;
import org.example.mapper.AirlineMapper;
import org.example.mapper.FlightMapper;
import org.example.service.AirlineService;
import org.example.service.AirlineServiceImpl;
import org.example.service.FlightService;
import org.example.service.FlightServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Logger;

//This is a TCP protocol connection based server.
public class SocketServer {
    public FlightController flightController;
    public AirlineController airlineController;

    public ServerSocket socket;
    public int port;
    public ListenConnectionRequestThread listenConnectionRequestThread;
    public ClientRemovingControlThread removingControlThread;
    public static ArrayList<SClient> clients;

    public SocketServer(int port, FlightController flightController, AirlineController airlineController) {
        try {
            this.port = port;
            this.flightController = flightController;
            this.airlineController = airlineController;
            this.socket = new ServerSocket(this.port);
            this.listenConnectionRequestThread = new ListenConnectionRequestThread(this);
            removingControlThread = new ClientRemovingControlThread(this);
            clients = new ArrayList<>();
        } catch (IOException e) {
            Logger.getLogger(SocketServer.class.getName()).info("Server could not be started on port: " + this.port);

        }
    }

    public void start() {
        this.listenConnectionRequestThread.start();
        this.removingControlThread.start();
        Logger.getLogger(SocketServer.class.getName()).info("Server started on port: " + this.port);
    }

    public void Stop() {
        try {
            this.socket.close();
            this.listenConnectionRequestThread.interrupt();
            this.removingControlThread.interrupt();
        } catch (IOException e) {
            Logger.getLogger(SocketServer.class.getName()).info("Server could not be stopped on port: " + this.port);
        }
    }

    public static void main(String[] args) throws Exception {
        ControllerFactory controllerFactory = new ControllerFactoryImpl();
        FlightController flightController = controllerFactory.getFlightController();
        AirlineController airlineController = controllerFactory.getAirlineController();

        SocketServer server = new SocketServer(5000, flightController, airlineController);
        server.start();
    }
}
