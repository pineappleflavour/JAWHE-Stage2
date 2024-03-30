package com.airline.flight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FlightRepository {
    private static Map<String, Flight> flights = new HashMap<>();

    public FlightRepository() {
        String csvFilePath = "data/flight.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;
            String[] headers = null;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    // Assume the first line contains headers
                    headers = line.split(",");
                    isFirstLine = false;
                } else {
                    String[] values = line.split(",");
                    Map<String, String> fileDetails = new HashMap<>();

                    for (int i = 0; i < headers.length; i++) {
                        fileDetails.put(headers[i], values[i]);
                    }

                    // Create an instance of Passenger and add it to the map
                    Flight flight = new Flight(
                            fileDetails.get("flightCode"),
                            fileDetails.get("destination"),
                            LocalDateTime.parse(fileDetails.get("departureTime")),
                            fileDetails.get("carrier"),
                            Integer.parseInt(fileDetails.get("capacity")),
                            Double.parseDouble(fileDetails.get("allowedBaggageWeight")),
                            Double.parseDouble(fileDetails.get("allowedBaggageVolume")),
                            Double.parseDouble(fileDetails.get("maxBaggageWeight")),
                            Double.parseDouble(fileDetails.get("maxBaggageVolume")),
                            Double.parseDouble(fileDetails.get("excessBaggageFee")),
                            Integer.parseInt(fileDetails.get("totalCheckedIn")),
                            Double.parseDouble(fileDetails.get("totalBaggageWeight")),
                            Double.parseDouble(fileDetails.get("totalBaggageVolume")),
                            Double.parseDouble(fileDetails.get("totalCollectedExcessBaggageFee"))
                    );
                    // Assume flightCode is unique and used as the key
                    flights.put(fileDetails.get("flightCode"), flight);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Flight findByFlightCode(String flightCode) {
        // Search for a flight by its flight code
        for (Map.Entry<String, Flight> entry : flights.entrySet()) {
            if (entry.getKey().equals(flightCode)) {
                return entry.getValue(); // Return the flight if found
            }
        }
        return null; // Return null if the flight with the given flight code is not found
    }

    public Map<String, Flight> getFlights() {
        return flights; // Return the map of flights
    }

    public void updateFlightInfo(String flightCode, Double baggageVolume, Double baggageWeight, Double chargeFee) {
        // Update flight information with the provided data
        Flight flightInfo = findByFlightCode(flightCode); // Find the flight by its flight code
        if (flightInfo != null) {
            // Update the flight's total collected excess baggage fee, total baggage weight, total baggage volume, and total checked-in passengers
            flightInfo.setTotalCollectedExcessBaggageFee(chargeFee);
            flightInfo.setTotalBaggageWeight(baggageWeight);
            flightInfo.setTotalBaggageVolume(baggageVolume);
            flightInfo.setTotalCheckedIn();
        }
    }

    public int getTotalCheckedInPassengers(String flightCode) {
        // Get the total number of checked-in passengers for a specific flight
        Flight flight = findByFlightCode(flightCode); // Find the flight by its flight code
        if (flight != null) {
            return flight.getTotalCheckedIn(); // Return the total number of checked-in passengers if the flight is found
        } else {
            return 0; // Return 0 if the flight with the given code was not found
        }
    }
}