package org.example.socket.client;


import org.example.dto.Response;

import java.io.IOException;
import java.util.logging.Logger;

//The purpose of this thread is listening the server continuously if there is a message incoming to our input stream.
// İf there is a message, then decide what will be happened.
public class ClientListenThread extends Thread {

    Client client;

    public ClientListenThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!this.client.socket.isClosed()) {
            try {
                Response response = (Response) this.client.sInput.readObject();
                switch (response.getStatus()) {
                    case SUCCESS:
                        if (response.getBody() != null) {
                            System.out.println("Server response: " +  response.getStatus() + ": " + response.getBody());
                        } else {
                            System.out.println("Server response: " + response.getStatus());
                        }
                        break;
                    case ERROR:
                        Logger.getLogger(ClientListenThread.class.getName()).log(java.util.logging.Level.SEVERE, null, response.getBody());
                        break;
                    default:
                        break;
                }

            } catch (IOException | ClassNotFoundException e) {
                Logger.getLogger(ClientListenThread.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
            }
        }
    }
}
