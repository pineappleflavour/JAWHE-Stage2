package com.airline.queue;
import com.airline.passenger.*;
public class BusinessQueueManager extends PassengerQueueManager{

    public BusinessQueueManager(PassengerRepository passengerRepo) {
        super(passengerRepo);
    }

    @Override
    public void addPassengerToQueue() {
            for (Passenger passenger : passengerRepo.getPassengers().values()) {
                if (passenger.getCheckedIn().equals(false) && passenger.getPassengerClass().equalsIgnoreCase("business") && passenger.getMissedFlight().equals(false)) {
                    passengerQueue.add(passenger);
                    delayProcessing();
                }
            }
    }
}
