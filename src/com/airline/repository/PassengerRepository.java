package com.airline.repository;

import com.airline.model.Passenger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PassengerRepository {
    private static Map<String, Passenger> passengers = new HashMap<>();
    public PassengerRepository() {
        String csvFilePath ="data/passenger.csv";
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
                    Passenger passenger = new Passenger(
                            fileDetails.get("bookingRef").trim(),
                            fileDetails.get("flightCode").trim(),
                            fileDetails.get("firstName").trim(),
                            fileDetails.get("lastName").trim(),
                            fileDetails.get("passengerClass").trim(),
                            Double.parseDouble(fileDetails.get("baggageWeight")),
                            Double.parseDouble(fileDetails.get("baggageLength")),
                            Double.parseDouble(fileDetails.get("baggageBreath")),
                            Double.parseDouble(fileDetails.get("baggageHeight")),
                            Boolean.parseBoolean(fileDetails.get("checkedIn"))
                    );

                    // Assume bookingRef is unique and used as the key
                    passengers.put(passenger.getBookingRef(), passenger);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void checkInByBookingRef(String bookingRef) {
        passengers.computeIfPresent(bookingRef.toUpperCase(), (key, passenger) -> {
            passenger.setCheckedIn(true);
            passenger.setMissedFlight(false);
            return passenger;
        });
    }

    public Map<String, Passenger> getPassengers() {
        return passengers;
    }

    public void printAllPassengers() {
        passengers.values().forEach(System.out::println);
    }


}
