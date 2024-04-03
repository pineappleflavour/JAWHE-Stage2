package com.airline.baggage;

import com.airline.passenger.Passenger;
import com.airline.flight.Flight;

public class BaggageService {

    /**
     * Calculates excess baggage charges for a given passenger and flight
     *
     * @param passengerInfo Passenger object containing baggage details
     * @param flightInfo    Flight object containing baggage allowance and fees
     * @return double The total excess baggage charge, if any
     */
    public static Double calculateExcessBaggage(Passenger passengerInfo, Flight flightInfo) {
        double excessVolumeCharge = 0.0, excessWeightCharge = 0.0;

        // Check if baggage exceeds allowance in terms of weight or volume
        if (passengerInfo.getBaggageVolume() > flightInfo.getAllowedBaggageVolume()
                || passengerInfo.getBaggageWeight() > flightInfo.getAllowedBaggageWeight()) {

            // Calculate excess volume charge
            if (passengerInfo.getBaggageVolume() > flightInfo.getAllowedBaggageVolume()) {
                excessVolumeCharge = (passengerInfo.getBaggageVolume() - flightInfo.getAllowedBaggageVolume())
                        * flightInfo.getExcessBaggageFee();
            }

            // Calculate excess weight charge
            if (passengerInfo.getBaggageWeight() > flightInfo.getAllowedBaggageWeight()) {
                excessWeightCharge = (passengerInfo.getBaggageWeight() - flightInfo.getAllowedBaggageWeight())
                        * flightInfo.getExcessBaggageFee();
            }

            // Return the sum of both charges
            return excessVolumeCharge + excessWeightCharge;
        }

        // Return 0 if there are no excess baggage charges
        return 0.0;
    }
}