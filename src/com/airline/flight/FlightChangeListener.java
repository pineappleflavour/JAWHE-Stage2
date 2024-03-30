package com.airline.flight;

// Interface for listening to changes in flight check-in status
public interface FlightChangeListener {
    // Method called when the total number of checked-in passengers changes
    void onTotalCheckedInChanged(Flight flight, int newTotalCheckedIn);
}
