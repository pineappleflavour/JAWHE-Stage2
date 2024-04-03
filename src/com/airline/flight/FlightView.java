package com.airline.flight;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class FlightView implements Observer {
    private Map<String, Flight> flightView;

    public FlightView(FlightRepository flightRepo){
        flightRepo.addObserver(this);
        flightRepo.getFlights().values().removeIf(Flight::isDeparting);
        this.flightView = flightRepo.getFlights();
    }
    @Override
    public void update(Observable o, Object arg) {
        this.updateFlightView(flightView);
    }

    private void updateFlightView(Map<String, Flight> flightsView) {
        flightsView.values().removeIf(Flight::isDeparting);
        this.flightView = flightsView;
    }
    public List<String> getFlightViewString(){
        List queue = new LinkedList<String>();
        flightView.values().stream()
                .sorted(Comparator.comparing(Flight::getFlightCode))
                .forEach(flight -> {
                    queue.add(flight.toString());
                });

        return queue;
    }
    public List<JPanel> getFlightView(){
        List queue = new LinkedList<JPanel>();
        flightView.values().stream()
                .sorted(Comparator.comparing(Flight::getFlightCode))
                .forEach(flight -> {
                    queue.add(new FramePanel(flight.toString()));
                });

        return queue;
    }

    private static class FramePanel extends JPanel {
        private final JTextArea textArea;

        public FramePanel(String text) {
            textArea = new JTextArea(text);
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(200, 100));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            add(new JScrollPane(textArea));
        }

    }
}
