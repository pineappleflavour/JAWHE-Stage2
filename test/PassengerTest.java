import com.airline.model.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        // Initialize a sample Passenger for testing
        passenger = new Passenger("ABC123", "FL123", "John", "Doe", "Economy", 15.0, 30.0, 20.0, 10.0, true);
    }

    @Test
    void testGetFullName() {
        assertEquals("John Doe", passenger.getFullName());
    }

    @Test
    void testGetBaggageVolume() {
        assertEquals(6000.0, passenger.getBaggageVolume(), 0.01);
    }

    @Test
    void testGetBaggageDimension() {
        assertEquals("30.00 X 20.00 X 10.00", passenger.getBaggageDimension());
    }

    @Test
    void testSetCheckedIn() {
        passenger.setCheckedIn(false);
        assertFalse(passenger.getCheckedIn());
    }

    @Test
    void testInvalidBookingRef() {
        assertThrows(IllegalArgumentException.class, () -> new Passenger("AB12", "FL123", "John", "Doe", "Economy", 15.0, 30.0, 20.0, 10.0, true));
    }
}
