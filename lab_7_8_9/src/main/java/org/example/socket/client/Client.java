package org.example.socket.client;

import org.example.dto.AirlineDto;
import org.example.dto.FlightDto;
import org.example.dto.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {

    public Socket socket;
    public ObjectInputStream sInput;
    public ObjectOutputStream sOutput;
    public String serverIP;
    public int serverPort;
    public ClientListenThread listenThread;

    public Client() {

    }

    public void Connect(String serverIP, int port) {
        try {
            System.out.println("Connecting to the server");

            this.serverIP = serverIP;
            this.serverPort = port;
            this.socket = new Socket(this.serverIP, this.serverPort);

            System.out.println("Connecting to the server");

            sOutput = new ObjectOutputStream(this.socket.getOutputStream());
            sInput = new ObjectInputStream(this.socket.getInputStream());
            listenThread = new ClientListenThread(this);

            this.listenThread.start();
        } catch (IOException ex) {
            System.out.println("Can not connected to the server.");
        }
    }

    public void Stop() {
        if (this.socket != null) {

            try {
                this.socket.close();
                this.sOutput.flush();
                this.sOutput.close();
                this.sInput.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void SendRequest(Request request) {
        try {
            this.sOutput.writeObject(request);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.Connect("localhost", 5000);

        var scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Get all airlines");
            System.out.println("2. Get all flights by airline id");
            System.out.println("3. Get all flights by airline name");
            System.out.println("4. Get flight by id");
            System.out.println("5. Get airline by id");
            System.out.println("6. Create airline");
            System.out.println("7. Create flight");
            System.out.println("8. Update airline");
            System.out.println("9. Update flight");
            System.out.println("10. Delete airline");
            System.out.println("11. Delete flight");
            System.out.println("12. Exit");

            var option = scanner.nextInt();

            try {
                switch (option) {
                    case 1 -> {
                        var request = new Request(Request.RequestMethod.GET, "/airline", null);
                        client.SendRequest(request);
                    }
                    case 2 -> {
                        System.out.println("Enter airline id: ");
                        var airlineId = scanner.next();
                        var request = new Request(Request.RequestMethod.GET, "/airline/" + airlineId + "/flights", null);
                        client.SendRequest(request);
                    }
                    case 3 -> {
                        System.out.println("Enter airline name: ");
                        var airlineName = scanner.next();
                        var request = new Request(Request.RequestMethod.GET, "/airline/name/" + airlineName + "/flights", null);
                        client.SendRequest(request);
                    }
                    case 4 -> {
                        System.out.println("Enter flight id: ");
                        var flightId = scanner.next();
                        var request = new Request(Request.RequestMethod.GET, "/flight/" + flightId, null);
                        client.SendRequest(request);
                    }
                    case 5 -> {
                        System.out.println("Enter airline id: ");
                        var airlineId = scanner.next();
                        var request = new Request(Request.RequestMethod.GET, "/airline/" + airlineId, null);
                        client.SendRequest(request);
                    }
                    case 6 -> {
                        var airlineDto = new AirlineDto();
                        System.out.println("Enter airline name: ");
                        airlineDto.setName(scanner.next());
                        System.out.println("Enter airline code: ");
                        airlineDto.setCode(scanner.next());
                        System.out.println("Enter airline country: ");
                        airlineDto.setCountry(scanner.next());
                        var request = new Request(Request.RequestMethod.POST, "/airline", airlineDto);
                        client.SendRequest(request);
                    }
                    case 7 -> {
                        var flightDto = new FlightDto();
                        System.out.println("Enter flight origin: ");
                        flightDto.setOrigin(scanner.next());
                        System.out.println("Enter flight destination: ");
                        flightDto.setDestination(scanner.next());
                        System.out.println("Enter flight number: ");
                        flightDto.setFlightNumber(scanner.next());
                        System.out.println("Enter flight airline id: ");
                        flightDto.setAirlineId(UUID.fromString(scanner.next()));
                        System.out.println("Enter flight departure time: ");
                        flightDto.setDepartureTime(Long.parseLong(scanner.next()));
                        System.out.println("Enter flight arrival time: ");
                        flightDto.setArrivalTime(Long.parseLong(scanner.next()));
                        var request = new Request(Request.RequestMethod.POST, "/flight", flightDto);
                        client.SendRequest(request);
                    }
                    case 8 -> {
                        AirlineDto airlineDto = new AirlineDto();
                        System.out.println("Enter airline id: ");
                        airlineDto.setAirlineId(UUID.fromString(scanner.next()));
                        System.out.println("Enter airline name: ");
                        airlineDto.setName(scanner.next());
                        System.out.println("Enter airline code: ");
                        airlineDto.setCode(scanner.next());
                        System.out.println("Enter airline country: ");
                        airlineDto.setCountry(scanner.next());
                        var request = new Request(Request.RequestMethod.PUT, "/airline", airlineDto);
                        client.SendRequest(request);
                    }
                    case 9 -> {
                        FlightDto flightDto = new FlightDto();
                        System.out.println("Enter flight id: ");
                        flightDto.setFlightId(UUID.fromString(scanner.next()));
                        System.out.println("Enter flight origin: ");
                        flightDto.setOrigin(scanner.next());
                        System.out.println("Enter flight destination: ");
                        flightDto.setDestination(scanner.next());
                        System.out.println("Enter flight number: ");
                        flightDto.setFlightNumber(scanner.next());
                        System.out.println("Enter flight airline id: ");
                        flightDto.setAirlineId(UUID.fromString(scanner.next()));
                        System.out.println("Enter flight departure time: ");
                        flightDto.setDepartureTime(Long.parseLong(scanner.next()));
                        System.out.println("Enter flight arrival time: ");
                        flightDto.setArrivalTime(Long.parseLong(scanner.next()));
                        var request = new Request(Request.RequestMethod.PUT, "/flight", flightDto);
                        client.SendRequest(request);
                    }
                    case 10 -> {
                        System.out.println("Enter airline id: ");
                        var airlineId = scanner.next();
                        var request = new Request(Request.RequestMethod.DELETE, "/airline/" + airlineId, null);
                        client.SendRequest(request);
                    }
                    case 11 -> {
                        System.out.println("Enter flight id: ");
                        var flightId = scanner.next();
                        var request = new Request(Request.RequestMethod.DELETE, "/flight/" + flightId, null);
                        client.SendRequest(request);
                    }
                    case 12 -> {
                        client.Stop();
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
