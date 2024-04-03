package com.airline.model;

public class Passenger {
    private final String bookingRef;
    private final String flightCode;
    private final String firstName;
    private final String lastName;
    private final String passengerClass;
    private final Double baggageWeight;
    private final Double baggageLenght;
    private final Double baggageBreath;
    private final Double baggageHeight;
    private Boolean checkedIn;
    private Boolean missedFlight = false;

    public Passenger(String bookingRef, String flightCode, String firstName, String lastName, String passengerClass, Double baggageWeight, Double baggageLenght, Double baggageBreath, Double baggageHeight, Boolean checkedIn) {

        if (bookingRef == null || flightCode == null || firstName == null || lastName == null || checkedIn == null) {
            throw new IllegalArgumentException("Parameters  bookingRef, flightCode, firstName, lastName, checkedIn cannot be null");
        }

        if (bookingRef.trim().isEmpty() || flightCode.trim().isEmpty() || firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("String parameters bookingRef, flightCode, firstName, lastName cannot be empty");
        }

        if (!lastName.trim().matches("[a-zA-Z:-]+") || !firstName.trim().matches("[a-zA-Z:-]+")) {
            throw new IllegalArgumentException("Invalid name input for lastName or firstName");
        }

        if (!bookingRef.matches("[a-zA-Z0-9]{6}")){
            throw new IllegalArgumentException("Invalid booking reference");
        }

        this.bookingRef = bookingRef;
        this.flightCode = flightCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengerClass = passengerClass;
        this.baggageWeight = baggageWeight;
        this.baggageLenght = baggageLenght;
        this.baggageBreath = baggageBreath;
        this.baggageHeight = baggageHeight;
        this.checkedIn = checkedIn;
    }
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

    public String getFullName(){
        return firstName + " " + lastName;
    }
    public String getPassengerClass() {
        return passengerClass;
    }

    public Double getBaggageWeight( ){ return baggageWeight; }

    public Double getBaggageVolume( ){ return baggageLenght * baggageBreath * baggageHeight; }

    public String getBaggageDimension( ){ return String.format("%.2f X %.2f X %.2f", baggageLenght, baggageBreath, baggageHeight); }

    public Boolean getCheckedIn( ) {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Boolean getMissedFlight() {
        return missedFlight;
    }

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
                ", baggageLength=" + baggageLenght +
                ", baggageBreath=" + baggageBreath +
                ", baggageHeight=" + baggageHeight +
                ", checkedIn=" + checkedIn +
                '}';
    }

}
