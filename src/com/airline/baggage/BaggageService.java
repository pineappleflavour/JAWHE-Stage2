package com.airline.baggage;
import com.airline.passenger.*;
import com.airline.flight.*;

public class BaggageService {
    public static Double calculateExcessBaggage(Passenger passengerInfo, Flight flightInfo) {
        double excessVolumeCharge = 0.0, excessWeightCharge = 0.0;
        if (passengerInfo.getBaggageVolume() > flightInfo.getAllowedBaggageVolume() || passengerInfo.getBaggageWeight() > flightInfo.getAllowedBaggageWeight()){
            if (passengerInfo.getBaggageVolume() > flightInfo.getAllowedBaggageVolume()){
                excessVolumeCharge = (passengerInfo.getBaggageVolume() - flightInfo.getAllowedBaggageVolume()) * flightInfo.getExcessBaggageFee();
            }
            if (passengerInfo.getBaggageWeight()> flightInfo.getAllowedBaggageWeight()){
                excessWeightCharge = (passengerInfo.getBaggageWeight()- flightInfo.getAllowedBaggageWeight()) * flightInfo.getExcessBaggageFee();
            }
            return excessVolumeCharge + excessWeightCharge;
        }
        return 0.0;
    }
}