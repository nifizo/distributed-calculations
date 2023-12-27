package org.example.rmi.client;

import org.example.dto.AirlineDto;
import org.example.dto.FlightDto;
import org.example.rmi.server.RMIServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        RMIServer server = (RMIServer) Naming.lookup("rmi://localhost:1099/server");

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
                        var airlines = server.findAllAirlines();
                        System.out.println(airlines);
                    }
                    case 2 -> {
                        System.out.println("Enter airline id: ");
                        var airlineId = scanner.next();
                        var airlines = server.findAllAirlines();
                        System.out.println(airlines);
                    }
                    case 3 -> {
                        System.out.println("Enter airline name: ");
                        var airlineName = scanner.next();
                        var flights = server.findAllByAirline(airlineName);
                        System.out.println(flights);
                    }
                    case 4 -> {
                        System.out.println("Enter flight id: ");
                        var flightId = scanner.next();
                        var flight = server.getFlight(UUID.fromString(flightId));
                        System.out.println(flight);
                    }
                    case 5 -> {
                        System.out.println("Enter airline id: ");
                        var airlineId = scanner.next();
                        var airline = server.getAirline(UUID.fromString(airlineId));
                        System.out.println(airline);
                    }
                    case 6 -> {
                        var airlineDto = new AirlineDto();
                        System.out.println("Enter airline name: ");
                        airlineDto.setName(scanner.next());
                        System.out.println("Enter airline code: ");
                        airlineDto.setCode(scanner.next());
                        System.out.println("Enter airline country: ");
                        airlineDto.setCountry(scanner.next());
                        var airline = server.createAirline(airlineDto);
                        System.out.println(airline);
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
                        var flight = server.createFlight(flightDto);
                        System.out.println(flight);
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
                        server.updateAirline(airlineDto);
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
                        server.updateFlight(flightDto);
                    }
                    case 10 -> {
                        System.out.println("Enter airline id: ");
                        var airlineId = scanner.next();
                        server.deleteAirline(UUID.fromString(airlineId));
                    }
                    case 11 -> {
                        System.out.println("Enter flight id: ");
                        var flightId = scanner.next();
                        server.deleteFlight(UUID.fromString(flightId));
                    }
                    case 12 -> {
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
