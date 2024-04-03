package com.airline.checkin;

import com.airline.flight.FlightRepository;
import com.airline.flight.FlightView;
import com.airline.logging.Logger;
import com.airline.passenger.PassengerRepository;
import com.airline.queue.BusinessQueueManager;
import com.airline.queue.EconomyQueueManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CheckInSystemGUI extends JFrame {
    private JPanel checkInSystemGUI;
    private JTabbedPane tabbedPane1;
    private JPanel infoDisplay;
    private JPanel settings;
    private JTable businessClass;
    private JTable economyClass;
    private JScrollPane economyCheckInDeskScrollPane;
    private JScrollPane businessCheckInDeskScrollPane;
    private JLabel businessClassLabel;
    private JLabel economyClassLabel;
    private JScrollPane businessClassScrollPane;
    private JScrollPane economyClassScrollPane;
    private JPanel economyCheckInDeskPanel;
    private JButton addEconomyCheckInButton;
    private JPanel businessCheckInDeskPanel;
    private JButton addBusinessCheckInButton;
    private JButton removeEconomyCheckInButton;
    private JButton removeBusinessCheckInButton;
    private JButton closeAllCheckInButton;
    private JButton pauseAllCheckInButton;
    private JButton resumeAllCheckInButton;
    private JButton setCheckInDeskButton;
    private JTextField checkInDeskTimer;
    private JScrollPane flightViewerScrollPane;
    private JPanel flightViewerPanel;
    private final PassengerRepository passengerRepo;
    private final BusinessQueueManager businessQueue;
    private final EconomyQueueManager economyQueue;
    private final FlightRepository flightRepo;
    private  final ExecutorService executor;
    private List<CheckInDesk> economyCheckInDesks = new ArrayList<CheckInDesk>();
    private List<CheckInDesk> businessCheckInDesks = new ArrayList<CheckInDesk>();
    private List<Future<?>> economyFutures = new ArrayList<Future<?>>();
    private List<Future<?>> businessFutures = new ArrayList<Future<?>>();
    private Integer queueTimer = 1000;
    private Integer checkInDeskTime = 5;
    private static final Logger logger = Logger.getInstance();

    public CheckInSystemGUI(PassengerRepository passengerRepo, BusinessQueueManager businessQueue, EconomyQueueManager economyQueue, FlightRepository flightRepo) {
        this.passengerRepo = passengerRepo;
        this.businessQueue = businessQueue;
        this.economyQueue = economyQueue;
        this.flightRepo = flightRepo;

        setTitle("Airport Management UI");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(tabbedPane1);
        setVisible(true);

        economyCheckInDeskPanel.setLayout(new BoxLayout(economyCheckInDeskPanel, BoxLayout.X_AXIS));
        businessCheckInDeskPanel.setLayout(new BoxLayout(businessCheckInDeskPanel, BoxLayout.X_AXIS));
        flightViewerPanel.setLayout(new BoxLayout(flightViewerPanel, BoxLayout.X_AXIS));
        executor = Executors.newCachedThreadPool();

        startAllQueues();
        startAllCheckInDesk();
        flightViewObserver(new FlightView(flightRepo));


        addEconomyCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEconomyCheckInDesk();
            }
        });
        addBusinessCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBusinessCheckInDesk();
            }
        });
        removeEconomyCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEconomyCheckInDesk();
            }
        });
        removeBusinessCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBusinessCheckInDesk();
            }
        });
        closeAllCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAllCheckInDesk();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.printAllLogs("./logs");
            }
        });
        pauseAllCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseAllCheckInDesk();
                setCheckInDeskButton.setEnabled(false);
                addEconomyCheckInButton.setEnabled(false);
                addBusinessCheckInButton.setEnabled(false);
                removeEconomyCheckInButton.setEnabled(false);
                removeBusinessCheckInButton.setEnabled(false);
            }
        });
        resumeAllCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeAllCheckInDesk();
                setCheckInDeskButton.setEnabled(true);
                addEconomyCheckInButton.setEnabled(true);
                addBusinessCheckInButton.setEnabled(true);
                removeEconomyCheckInButton.setEnabled(true);
                removeBusinessCheckInButton.setEnabled(true);
            }
        });
        setCheckInDeskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               checkInDeskTime = Integer.parseInt(checkInDeskTimer.getText().trim());
               adjustCheckInDeskTImer();
            }
        });
    }
    private void updateBusinessQueueDisplay( BusinessQueueManager businessQueue ){
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Booking Ref", "Passenger Name", "Baggage Weight", "Baggage Dimension" }, 0);
            new LinkedList<>(businessQueue.getPassengerQueue()).forEach(passenger -> tableModel.addRow(new Object[]{ passenger.getBookingRef(),
                    passenger.getFullName(),
                    String.format("%.2f Kg", passenger.getBaggageWeight()),
                    passenger.getBaggageDimension()
            }));
            businessClass.setModel(tableModel);
            businessClass.setEnabled(false);
            businessClass.setFocusable(false);
            businessClass.setRowSelectionAllowed(false);
            businessClass.setColumnSelectionAllowed(false);
            businessClassLabel.setText(String.format("Business Class: Total in Queue %d", businessQueue.passengerQueueSize()));
        });
    }
    private void updateEconomyQueueDisplay( EconomyQueueManager economyQueue ){
        SwingUtilities.invokeLater(() ->{
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Booking Ref", "Passenger Name", "Baggage Weight", "Baggage Dimension" }, 0);
            new LinkedList<>(economyQueue.getPassengerQueue()).forEach(passenger -> tableModel.addRow(new Object[]{ passenger.getBookingRef(),
                    passenger.getFullName(),
                    String.format("%.2f Kg", passenger.getBaggageWeight()),
                    passenger.getBaggageDimension()
            }));
            economyClass.setModel(tableModel);
            economyClass.setEnabled(false);
            economyClass.setFocusable(false);
            economyClass.setRowSelectionAllowed(false);
            economyClass.setColumnSelectionAllowed(false);
            economyClassLabel.setText(String.format("Economy Class: Total in Queue %d", economyQueue.passengerQueueSize()));
        });
    }

    private void addEconomyCheckInDesk(){
        ThreadPanel threadPanel = new ThreadPanel();
        economyCheckInDeskPanel.add(threadPanel);
        economyCheckInDeskPanel.revalidate();

        CheckInDesk checkInDesk = new CheckInDesk(passengerRepo, economyQueue, flightRepo, economyCheckInDeskPanel.getComponentCount(), threadPanel.getTextArea());
        Future<?> future = executor.submit(checkInDesk);
        economyFutures.add(future);
        economyCheckInDesks.add(checkInDesk);
    }

    private void addBusinessCheckInDesk(){
        ThreadPanel threadPanel = new ThreadPanel();
        businessCheckInDeskPanel.add(threadPanel);
        businessCheckInDeskPanel.revalidate();

        CheckInDesk checkInDesk = new CheckInDesk(passengerRepo, businessQueue, flightRepo, businessCheckInDeskPanel.getComponentCount(), threadPanel.getTextArea());
        Future<?> future = executor.submit(checkInDesk);
        businessFutures.add(future);
        businessCheckInDesks.add(checkInDesk);
    }

    private void removeEconomyCheckInDesk() {
        if (!economyFutures.isEmpty()) {
            Future<?> future = economyFutures.remove(economyFutures.size() - 1);
            future.cancel(true);

            economyCheckInDeskPanel.remove(economyCheckInDeskPanel.getComponentCount() - 1);
            economyCheckInDeskPanel.revalidate();
            economyCheckInDeskPanel.repaint();
        }
    }

    private void removeBusinessCheckInDesk() {
        if (!businessFutures.isEmpty()) {
            Future<?> future = businessFutures.remove(businessFutures.size() - 1);
            future.cancel(true);

            businessCheckInDeskPanel.remove(businessCheckInDeskPanel.getComponentCount() - 1);
            businessCheckInDeskPanel.revalidate();
            businessCheckInDeskPanel.repaint();
        }
    }

    private void startAllQueues(){
        executor.submit(businessQueue);
        executor.submit(economyQueue);

        new Timer( queueTimer, event ->{
            updateBusinessQueueDisplay(businessQueue);
            updateEconomyQueueDisplay(economyQueue);
        }).start();
    }
    private void startAllCheckInDesk(){
        for (int i = 0; i < 3; i++) {
            addEconomyCheckInDesk();
            addBusinessCheckInDesk();
        }
    }

    private void closeAllCheckInDesk() {
        while(!economyFutures.isEmpty()){
            removeEconomyCheckInDesk();
        }
        while(!businessFutures.isEmpty()){
            removeBusinessCheckInDesk();
        }
    }

    private void pauseAllCheckInDesk(){
        for(CheckInDesk desk : economyCheckInDesks){
            desk.pause();
        }
        for(CheckInDesk desk : businessCheckInDesks){
            desk.pause();
        }
    }
    private void resumeAllCheckInDesk(){
        for(CheckInDesk desk : economyCheckInDesks){
            desk.resume();
        }
        for(CheckInDesk desk : businessCheckInDesks){
            desk.resume();
        }
    }

    private void adjustCheckInDeskTImer(){
        for(CheckInDesk desk : economyCheckInDesks){
            desk.setProcessingDelay((checkInDeskTime * 1000));
        }
        for(CheckInDesk desk : businessCheckInDesks){
            desk.setProcessingDelay(checkInDeskTime * 1000 );
        }
    }

    private void flightViewObserver( FlightView flightView){
        new Timer( queueTimer, event ->{
            flightViewerPanel.removeAll();
            flightView.getFlightView().forEach(frame ->{
                flightViewerPanel.add(frame);
            });
            flightViewerPanel.revalidate();
            flightViewerPanel.repaint();
        }).start();
    }

    public static void main(String[] args) {
        FlightRepository flightRepo = new FlightRepository();
        PassengerRepository passengerRepo = new PassengerRepository();
        EconomyQueueManager economyQueue = new EconomyQueueManager(passengerRepo);
        BusinessQueueManager businessQueue = new BusinessQueueManager(passengerRepo);
        SwingUtilities.invokeLater((() -> new CheckInSystemGUI(passengerRepo, businessQueue, economyQueue, flightRepo)));

    }

    private static class ThreadPanel extends JPanel {
        private final JTextArea textArea;

        public ThreadPanel() {
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(200, 100));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            add(new JScrollPane(textArea));
        }

        public JTextArea getTextArea() {
            return textArea;
        }

    }
}
