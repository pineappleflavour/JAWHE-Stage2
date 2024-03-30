package com.airline.flight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    private String flightCode;
    private String destination;
    private LocalDateTime departureTime;
    private String carrier;
    private Integer capacity;
    private Double allowedBaggageWeight;
    private Double allowedBaggageVolume;
    private Double maxBaggageWeight;
    private Double maxBaggageVolume;
    private Double excessBaggageFee;
    private Integer totalCheckedIn;
    private Double totalBaggageWeight;
    private Double totalBaggageVolume;
    private Double totalCollectedExcessBaggageFee;

    public Flight(String flightCode, String destination, LocalDateTime departureTime, String carrier, Integer capacity, Double allowedBaggageWeight, Double allowedBaggageVolume, Double maxBaggageWeight, Double maxBaggageVolume, Double excessBaggageFee, Integer totalCheckedIn, Double totalBaggageWeight, Double totalBaggageVolume, Double totalCollectedExcessBaggageFee ) {
        // Check for null parameters
        if (flightCode == null || destination == null || departureTime == null || carrier == null) {
            throw new IllegalArgumentException("Parameters flightCode, destination, departureTime, carrier cannot be null");
        }

        // Check for empty string parameters
        if (flightCode.trim().isEmpty() || destination.trim().isEmpty() || carrier.trim().isEmpty()) {
            throw new IllegalArgumentException("String parameters flightCode, destination, carrier cannot be empty");
        }

        // Check for non-positive numeric parameters
        if (capacity <= 0 || allowedBaggageWeight <= 0 || allowedBaggageVolume <= 0 || maxBaggageWeight <= 0 || maxBaggageVolume <= 0 || excessBaggageFee < 0 || totalCheckedIn < 0 || totalBaggageWeight < 0 || totalBaggageVolume < 0 || totalCollectedExcessBaggageFee < 0) {
            throw new IllegalArgumentException("Numeric parameters capacity, allowedBaggageWeight, allowedBaggageVolume, maxBaggageWeight, maxBaggageVolume, excessBaggageFee, totalCheckedIn must be positive");
        }

        // Initialize flight properties
        this.flightCode = flightCode;
        this.destination = destination;
        this.departureTime = departureTime;
        this.carrier = carrier;
        this.capacity = capacity;
        this.allowedBaggageWeight = allowedBaggageWeight;
        this.allowedBaggageVolume = allowedBaggageVolume;
        this.maxBaggageWeight = maxBaggageWeight;
        this.maxBaggageVolume = maxBaggageVolume;
        this.excessBaggageFee = excessBaggageFee;
        this.totalCheckedIn = totalCheckedIn;
        this.totalBaggageWeight = totalBaggageWeight;
        this.totalBaggageVolume = totalBaggageVolume;
        this.totalCollectedExcessBaggageFee = totalCollectedExcessBaggageFee;
    }

    // Getter methods for flight properties
    public String getFlightCode() {
        return flightCode;
    }
    public String getDestination() {
        return destination;
    }
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    public String getCarrier() {
        return carrier;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public Double getMaxBaggageWeight() {
        return maxBaggageWeight;
    }
    public Double getMaxBaggageVolume() {
        return maxBaggageVolume;
    }

    // Listeners for flight changes
    private List<FlightChangeListener> listeners = new ArrayList<>();

    public void addFlightChangeListener(FlightChangeListener listener) {
        listeners.add(listener);
    }

    public void removeFlightChangeListener(FlightChangeListener listener) {
        listeners.remove(listener);
    }

    // Notify listeners about changes in total checked-in passengers
    private void notifyTotalCheckedInChanged() {
        for (FlightChangeListener listener : listeners) {
            listener.onTotalCheckedInChanged(this, totalCheckedIn);
        }
    }

    // Setter method for total checked-in passengers
    public void setTotalCheckedIn(int totalCheckedIn) {
        this.totalCheckedIn = totalCheckedIn;
        notifyTotalCheckedInChanged();
    }
    public int getTotalCheckedInPassengers() {
        return totalCheckedIn;
    }
    public Double getExcessBaggageFee() {
        return excessBaggageFee;
    }

    public Double getAllowedBaggageWeight() {
        return allowedBaggageWeight;
    }

    public Double getAllowedBaggageVolume() {
        return allowedBaggageVolume;
    }
    public Integer getTotalCheckedIn() {
        return totalCheckedIn;
    }
    public Double getTotalBaggageWeight() {
        return totalBaggageWeight;
    }
    public Double getTotalBaggageVolume() {
        return totalBaggageVolume;
    }
    public Double getTotalCollectedExcessBaggageFee(){
        return totalCollectedExcessBaggageFee;
    }

    // Increment total checked-in passengers by one
    public void setTotalCheckedIn() {
        this.totalCheckedIn++;
    }
    // Add to total baggage weight
    public void setTotalBaggageWeight(Double BaggageWeight) {
        this.totalBaggageWeight += BaggageWeight;
    }
    // Add to total baggage volume
    public void setTotalBaggageVolume(Double BaggageVolume) {
        this.totalBaggageVolume += BaggageVolume;
    }
    // Add to total collected excess baggage fee
    public void setTotalCollectedExcessBaggageFee(Double chargeFee){
        this.totalCollectedExcessBaggageFee += chargeFee;
    }
    // Check if the flight is departing within the next 5 minutes
    public Boolean isDeparting(){
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesUntilDeparture = ChronoUnit.MINUTES.between(currentTime, departureTime);
        return  minutesUntilDeparture <= 5;
    }

}
