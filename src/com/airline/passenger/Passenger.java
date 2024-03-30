package com.airline.passenger;

// Class representing a passenger
public class Passenger {
    private final String bookingRef;    // Booking reference
    private final String flightCode;    // Flight code
    private final String firstName;     // First name of the passenger
    private final String lastName;      // Last name of the passenger
    private final String passengerClass; // Passenger class
    private final Double baggageWeight; // Weight of the baggage
    private final Double baggageLength; // Length of the baggage
    private final Double baggageBreath; // Breadth of the baggage
    private final Double baggageHeight; // Height of the baggage
    private Boolean checkedIn;           // Indicates if the passenger is checked in
    private Boolean missedFlight = false; // Indicates if the passenger missed the flight

    // Constructor with parameters
    public Passenger(String bookingRef, String flightCode, String firstName, String lastName, String passengerClass, Double baggageWeight, Double baggageLength, Double baggageBreath, Double baggageHeight, Boolean checkedIn) {
        // Validate input parameters
        if (bookingRef == null || flightCode == null || firstName == null || lastName == null || checkedIn == null) {
            throw new IllegalArgumentException("Parameters bookingRef, flightCode, firstName, lastName, checkedIn cannot be null");
        }

        if (bookingRef.trim().isEmpty() || flightCode.trim().isEmpty() || firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("String parameters bookingRef, flightCode, firstName, lastName cannot be empty");
        }

        if (!lastName.trim().matches("[a-zA-Z:-]+") || !firstName.trim().matches("[a-zA-Z:-]+")) {
            throw new IllegalArgumentException("Invalid name input for lastName or firstName");
        }

        if (!bookingRef.matches("[a-zA-Z0-9]{6}")) {
            throw new IllegalArgumentException("Invalid booking reference");
        }

        // Initialize instance variables
        this.bookingRef = bookingRef;
        this.flightCode = flightCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengerClass = passengerClass;
        this.baggageWeight = baggageWeight;
        this.baggageLength = baggageLength;
        this.baggageBreath = baggageBreath;
        this.baggageHeight = baggageHeight;
        this.checkedIn = checkedIn;
    }

    // Getter methods
    public String getBookingRef() {
        return bookingRef;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // Method to get full name of the passenger
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPassengerClass() {
        return passengerClass;
    }

    public Double getBaggageWeight() {
        return baggageWeight;
    }

    // Method to calculate baggage volume
    public Double getBaggageVolume() {
        return baggageLength * baggageBreath * baggageHeight;
    }

    // Method to get baggage dimensions
    public String getBaggageDimension() {
        return String.format("%.2f X %.2f X %.2f", baggageLength, baggageBreath, baggageHeight);
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    // Setter method for checked-in status
    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Boolean getMissedFlight() {
        return missedFlight;
    }

    // Setter method for missed flight status
    public void setMissedFlight(Boolean missedFlight) {
        this.missedFlight = missedFlight;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "bookingRef='" + bookingRef + '\'' +
                ", flightCode='" + flightCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", passengerClass='" + passengerClass + '\'' +
                ", baggageWeight=" + baggageWeight +
                ", baggageLength=" + baggageLength +
                ", baggageBreath=" + baggageBreath +
                ", baggageHeight=" + baggageHeight +
                ", checkedIn=" + checkedIn +
                '}';
    }
}
