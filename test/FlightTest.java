import com.airline.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {
    private Flight flight;

    @BeforeEach
    void setUp() {
        // Initialize a sample Flight for testing
        flight = new Flight("FL123", "Destination", LocalDateTime.now(), "Airline", 200,
                20.0, 30.0, 40.0, 60.0, 10.0, 100, 500.0, 600.0, 50.0);
    }

    @Test
    void testGetFlightCode() {
        assertEquals("FL123", flight.getFlightCode());
    }

    @Test
    void testGetDestination() {
        assertEquals("Destination", flight.getDestination());
    }

    @Test
    void testIsDeparting() {
        assertTrue(flight.isDeparting()); // Assuming departure time is within the next 5 minutes
    }

    @Test
    void testSetTotalCheckedIn() {
        flight.setTotalCheckedIn();
        assertEquals(101, flight.getTotalCheckedIn());
    }

    @Test
    void testGetBaggageVolume() {
        assertEquals(60000.0, flight.getMaxBaggageVolume(), 0.01);
    }

    // Add more test cases for other methods as needed

    // Example of testing an exception (invalid capacity)
    @Test
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () ->
                new Flight("FL456", "Another Destination", LocalDateTime.now(), "Another Airline",
                        -10, 20.0, 30.0, 40.0, 50.0, 10.0, 100, 500.0, 600.0, 50.0));
    }
    @Test
    void testGetMaxBaggageWeight() {
        assertEquals(40.0, flight.getMaxBaggageWeight(), 0.01);
    }

    @Test
    void testGetAllowedBaggageWeight() {
        assertEquals(20.0, flight.getAllowedBaggageWeight(), 0.01);
    }

    @Test
    void testGetTotalCollectedExcessBaggageFee() {
        assertEquals(10.0, flight.getTotalCollectedExcessBaggageFee(), 0.01);
    }


    @Test
    void testInvalidBaggageDimensions() {
        assertThrows(IllegalArgumentException.class, () ->
                new Flight("FL456", "Another Destination", LocalDateTime.now(), "Another Airline",
                        200, 20.0, -30.0, 40.0, 50.0, 10.0, 100, 500.0, 600.0, 50.0));
    }

    // Add more test cases for other methods as needed

    // Example of testing departure status (within 5 minutes)
    @Test
    void testIsDepartingWithin5Minutes() {
        assertTrue(flight.isDeparting());
    }
}
