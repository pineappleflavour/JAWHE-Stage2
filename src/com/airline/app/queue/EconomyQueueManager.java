package com.airline.app.queue;
import com.airline.model.Passenger;
import com.airline.repository.PassengerRepository;

public class EconomyQueueManager extends PassengerQueueManager{
    public EconomyQueueManager(PassengerRepository passengerRepo) {
        super(passengerRepo);
    }
    @Override
    public void addPassengerToQueue() {
            for (Passenger passenger : passengerRepo.getPassengers().values()) {
                if (passenger.getCheckedIn().equals(false) && passenger.getPassengerClass().equalsIgnoreCase("economy") && passenger.getMissedFlight().equals(false)) {
                    passengerQueue.add(passenger);
                    delayProcessing();
                }
            }
    }
}
