package com.airline.checkin;

import com.airline.flight.Flight;
import com.airline.flight.FlightChangeListener;
import com.airline.flight.FlightRepository;
import com.airline.logging.Logger;
import com.airline.passenger.PassengerRepository;
import com.airline.queue.BusinessQueueManager;
import com.airline.queue.EconomyQueueManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CheckInSystemGUI extends JFrame {
    private JPanel checkInSystemGUI;
    private JTabbedPane tabbedPane1;
    private JPanel infoDisplay;
    private JPanel observerPanel;
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
    private JPanel flight;
    private JPanel flightInfoPanel; // New panel for flight info
    private final PassengerRepository passengerRepo;
    private final BusinessQueueManager businessQueue;
    private final EconomyQueueManager economyQueue;
    private final FlightRepository flightRepo;
    private  final ExecutorService executor;
    private List<CheckInDesk> economyCheckInDesks = new ArrayList<>();
    private List<CheckInDesk> businessCheckInDesks = new ArrayList<>();
    private List<Future<?>> economyFutures = new ArrayList<>();
    private List<Future<?>> businessFutures = new ArrayList<>();
    private Integer queueTimer = 1000;
    private Integer checkInDeskTime = 5;
    private static final Logger logger = Logger.getInstance();



    public CheckInSystemGUI(PassengerRepository passengerRepo, BusinessQueueManager businessQueue, EconomyQueueManager economyQueue, FlightRepository flightRepo) {
        // Initialize repositories and queues
        this.passengerRepo = passengerRepo;
        this.businessQueue = businessQueue;
        this.economyQueue = economyQueue;
        this.flightRepo = flightRepo;

        // Set up the initial frame properties
        setTitle("Airport Management UI");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(tabbedPane1);
        setVisible(true);

        // Set layouts for check-in desk panels
        economyCheckInDeskPanel.setLayout(new BoxLayout(economyCheckInDeskPanel, BoxLayout.X_AXIS));
        businessCheckInDeskPanel.setLayout(new BoxLayout(businessCheckInDeskPanel, BoxLayout.X_AXIS));

        // Create a thread pool executor
        executor = Executors.newCachedThreadPool();

        // Start queues and check-in desks
        startAllQueues();
        startAllCheckInDesk();

        // Listen for changes in flight check-in status
        for (Flight flight : flightRepo.getFlights().values()) {
            flight.addFlightChangeListener(new FlightChangeListener() {
                @Override
                public void onTotalCheckedInChanged(Flight flight, int newTotalCheckedIn) {
                    // Extract flight information
                    String carrier = flight.getCarrier();
                    int capacity = flight.getCapacity();
                    double holdOccupancy = (double) newTotalCheckedIn / capacity * 100;

                    // Log flight status
                    System.out.println(carrier + " " + flight.getFlightCode() );
                    System.out.println(newTotalCheckedIn + " checked in of " + capacity);
                    System.out.println("Hold is " + String.format("%.2f", holdOccupancy) + "% full");
                }
            });
        }

        // Add action listeners for check-in desk buttons
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

        // Listen for window closing event to log all logs
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.printAllLogs("./logs");
            }
        });

        // Add action listeners for pause, resume, and set timer buttons
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

        // Create borders and panels for flight information
        Border border1 = BorderFactory.createLineBorder(Color.BLACK); // Example border style
        Border border2 = BorderFactory.createLineBorder(Color.BLACK); // Example border style
        Border border3 = BorderFactory.createLineBorder(Color.BLACK);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        // Set layouts and borders for flight information panels
        panel1.setLayout(new BorderLayout());
        panel2.setLayout(new BorderLayout());
        panel3.setLayout(new BorderLayout());
        panel4.setLayout(new BorderLayout());

        panel1.setBorder(border1);
        panel2.setBorder(border2);
        panel3.setBorder(border3);
        panel4.setBorder(border3);

        // Create labels for flight information panels
        JLabel label1 = new JLabel("Flight Status");
        JLabel label2 = new JLabel("Flight Status");
        JLabel label3 = new JLabel("Flight Status");
        JLabel label4 = new JLabel("Flight Status");

        // Add labels to flight information panels
        panel1.add(label1, BorderLayout.CENTER);
        panel2.add(label2, BorderLayout.CENTER);
        panel3.add(label3, BorderLayout.CENTER);
        panel4.add(label4, BorderLayout.CENTER);

        // Create and configure flight info panel
        flightInfoPanel = new JPanel();
        flightInfoPanel.setLayout(new BoxLayout(flightInfoPanel, BoxLayout.X_AXIS));
        flightInfoPanel.add(panel1);
        flightInfoPanel.add(panel2);
        flightInfoPanel.add(panel3);
        flightInfoPanel.add(panel4);
        flightInfoPanel.setBorder(BorderFactory.createTitledBorder("Flight Information")); // Example border with a title

        // Add flight info panel to observer panel
        observerPanel.setLayout(new BorderLayout());
        observerPanel.add(flightInfoPanel, BoxLayout.X_AXIS);

        // Listen for changes in flight check-in status and update corresponding panels
        for (Flight flight : flightRepo.getFlights().values()) {
            flight.addFlightChangeListener(new FlightChangeListener() {
                @Override
                public void onTotalCheckedInChanged(Flight flight, int newTotalCheckedIn) {
                    // Extract flight information
                    String carrier = flight.getCarrier();
                    String flightCode = flight.getFlightCode();
                    int capacity = flight.getCapacity();
                    double holdOccupancy = (double) newTotalCheckedIn / capacity * 100;

                    // Create a label to display the flight information
                    JLabel flightInfoLabel = new JLabel("<html>" +
                            carrier + " " + flightCode + ": " + newTotalCheckedIn + " checked in of " + capacity + "<br>" +
                            "Hold is " + String.format("%.2f", holdOccupancy) + "% full" +
                            "</html>");
                    flightInfoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text

                    // Add the label to the corresponding panel based on flight code
                    if (flightCode.equals("ABC123")) {
                        panel1.removeAll(); // Clear the panel before adding the new label
                        panel1.add(flightInfoLabel);
                        panel1.revalidate(); // Refresh panel to reflect changes
                    } else if (flightCode.equals("DEF789")) {
                        panel2.removeAll();
                        panel2.add(flightInfoLabel);
                        panel2.revalidate();
                    } else if (flightCode.equals("GHI012")) {
                        panel3.removeAll();
                        panel3.add(flightInfoLabel);
                        panel3.revalidate();
                    } else if (flightCode.equals("XYZ456")) {
                        panel4.removeAll();
                        panel4.add(flightInfoLabel);
                        panel4.revalidate();
                    }
                }
            });
        }
    }


    // Method to update flight info panel
    private void updateFlightInfoPanel(String flightCode, int totalCheckedIn) {
        // Create a label to display flight check-in information
        JLabel flightInfoLabel = new JLabel(flightCode + " " + totalCheckedIn + " Checked In.");
        // Add the label to the flightInfoPanel
        flightInfoPanel.add(flightInfoLabel);
        // Revalidate the flightInfoPanel to reflect changes
        flightInfoPanel.revalidate();
        // Repaint the flightInfoPanel to ensure proper rendering
        flightInfoPanel.repaint();
    }

    private void updateBusinessQueueDisplay(BusinessQueueManager businessQueue) {
        // Update the business queue display on the Swing event dispatch thread
        SwingUtilities.invokeLater(() -> {
            // Create a new table model for the business class queue
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Booking Ref", "Passenger Name", "Baggage Weight", "Baggage Dimension" }, 0);
            // Populate the table model with passenger queue information
            new LinkedList<>(businessQueue.getPassengerQueue()).forEach(passenger -> tableModel.addRow(new Object[]{ passenger.getBookingRef(),
                    passenger.getFullName(),
                    String.format("%.2f Kg", passenger.getBaggageWeight()),
                    passenger.getBaggageDimension()
            }));
            // Set the table model for the business class queue table
            businessClass.setModel(tableModel);
            // Disable row selection for the business class queue table
            businessClass.setEnabled(false);
            // Disable focus for the business class queue table
            businessClass.setFocusable(false);
            // Disable row selection for the business class queue table
            businessClass.setRowSelectionAllowed(false);
            // Disable column selection for the business class queue table
            businessClass.setColumnSelectionAllowed(false);
            // Update the label displaying the total number of passengers in the business class queue
            businessClassLabel.setText(String.format("Business Class: Total in Queue %d", businessQueue.passengerQueueSize()));
        });
    }

    private void updateEconomyQueueDisplay(EconomyQueueManager economyQueue) {
        // Update the economy queue display on the Swing event dispatch thread
        SwingUtilities.invokeLater(() ->{
            // Create a new table model for the economy class queue
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Booking Ref", "Passenger Name", "Baggage Weight", "Baggage Dimension" }, 0);
            // Populate the table model with passenger queue information
            new LinkedList<>(economyQueue.getPassengerQueue()).forEach(passenger -> tableModel.addRow(new Object[]{ passenger.getBookingRef(),
                    passenger.getFullName(),
                    String.format("%.2f Kg", passenger.getBaggageWeight()),
                    passenger.getBaggageDimension()
            }));
            // Set the table model for the economy class queue table
            economyClass.setModel(tableModel);
            // Disable row selection for the economy class queue table
            economyClass.setEnabled(false);
            // Disable focus for the economy class queue table
            economyClass.setFocusable(false);
            // Disable row selection for the economy class queue table
            economyClass.setRowSelectionAllowed(false);
            // Disable column selection for the economy class queue table
            economyClass.setColumnSelectionAllowed(false);
            // Update the label displaying the total number of passengers in the economy class queue
            economyClassLabel.setText(String.format("Economy Class: Total in Queue %d", economyQueue.passengerQueueSize()));
        });
    }

    private void addEconomyCheckInDesk() {
        // Add an economy check-in desk to the UI
        ThreadPanel threadPanel = new ThreadPanel();
        economyCheckInDeskPanel.add(threadPanel);
        economyCheckInDeskPanel.revalidate();

        // Create a new check-in desk for the economy class
        CheckInDesk checkInDesk = new CheckInDesk(passengerRepo, economyQueue, flightRepo, economyCheckInDeskPanel.getComponentCount(), threadPanel.getTextArea());
        // Submit the check-in desk task to the executor service
        Future<?> future = executor.submit(checkInDesk);
        economyFutures.add(future);
        economyCheckInDesks.add(checkInDesk);
    }

    private void addBusinessCheckInDesk() {
        // Add a business check-in desk to the UI
        ThreadPanel threadPanel = new ThreadPanel();
        businessCheckInDeskPanel.add(threadPanel);
        businessCheckInDeskPanel.revalidate();

        // Create a new check-in desk for the business class
        CheckInDesk checkInDesk = new CheckInDesk(passengerRepo, businessQueue, flightRepo, businessCheckInDeskPanel.getComponentCount(), threadPanel.getTextArea());
        // Submit the check-in desk task to the executor service
        Future<?> future = executor.submit(checkInDesk);
        businessFutures.add(future);
        businessCheckInDesks.add(checkInDesk);
    }

    private void removeEconomyCheckInDesk() {
        // Remove an economy check-in desk from the UI
        if (!economyFutures.isEmpty()) {
            // Cancel the task associated with the last added economy check-in desk
            Future<?> future = economyFutures.remove(economyFutures.size() - 1);
            future.cancel(true);

            // Remove the last added economy check-in desk panel
            economyCheckInDeskPanel.remove(economyCheckInDeskPanel.getComponentCount() - 1);
            economyCheckInDeskPanel.revalidate();
            economyCheckInDeskPanel.repaint();
        }
    }

    private void removeBusinessCheckInDesk() {
        // Remove a business check-in desk from the UI
        if (!businessFutures.isEmpty()) {
            // Cancel the task associated with the last added business check-in desk
            Future<?> future = businessFutures.remove(businessFutures.size() - 1);
            future.cancel(true);

            // Remove the last added business check-in desk panel
            businessCheckInDeskPanel.remove(businessCheckInDeskPanel.getComponentCount() - 1);
            businessCheckInDeskPanel.revalidate();
            businessCheckInDeskPanel.repaint();
        }
    }

    private void startAllQueues() {
        // Start threads for business and economy class queues
        new Thread(businessQueue).start();
        new Thread(economyQueue).start();

        // Start a timer to update queue displays periodically
        new Timer( queueTimer, event ->{
            updateBusinessQueueDisplay(businessQueue);
            updateEconomyQueueDisplay(economyQueue);
        }).start();
    }

    private void startAllCheckInDesk() {
        // Add initial check-in desks for both economy and business classes
        for (int i = 0; i < 3; i++) {
            addEconomyCheckInDesk();
            addBusinessCheckInDesk();
        }
    }

    private void closeAllCheckInDesk() {
        // Close all check-in desks for both economy and business classes
        while(!economyFutures.isEmpty()){
            removeEconomyCheckInDesk();
        }
        while(!businessFutures.isEmpty()){
            removeBusinessCheckInDesk();
        }
    }

    private void pauseAllCheckInDesk() {
        // Pause all check-in desks for both economy and business classes
        for(CheckInDesk desk : economyCheckInDesks){
            desk.pause();
        }
        for(CheckInDesk desk : businessCheckInDesks){
            desk.pause();
        }
    }

    private void resumeAllCheckInDesk() {
        // Resume all check-in desks for both economy and business classes
        for(CheckInDesk desk : economyCheckInDesks){
            desk.resume();
        }
        for(CheckInDesk desk : businessCheckInDesks){
            desk.resume();
        }
    }

    private void adjustCheckInDeskTImer() {
        // Adjust processing delay for all check-in desks based on user input
        for(CheckInDesk desk : economyCheckInDesks){
            desk.setProcessingDelay((checkInDeskTime * 1000));
        }
        for(CheckInDesk desk : businessCheckInDesks){
            desk.setProcessingDelay(checkInDeskTime * 1000 );
        }
    }


    // Method to update flight information panel
