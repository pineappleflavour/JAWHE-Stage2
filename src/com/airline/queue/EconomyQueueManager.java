package com.airline.queue;
import com.airline.passenger.*;
public class EconomyQueueManager extends PassengerQueueManager{
    public EconomyQueueManager(PassengerRepository passengerRepo) {
        super(passengerRepo);
    }
    @Override
    public void addPassengerToQueue() {
            for (Passenger passenger : passengerRepo.getPassengers().values()) {
                if (passenger.getCheckedIn().equals(false) && passenger.getPassengerClass().equalsIgnoreCase("economy")) {
                    passengerQueue.add(passenger);
                    delayProcessing();
                }
            }
    }
}
