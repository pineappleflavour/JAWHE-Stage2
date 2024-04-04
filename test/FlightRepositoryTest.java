import com.airline.model.Flight;
import com.airline.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlightRepositoryTest {
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        // Initialize a sample FlightRepository for testing
        flightRepository = new FlightRepository();
    }

    @Test
    void testFindByFlightCode() {
        // Assume a flight with code "ABC123" exists in the repository
        String flightCode = "ABC123";
        Flight foundFlight = flightRepository.findByFlightCode(flightCode);
        assertNotNull(foundFlight);
        assertEquals(flightCode, foundFlight.getFlightCode());
    }

    @Test
    void testUpdateFlightInfo() {

        String flightCode = "ABC123";
        double baggageVolume = 6000.0;
        double baggageWeight = 50.0;
        double chargeFee = 20.0;

        Flight flight = flightRepository.getFlights().get(flightCode);
        double expectedBaggageVolume = baggageVolume + flight.getTotalBaggageVolume();
        double expectedBaggageWeight = baggageWeight + flight.getTotalBaggageWeight();
        double expectedChargeFee = chargeFee + flight.getTotalCollectedExcessBaggageFee() ;

        flightRepository.updateFlightInfo(flightCode, baggageVolume, baggageWeight, chargeFee);

        Map<String, Flight> flights = flightRepository.getFlights();
        assertTrue(flights.containsKey(flightCode));
        assertEquals(expectedBaggageVolume, flights.get(flightCode).getTotalBaggageVolume(), 0.01);
        assertEquals(expectedBaggageWeight, flights.get(flightCode).getTotalBaggageWeight(), 0.01);
        assertEquals(expectedChargeFee, flights.get(flightCode).getTotalCollectedExcessBaggageFee(), 0.01);
    }

    @Test
    void testFindByNonExistingFlightCode() {
        assertNull(flightRepository.findByFlightCode("FL789"));
    }
}