//    private void updateFlightInfoPanel(String flightCode, int totalCheckedIn) {
//        JLabel flightInfoLabel = new JLabel(flightCode + " " + totalCheckedIn + " Checked In.");
//        flightInfoPanel.add(flightInfoLabel);
//        flightInfoPanel.revalidate();
//        flightInfoPanel.repaint();
//    }

    public static void main(String[] args) {
        // Initialize flight repository
        FlightRepository flightRepo = new FlightRepository();
        // Initialize passenger repository
        PassengerRepository passengerRepo = new PassengerRepository();
        // Initialize economy queue manager
        EconomyQueueManager economyQueue = new EconomyQueueManager(passengerRepo);
        // Initialize business queue manager
        BusinessQueueManager businessQueue = new BusinessQueueManager(passengerRepo);
        // Launch the GUI on the event dispatch thread
        SwingUtilities.invokeLater(() -> new CheckInSystemGUI(passengerRepo, businessQueue, economyQueue, flightRepo));
    }

    // Inner class representing a panel for displaying thread information
    private static class ThreadPanel extends JPanel {
        private final JTextArea textArea;

        public ThreadPanel() {
            // Initialize text area for displaying thread information
            textArea = new JTextArea();
            // Set text area properties
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(200, 100));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            // Add text area to a scroll pane and add scroll pane to the panel
            add(new JScrollPane(textArea));
        }

        // Method to retrieve the text area
        public JTextArea getTextArea() {
            return textArea;
        }
    }

}
