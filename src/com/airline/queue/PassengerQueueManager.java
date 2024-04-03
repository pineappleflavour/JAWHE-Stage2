package com.airline.queue;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import com.airline.passenger.*;

public abstract class PassengerQueueManager  implements Runnable {
    protected PassengerRepository passengerRepo;
    protected Queue<Passenger> passengerQueue = new LinkedList<>();
    private volatile Integer processingDelay = 1000;

    public  PassengerQueueManager (PassengerRepository passengerRepo){
        this.passengerRepo = passengerRepo;
    }

    @Override
    public void run() {
        addPassengerToQueue();
    }
    public Passenger getNextPassenger() {
        return passengerQueue.peek();
    }

    public synchronized Passenger attendToNextPassenger() {
        return passengerQueue.poll();
    }
    public Queue<Passenger> getPassengerQueue() {
        return passengerQueue;
    }
    public void addPassengerToQueue(){
        for (Passenger passenger : passengerRepo.getPassengers().values()) {
            if (!passenger.getCheckedIn()) {
                    passengerQueue.add(passenger);
                    delayProcessing();
            }
        }
    }
    public void delayProcessing() {
        try {
            Thread.sleep(processingDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setProcessingDelay(int delay) {
        this.processingDelay = delay;
    }
    public synchronized void printAllPassengersInQueue() {
        passengerQueue.forEach(System.out::println);
    }

    public synchronized  Boolean isPassengerQueueEmpty(){
        return passengerQueue.isEmpty();
    }
    public synchronized  Integer passengerQueueSize(){
        return passengerQueue.size();
    }

}
