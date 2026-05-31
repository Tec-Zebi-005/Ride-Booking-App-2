import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RidePaymentPage extends JFrame {
    // Database Config
    private final String DB_URL = "jdbc:mysql://localhost:3306/rideflow_DB";
    private final String DB_USER = "root";
    private final String DB_PASS = "2D905Bdad";

    // Dynamic Data Fields
    private String username, driverName, vehicle, pickup, dropoff;
    private int rideId, totalFare;
    private double distance;
    private int durationMinutes;

    // UI Components
    private JLabel totalFareLabel, statusLabel;
    private JTextField cardNumberField, cardHolderField;
    private JPasswordField cvvField;
    private JRadioButton creditCardBtn, debitCardBtn, easyPaisaBtn, jazzCashBtn;
    private JComboBox<String> expiryMonthCombo, expiryYearCombo;
    private JButton payButton;

    public RidePaymentPage(int rideId, String username, String driverName) {
        this.rideId = rideId;
        this.username = username;
        this.driverName = driverName;

        // 1. Fetch data from SQL first
        fetchRideData();

        // 2. Setup Frame
        setTitle("RideFlow Payment - " + username);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 850);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel with Scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(45, 12, 56));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // UI Building blocks
        mainPanel.add(createHeaderPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createRideDetailsPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createFareBreakdownPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createPaymentMethodPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createCardDetailsPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createActionPanel());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        setVisible(true);
    }

    private void fetchRideData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ride_requests WHERE id = ?")) {

            pstmt.setInt(1, rideId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.pickup = rs.getString("pickup_location");
                this.dropoff = rs.getString("dropoff_location");
                this.vehicle = rs.getString("vehicle_type");

                // If distance/time isn't in your SQL, we simulate realistic values
                this.distance = 5.0 + (Math.random() * 10.0);
                this.durationMinutes = (int) (distance * 2.2);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    private int calculateFare() {
        int baseFare = 0;
        switch (vehicle) {
            case "Bike": baseFare = 150; break;
            case "Car": baseFare = 250; break;
            case "AC Car": baseFare = 350; break;
            case "Premium": baseFare = 600; break;
            default: baseFare = 200;
        }
        int distFare = ((int) Math.ceil(distance / 5)) * 100;
        int timeFare = durationMinutes * 15;
        return baseFare + distFare + timeFare + 100 + 95; // + Service fee & Tax
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(25, 5, 35));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel title = new JLabel("Secure Payment Gateway");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panel.add(title);
        return panel;
    }

    private JPanel createRideDetailsPanel() {
        JPanel panel = createStyledSection("Trip Information", new Color(3, 52, 81));
        JPanel grid = new JPanel(new GridLayout(3, 2, 10, 10));
        grid.setOpaque(false);

        addInfo(grid, "From:", pickup);
        addInfo(grid, "To:", dropoff);
        addInfo(grid, "Service:", vehicle);
        addInfo(grid, "Driver:", driverName);
        addInfo(grid, "Distance:", String.format("%.2f km", distance));
        addInfo(grid, "Trip Time:", durationMinutes + " mins");

        panel.add(grid);
        return panel;
    }

    private JPanel createFareBreakdownPanel() {
        JPanel panel = createStyledSection("Fare Summary", new Color(39, 174, 96));
        totalFare = calculateFare();

        addFareRow(panel, "Standard Base Fare", "Rs. " + (totalFare - 295));
        addFareRow(panel, "Service Fee", "Rs. 100");
        addFareRow(panel, "Government Tax", "Rs. 95");

        JSeparator sep = new JSeparator();
        panel.add(Box.createVerticalStrut(10));
        panel.add(sep);

        totalFareLabel = new JLabel("Total to Pay: Rs. " + totalFare);
        totalFareLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalFareLabel.setForeground(new Color(255, 235, 59));
        totalFareLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(totalFareLabel);

        return panel;
    }

    private JPanel createPaymentMethodPanel() {
        JPanel panel = createStyledSection("Select Payment Method", new Color(180, 20, 20));
        ButtonGroup group = new ButtonGroup();

        creditCardBtn = new JRadioButton("Credit/Debit Card", true);
        easyPaisaBtn = new JRadioButton("EasyPaisa");
        jazzCashBtn = new JRadioButton("JazzCash");

        for (JRadioButton rb : new JRadioButton[]{creditCardBtn, easyPaisaBtn, jazzCashBtn}) {
            rb.setBackground(new Color(32, 46, 55));
            rb.setForeground(Color.WHITE);
            group.add(rb);
            panel.add(rb);
        }
        return panel;
    }

    private JPanel createCardDetailsPanel() {
        JPanel panel = createStyledSection("Card Details", new Color(189, 103, 28));

        cardNumberField = new JTextField(20);
        cardHolderField = new JTextField(20);
        cvvField = new JPasswordField(4);

        panel.add(new JLabel("Card Number:"));
        panel.add(cardNumberField);
        panel.add(new JLabel("Cardholder Name:"));
        panel.add(cardHolderField);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        payButton = new JButton("Confirm & Pay");
        payButton.setBackground(new Color(46, 204, 113));
        payButton.setForeground(Color.WHITE);
        payButton.setPreferredSize(new Dimension(200, 50));
        payButton.addActionListener(e -> handlePaymentProcess());

        statusLabel = new JLabel("Encrypted Connection Active");
        statusLabel.setForeground(Color.LIGHT_GRAY);

        panel.add(payButton);
        return panel;
    }

    private void handlePaymentProcess() {
        payButton.setEnabled(false);
        statusLabel.setText("Verifying Transaction...");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE ride_requests SET status = 'COMPLETED' WHERE id = ?")) {

            pstmt.setInt(1, rideId);
            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Payment Successful! Rs. " + totalFare + "\n\nThank you for using RideFlow!",
                        "Payment Confirmed",
                        JOptionPane.INFORMATION_MESSAGE);

                // Close payment page
                this.dispose();

                // Redirect to feedback page
                SwingUtilities.invokeLater(() -> {
                    new FeedbackPage(username, driverName);
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Payment Failed: " + ex.getMessage());
            payButton.setEnabled(true);
        }
    }

    // Helper UI Methods
    private JPanel createStyledSection(String title, Color borderColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(32, 46, 55));
        panel.setBorder(new CompoundBorder(new LineBorder(borderColor, 2), new EmptyBorder(15, 15, 15, 15)));
        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE); t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(t); panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private void addInfo(JPanel p, String lbl, String val) {
        p.add(new JLabel("<html><font color='#AAAAAA'>" + lbl + "</font></html>"));
        p.add(new JLabel("<html><font color='white'>" + val + "</font></html>"));
    }

    private void addFareRow(JPanel p, String desc, String amt) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel d = new JLabel(desc); d.setForeground(Color.WHITE);
        JLabel a = new JLabel(amt); a.setForeground(new Color(255, 235, 59));
        row.add(d, BorderLayout.WEST); row.add(a, BorderLayout.EAST);
        p.add(row);
    }
}