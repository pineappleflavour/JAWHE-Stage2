package com.airline.checkin;

import com.airline.baggage.BaggageService;
import com.airline.flight.Flight;
import com.airline.flight.FlightRepository;
import com.airline.logging.Logger;
import com.airline.passenger.Passenger;
import com.airline.passenger.PassengerRepository;
import com.airline.queue.PassengerQueueManager;

import javax.swing.*;
public class CheckInDesk implements Runnable {
    private final PassengerRepository passengerRepo;
    private final PassengerQueueManager passengerQueue;
    private final FlightRepository flightRepo;
    private final Integer deskId ;
    private volatile Integer processingDelay = 5000;
    private static final Logger logger = Logger.getInstance();
    private final JTextArea textArea;
    private volatile boolean paused = false;


    public CheckInDesk(PassengerRepository passengerRepo, PassengerQueueManager passengerQueue, FlightRepository flightRepo, Integer deskId, JTextArea textArea) { // Modify this line
        this.passengerRepo = passengerRepo;
        this.passengerQueue = passengerQueue;
        this.flightRepo = flightRepo;
        this.deskId = deskId;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Passenger passengerInfo = passengerQueue.attendToNextPassenger();
            if (passengerInfo != null) {
                processPassenger(passengerInfo);
            }
            while (paused) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void processPassenger(Passenger passengerInfo) {
        String message;
        Flight flightInfo = flightRepo.findByFlightCode(passengerInfo.getFlightCode());
        message = String.format("Check-In Desk %d (%s class): Attending to passenger %s",deskId, passengerInfo.getPassengerClass(), passengerInfo.getFullName());
        updateTextArea(message);
        logger.info(message);
        if (!flightInfo.isDeparting()) {
            delayProcessing();
            message = String.format("Check-In Desk %d (%s class): Calculating charge for %s",deskId, passengerInfo.getPassengerClass(), passengerInfo.getFullName());
            updateTextArea(message);
            logger.info(message);
            Double charge = BaggageService.calculateExcessBaggage(passengerInfo, flightInfo);
            delayProcessing();
            message = String.format("Check-In Desk %d (%s class): The passenger %s  would be charged an excess charge: $ %.2f",deskId, passengerInfo.getPassengerClass(), passengerInfo.getFullName(), charge);
            updateTextArea(message);
            logger.info(message);
            passengerRepo.checkInByBookingRef(passengerInfo.getBookingRef());
            flightRepo.updateFlightInfo(passengerInfo.getFlightCode(), passengerInfo.getBaggageVolume(), passengerInfo.getBaggageWeight(), charge);
            delayProcessing();
        } else {
            message = String.format("Check-In Desk %d (%s class): The passenger %s cannot be checked-in as the flight %s has departed",deskId, passengerInfo.getPassengerClass(), passengerInfo.getFullName(), passengerInfo.getFlightCode());
            updateTextArea(message);
            logger.warn(message);
            delayProcessing();
        }
        message = String.format("Check-In Desk %d (%s class): Is currently free or on hold",deskId, passengerInfo.getPassengerClass() );
        updateTextArea(message);
        logger.info(message);
    }

    private void updateTextArea(String message) {
        SwingUtilities.invokeLater(() -> textArea.setText(message + "\n"));
    }

    private void delayProcessing() {
        try {
            Thread.sleep(processingDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setProcessingDelay(int delay) {
        this.processingDelay = delay;
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

}
