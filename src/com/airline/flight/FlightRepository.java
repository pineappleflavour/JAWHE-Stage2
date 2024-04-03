package com.airline.flight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class FlightRepository extends Observable {
    private static Map<String, Flight> flights = new HashMap<>();
    public FlightRepository() {
        String csvFilePath ="data/flight.csv";
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
                    Flight  flight = new Flight(
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

    public Flight findByFlightCode(String flightCode){
        for (Map.Entry<String, Flight> entry : flights.entrySet()) {
            if (entry.getKey().equals(flightCode)){
                return entry.getValue();
            }
        }
        return null;
    }

    public Map<String, Flight> getFlights() {
        return flights;
    }
    public void updateFlightInfo(String flightCode, Double baggageVolume, Double baggageWeight, Double chargeFee){
        Flight flightInfo = findByFlightCode(flightCode);
        if (flightInfo != null) {
            flightInfo.setTotalCollectedExcessBaggageFee(chargeFee);
            flightInfo.setTotalBaggageWeight(baggageWeight);
            flightInfo.setTotalBaggageVolume(baggageVolume);
            flightInfo.setTotalCheckedIn();
            setChanged();
            notifyObservers(flights);
        }
    }
}
