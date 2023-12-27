package org.example.socket.server;


import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;


// This thread called by server directly when the server opens and never closes. The purpose of this
// thread is always waiting for new client connection requests to accept or reject them.
public class ListenConnectionRequestThread extends Thread {

    private final SocketServer server;

    public ListenConnectionRequestThread(SocketServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (!this.server.socket.isClosed()) {
            try {
                Socket nSocket = this.server.socket.accept();
                SClient nClient = new SClient(nSocket, this.server.flightController, this.server.airlineController);
                nClient.Listen();
                SocketServer.clients.add(nClient);
                Logger.getLogger(ListenConnectionRequestThread.class.getName()).info("New client connected to the server.");

            } catch (IOException ex) {
                Logger.getLogger(ListenConnectionRequestThread.class.getName()).info("Client could not be connected to the server.");
            }
        }
    }
}
