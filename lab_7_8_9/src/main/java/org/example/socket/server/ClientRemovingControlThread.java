package org.example.socket.server;


// The purpose of this class is control the clients anytime.If any client
//lost the connection to the server but still in arraylist which keeps clients in server,
//remove them from list.
public class ClientRemovingControlThread extends Thread{

    private final SocketServer server;

    public ClientRemovingControlThread(SocketServer server) {
        this.server = server;
    }

    @Override
    public void run() {

        while(!this.server.socket.isClosed())
        {
            SocketServer.clients.removeIf(client -> client.socket.isClosed());
        }
    }
}