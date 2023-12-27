package org.example.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(1099);
            RMIServer server = new RMIServerImpl();
            registry.rebind("server", server);
            Logger.getLogger(Server.class.getName()).info("Server started on port: 1099");
        } catch (RemoteException | SQLException e) {
            Logger.getLogger(Server.class.getName()).info("Server could not be started");
        }
    }
}
