import com.airline.model.Passenger;
import com.airline.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PassengerRepositoryTest {
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setUp() {
        passengerRepository = new PassengerRepository();
    }

    @Test
    void testCheckInByBookingRef() {
        String bookingRef = "EOR1ZO";
        passengerRepository.checkInByBookingRef(bookingRef);
        Map<String, Passenger> passengers = passengerRepository.getPassengers();
        assertTrue(passengers.containsKey(bookingRef));
        assertTrue(passengers.get(bookingRef).getCheckedIn());
    }

    @Test
    void testGetPassengers() {
        Map<String, Passenger> passengers = passengerRepository.getPassengers();
        assertNotNull(passengers);
        assertFalse(passengers.isEmpty());
    }

    @Test
    void testPrintAllPassengers() {
        passengerRepository.printAllPassengers();
    }
}
