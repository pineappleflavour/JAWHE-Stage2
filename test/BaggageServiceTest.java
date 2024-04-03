//package com.airline.service;

import com.airline.model.Flight;
import com.airline.model.Passenger;
import com.airline.service.BaggageService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class BaggageServiceTest {
    @Test
    public void testCalculateExcessBaggage() {
        // Create a test passenger
        Passenger passenger = new Passenger("BK1234", "FL123", "John", "Doe", "Business", 30.0, 1.0, 1.0, 1.0, false);

        // Create a test flight
        Flight flight = new Flight("FL123", "Paris", LocalDateTime.now().plusHours(2), "Airline Carrier", 200, 20.0, 40.0, 30.0, 60.0, 50.0, 0, 0.0, 0.0, 0.0);

        // Calculate the expected excess baggage fee

        double expectedExcessBaggageFee = 0.0;
        if (passenger.getBaggageVolume() > flight.getAllowedBaggageVolume()) {
            expectedExcessBaggageFee += (passenger.getBaggageVolume() - flight.getAllowedBaggageVolume()) * flight.getExcessBaggageFee();
        }
        if (passenger.getBaggageWeight() > flight.getAllowedBaggageWeight()) {
            expectedExcessBaggageFee += (passenger.getBaggageWeight() - flight.getAllowedBaggageWeight()) * flight.getExcessBaggageFee();
        }

        // Calculate the actual excess baggage fee
        double actualExcessBaggageFee = BaggageService.calculateExcessBaggage(passenger, flight);

        // Check if the expected and actual values are equal
        assertEquals(expectedExcessBaggageFee, actualExcessBaggageFee, 0.0001);
    }
}
