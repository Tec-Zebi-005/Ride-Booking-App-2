import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.Vector;

public class RideAdminApp extends JFrame {

    // ── Colors ──────────────────────────────────
    private static final Color BG_DARK        = new Color(10, 14, 26);
    private static final Color BG_CARD        = new Color(18, 24, 42);
    private static final Color BG_CARD2       = new Color(24, 32, 56);
    private static final Color ACCENT         = new Color(0, 210, 180);
    private static final Color ACCENT_RED     = new Color(255, 80, 80);
    private static final Color ACCENT_YELLOW  = new Color(255, 200, 50);
    private static final Color ACCENT_BLUE    = new Color(60, 140, 255);
    private static final Color ACCENT_PURPLE  = new Color(160, 80, 255);
    private static final Color ACCENT_GREEN   = new Color(50, 220, 120);
    private static final Color TEXT_PRIMARY   = new Color(230, 235, 255);
    private static final Color TEXT_SECONDARY = new Color(130, 145, 180);
    private static final Color BORDER_COLOR   = new Color(40, 55, 90);
    private static final Color TABLE_HEADER   = new Color(20, 28, 50);
    private static final Color TABLE_ROW1     = new Color(16, 22, 40);
    private static final Color TABLE_ROW2     = new Color(20, 28, 50);
    private static final Color TABLE_SELECT   = new Color(0, 100, 90);

    private JPanel contentArea;
    private JLabel pageTitle;

    public RideAdminApp() {
        setTitle("RideFlow — Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 740);
        setLocationRelativeTo(null);
        setResizable(true);
        setBackground(BG_DARK);
        showLoginPage();
        setVisible(true);
    }

    // ── DB Connection ────────────────────────────
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    // ════════════════════════════════════════════
    //  LOGIN PAGE  (FIXED: left-aligned form)
    // ════════════════════════════════════════════
    private void showLoginPage() {
        setSize(480, 580);
        setLocationRelativeTo(null);

        // Root uses BorderLayout so the card can be pinned left
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        // ── Card ──────────────────────────────────
        RoundedPanel card = new RoundedPanel(20, BG_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 44, 40, 44));
        card.setPreferredSize(new Dimension(400, 500));
        card.setMaximumSize(new Dimension(400, 500));

        // Logo — LEFT aligned
        JLabel logo = new JLabel("RIDEFLOW");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Admin Console");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Username ──────────────────────────────
        JLabel userLbl = styledLabel("Username");
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField userField = styledField("admin");
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Password ──────────────────────────────
        JLabel passLbl = styledLabel("Password");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passField = new JPasswordField("admin123");
        styleTextField(passField);
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Error label ───────────────────────────
        JLabel errLbl = new JLabel(" ");
        errLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errLbl.setForeground(ACCENT_RED);
        errLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Login button ──────────────────────────
        JButton loginBtn = accentButton("Login →", ACCENT, BG_DARK);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            if (u.equals("admin") && p.equals("admin123")) {
                showDashboard();
            } else {
                errLbl.setText("Invalid credentials. Try again.");
            }
        });
        passField.addActionListener(e -> loginBtn.doClick());

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(28));
        card.add(userLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(userField);
        card.add(Box.createVerticalStrut(20));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(passField);
        card.add(Box.createVerticalStrut(8));
        card.add(errLbl);
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);

        // Wrap card in a left-aligned outer panel with padding
        JPanel leftWrap = new JPanel();
        leftWrap.setOpaque(false);
        leftWrap.setLayout(new BoxLayout(leftWrap, BoxLayout.Y_AXIS));
        leftWrap.setBorder(new EmptyBorder(40, 40, 40, 40));
        leftWrap.add(card);
        leftWrap.add(Box.createVerticalGlue());

        root.add(leftWrap, BorderLayout.WEST);
        // Right side — decorative dark panel
        JPanel rightPane = new JPanel();
        rightPane.setBackground(BG_DARK);
        root.add(rightPane, BorderLayout.CENTER);

        setContentPane(root);
        revalidate(); repaint();
    }

    // ════════════════════════════════════════════
    //  MAIN DASHBOARD SHELL
    // ════════════════════════════════════════════
    private void showDashboard() {
        setSize(1200, 740);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        root.add(createSidebar(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG_DARK);
        right.add(createTopBar(), BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BG_DARK);
        contentArea.setBorder(new EmptyBorder(0, 24, 24, 24));

        showHomeCards();

        right.add(contentArea, BorderLayout.CENTER);
        root.add(right, BorderLayout.CENTER);

        setContentPane(root);
        revalidate(); repaint();
    }

    // ── Sidebar ──────────────────────────────────
    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(BG_CARD);
        side.setPreferredSize(new Dimension(220, 740));
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        side.add(Box.createVerticalStrut(28));

        JLabel logo = new JLabel("RIDEFLOW");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(ACCENT);
        logo.setBorder(new EmptyBorder(0, 22, 0, 0));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logo);

        JLabel adminTag = new JLabel("Admin Panel");
        adminTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        adminTag.setForeground(TEXT_SECONDARY);
        adminTag.setBorder(new EmptyBorder(2, 22, 0, 0));
        adminTag.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(adminTag);

        side.add(Box.createVerticalStrut(30));
        side.add(sidebarDivider("MANAGE"));
        side.add(Box.createVerticalStrut(6));
        side.add(sidebarBtn("Passengers",    ACCENT_BLUE,   () -> showUserTable("Passenger")));
        side.add(Box.createVerticalStrut(4));
        side.add(sidebarBtn("Drivers",       ACCENT_GREEN,  () -> showUserTable("Driver")));
        side.add(Box.createVerticalStrut(4));
        side.add(sidebarBtn("Ride Requests",  ACCENT_YELLOW, () -> showRideRequests()));
        side.add(Box.createVerticalStrut(20));
        side.add(sidebarDivider("TOOLS"));
        side.add(Box.createVerticalStrut(6));
        side.add(sidebarBtn("Search User",   ACCENT_PURPLE, () -> showSearchPanel()));
        side.add(Box.createVerticalStrut(4));
        side.add(sidebarBtn("Home",          ACCENT,        () -> showHomeCards()));

        side.add(Box.createVerticalGlue());

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logout.setForeground(ACCENT_RED);
        logout.setBackground(new Color(40, 20, 20));
        logout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 80, 80, 120), 1, true),
                new EmptyBorder(8, 18, 8, 18)));
        logout.setFocusPainted(false);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.setOpaque(true);
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logout.setBackground(new Color(80, 20, 20));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                logout.setBackground(new Color(40, 20, 20));
            }
        });
        logout.addActionListener(e -> showLoginPage());

        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 22, 0));
        logoutWrapper.setOpaque(false);
        logoutWrapper.add(logout);
        logoutWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logoutWrapper);
        side.add(Box.createVerticalStrut(24));

        return side;
    }

    private JPanel sidebarDivider(String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 22, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_SECONDARY);
        p.add(lbl);
        return p;
    }

    // ── FIXED sidebarBtn: solid hover bg + left-side accent bar ─────────────
    private JPanel sidebarBtn(String text, Color accent, Runnable action) {
        // Precompute solid hover background (blends accent into BG_CARD)
        Color hoverBg = new Color(
                Math.min(255, BG_CARD.getRed()   + (accent.getRed()   - BG_CARD.getRed())   / 4),
                Math.min(255, BG_CARD.getGreen() + (accent.getGreen() - BG_CARD.getGreen()) / 4),
                Math.min(255, BG_CARD.getBlue()  + (accent.getBlue()  - BG_CARD.getBlue())  / 4)
        );

        // Outer wrapper — full sidebar width, fixed height
        JPanel wrapper = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                // handled by button
            }
        };
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setPreferredSize(new Dimension(220, 38));
        wrapper.setMaximumSize(new Dimension(220, 38));
        wrapper.setMinimumSize(new Dimension(220, 38));
        wrapper.setLayout(new BorderLayout());

        // Left accent bar (3 px)
        JPanel accentBar = new JPanel();
        accentBar.setBackground(BG_CARD);
        accentBar.setPreferredSize(new Dimension(3, 38));
        accentBar.setOpaque(true);
        wrapper.add(accentBar, BorderLayout.WEST);

        // Button fills the rest
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(BG_CARD);
        btn.setBorder(new EmptyBorder(6, 16, 6, 10));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverBg);
                btn.setForeground(accent);
                accentBar.setBackground(accent);
                wrapper.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BG_CARD);
                btn.setForeground(TEXT_PRIMARY);
                accentBar.setBackground(BG_CARD);
                wrapper.repaint();
            }
        });
        btn.addActionListener(e -> {
            setPageTitle(text.replaceAll("[^a-zA-Z ]", "").trim());
            action.run();
        });

        wrapper.add(btn, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Top Bar ──────────────────────────────────
    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_DARK);

        pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(TEXT_PRIMARY);
        bar.add(pageTitle, BorderLayout.WEST);

        // Right side: admin badge + logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);

        JLabel admin = new JLabel("Admin  ●");
        admin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        admin.setForeground(ACCENT);

        JLabel divider = new JLabel("|");
        divider.setForeground(BORDER_COLOR);
        divider.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton topLogout = new JButton("⏻  Logout");
        topLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        topLogout.setForeground(ACCENT_RED);
        topLogout.setBackground(new Color(40, 20, 20));
        topLogout.setOpaque(true);
        topLogout.setBorderPainted(true);
        topLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 50, 50), 1, true),
                new EmptyBorder(6, 14, 6, 14)));
        topLogout.setFocusPainted(false);
        topLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                topLogout.setBackground(ACCENT_RED);
                topLogout.setForeground(Color.WHITE);
                topLogout.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_RED, 1, true),
                        new EmptyBorder(6, 14, 6, 14)));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                topLogout.setBackground(new Color(40, 20, 20));
                topLogout.setForeground(ACCENT_RED);
                topLogout.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 50, 50), 1, true),
                        new EmptyBorder(6, 14, 6, 14)));
            }
        });
        topLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    RideAdminApp.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) showLoginPage();
        });

        rightPanel.add(admin);
        rightPanel.add(divider);
        rightPanel.add(topLogout);
        bar.add(rightPanel, BorderLayout.EAST);

        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(18, 24, 14, 24)));

        return bar;
    }

    private void setPageTitle(String title) {
        if (pageTitle != null) pageTitle.setText(title);
    }

    // ── Home Cards ───────────────────────────────
    private void showHomeCards() {
        setPageTitle("Dashboard");
        contentArea.removeAll();

        int passengerCount = getCount("SELECT COUNT(*) FROM Passengers");
        int driverCount    = getCount("SELECT COUNT(*) FROM Drivers");
        int rideCount      = getCount("SELECT COUNT(*) FROM ride_requests");
        int pendingCount   = getCount("SELECT COUNT(*) FROM ride_requests WHERE status='PENDING'");

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(0, 0, 20, 0));

        statsRow.add(statCard("Passengers",    String.valueOf(passengerCount), "👥", ACCENT_BLUE));
        statsRow.add(statCard("Drivers",       String.valueOf(driverCount),    "🚗", ACCENT_GREEN));
        statsRow.add(statCard("Total Rides",   String.valueOf(rideCount),      "🛣️", ACCENT_YELLOW));
        statsRow.add(statCard("Pending Rides", String.valueOf(pendingCount),   "⏳", ACCENT_RED));

        JPanel cards = new JPanel(new GridLayout(2, 2, 16, 16));
        cards.setOpaque(false);

        cards.add(dashCard("👥 Passengers",    "View, edit or delete passenger accounts", ACCENT_BLUE,   () -> showUserTable("Passenger")));
        cards.add(dashCard("🚗 Drivers",       "View, edit or delete driver accounts",    ACCENT_GREEN,  () -> showUserTable("Driver")));
        cards.add(dashCard("🛣️ Ride Requests", "View all ride history and statuses",       ACCENT_YELLOW, () -> showRideRequests()));
        cards.add(dashCard("🔍 Search User",   "Search passenger or driver by username",  ACCENT_PURPLE, () -> showSearchPanel()));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(statsRow, BorderLayout.NORTH);
        wrapper.add(cards,    BorderLayout.CENTER);

        contentArea.add(wrapper, BorderLayout.NORTH);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private int getCount(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private JPanel statCard(String label, String value, String icon, Color accent) {
        RoundedPanel card = new RoundedPanel(14, BG_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        ico.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 30));
        val.setForeground(accent);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(ico);
        card.add(Box.createVerticalStrut(8));
        card.add(val);
        card.add(Box.createVerticalStrut(4));
        card.add(lbl);
        return card;
    }

    private JPanel dashCard(String title, String desc, Color accent, Runnable action) {
        RoundedPanel card = new RoundedPanel(16, BG_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Precompute solid hover bg for dash cards too
        Color hoverBg = new Color(
                Math.min(255, BG_CARD.getRed()   + (accent.getRed()   - BG_CARD.getRed())   / 6),
                Math.min(255, BG_CARD.getGreen() + (accent.getGreen() - BG_CARD.getGreen()) / 6),
                Math.min(255, BG_CARD.getBlue()  + (accent.getBlue()  - BG_CARD.getBlue())  / 6)
        );

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(accent);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("<html><p style='width:200px'>" + desc + "</p></html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLbl.setForeground(TEXT_SECONDARY);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrow = new JLabel("→  Open");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        arrow.setForeground(accent);
        arrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(descLbl);
        card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(16));
        card.add(arrow);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { action.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBgColor(hoverBg);
                card.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBgColor(BG_CARD);
                card.repaint();
            }
        });
        return card;
    }

    // ════════════════════════════════════════════
    //  PASSENGERS / DRIVERS TABLE
    // ════════════════════════════════════════════
    private void showUserTable(String role) {
        setPageTitle(role + "s");
        contentArea.removeAll();

        DefaultTableModel model = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        String query = role.equals("Passenger")
                ? "SELECT u.UserID, u.Username, u.Email, u.Phone, u.CreatedAt " +
                "FROM Users u JOIN Passengers p ON u.UserID = p.UserID"
                : "SELECT u.UserID, u.Username, u.Email, u.Phone, d.LicenseNumber, d.VehicleType, d.IsAvailable, d.Rating " +
                "FROM Users u JOIN Drivers d ON u.UserID = d.UserID";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++)
                model.addColumn(meta.getColumnName(i));

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= meta.getColumnCount(); i++)
                    row.add(rs.getObject(i));
                model.addRow(row);
            }
        } catch (SQLException e) {
            showError("DB Error: " + e.getMessage());
            return;
        }

        JTable table = buildStyledTable(model);
        JScrollPane scroll = buildScrollPane(table);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnBar.setOpaque(false);
        btnBar.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton deleteBtn = accentButton("🗑  Delete Selected", ACCENT_RED, Color.WHITE);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { showError("Select a row first."); return; }
            int userId = (int) model.getValueAt(row, 0);
            String uname = (String) model.getValueAt(row, 1);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete user '" + uname + "'? This cannot be undone.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "DELETE FROM Users WHERE UserID = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                    model.removeRow(row);
                    showSuccess("User deleted successfully.");
                } catch (SQLException ex) { showError(ex.getMessage()); }
            }
        });

        JButton refreshBtn = accentButton("↻  Refresh", ACCENT, BG_DARK);
        refreshBtn.addActionListener(e -> showUserTable(role));

        btnBar.add(deleteBtn);
        btnBar.add(refreshBtn);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(buildBackBar(), BorderLayout.NORTH);
        wrapper.add(scroll, BorderLayout.CENTER);
        wrapper.add(btnBar, BorderLayout.SOUTH);

        contentArea.add(wrapper, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ════════════════════════════════════════════
    //  RIDE REQUESTS TABLE
    // ════════════════════════════════════════════
    private void showRideRequests() {
        setPageTitle("Ride Requests");
        contentArea.removeAll();

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterBar.setOpaque(false);
        filterBar.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel filterLbl = styledLabel("Filter by status:");
        filterLbl.setForeground(TEXT_SECONDARY);

        String[] statuses = {"ALL", "PENDING", "ACCEPTED", "FINISHED", "CANCELLED"};
        JComboBox<String> statusFilter = new JComboBox<>(statuses);
        statusFilter.setBackground(BG_CARD2);
        statusFilter.setForeground(TEXT_PRIMARY);
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        filterBar.add(filterLbl);
        filterBar.add(statusFilter);

        DefaultTableModel model = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
                "ID", "Passenger", "Pickup", "Dropoff", "Vehicle", "Fare", "Status", "Driver", "Time"
        });

        JTable table = buildStyledTable(model);

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                String status = val != null ? val.toString() : "";
                setForeground(switch (status) {
                    case "PENDING"   -> ACCENT_YELLOW;
                    case "ACCEPTED"  -> ACCENT_BLUE;
                    case "FINISHED"  -> ACCENT_GREEN;
                    case "CANCELLED" -> ACCENT_RED;
                    default          -> TEXT_PRIMARY;
                });
                setBackground(sel ? TABLE_SELECT : (r % 2 == 0 ? TABLE_ROW1 : TABLE_ROW2));
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return this;
            }
        });

        loadRideRequests(model, "ALL");
        statusFilter.addActionListener(e ->
                loadRideRequests(model, (String) statusFilter.getSelectedItem()));

        JScrollPane scroll = buildScrollPane(table);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnBar.setOpaque(false);
        btnBar.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton deleteBtn = accentButton("🗑  Delete Selected", ACCENT_RED, Color.WHITE);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { showError("Select a row first."); return; }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete ride #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "DELETE FROM ride_requests WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    model.removeRow(row);
                    showSuccess("Ride deleted.");
                } catch (SQLException ex) { showError(ex.getMessage()); }
            }
        });

        JButton updateBtn = accentButton("✏  Update Status", ACCENT_YELLOW, BG_DARK);
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { showError("Select a row first."); return; }
            int id = (int) model.getValueAt(row, 0);
            String[] opts = {"PENDING", "ACCEPTED", "FINISHED", "CANCELLED"};
            String chosen = (String) JOptionPane.showInputDialog(this,
                    "Select new status for Ride #" + id,
                    "Update Status", JOptionPane.PLAIN_MESSAGE,
                    null, opts, model.getValueAt(row, 6));
            if (chosen != null) {
                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "UPDATE ride_requests SET status=? WHERE id=?")) {
                    ps.setString(1, chosen);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    model.setValueAt(chosen, row, 6);
                    showSuccess("Status updated to " + chosen);
                } catch (SQLException ex) { showError(ex.getMessage()); }
            }
        });

        JButton refreshBtn = accentButton("↻  Refresh", ACCENT, BG_DARK);
        refreshBtn.addActionListener(e ->
                loadRideRequests(model, (String) statusFilter.getSelectedItem()));

        btnBar.add(deleteBtn);
        btnBar.add(updateBtn);
        btnBar.add(refreshBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(filterBar, BorderLayout.NORTH);
        top.add(scroll, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(buildBackBar(), BorderLayout.NORTH);
        wrapper.add(top, BorderLayout.CENTER);
        wrapper.add(btnBar, BorderLayout.SOUTH);

        contentArea.add(wrapper, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void loadRideRequests(DefaultTableModel model, String status) {
        model.setRowCount(0);
        String query = status.equals("ALL")
                ? "SELECT id, passenger_username, pickup_location, dropoff_location, " +
                "vehicle_type, fare, status, driver_username, request_time FROM ride_requests ORDER BY request_time DESC"
                : "SELECT id, passenger_username, pickup_location, dropoff_location, " +
                "vehicle_type, fare, status, driver_username, request_time FROM ride_requests " +
                "WHERE status=? ORDER BY request_time DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            if (!status.equals("ALL")) ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("passenger_username"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("vehicle_type"),
                        "Rs. " + rs.getString("fare"),
                        rs.getString("status"),
                        rs.getString("driver_username"),
                        rs.getString("request_time")
                });
            }
        } catch (SQLException e) { showError(e.getMessage()); }
    }

    // ════════════════════════════════════════════
    //  SEARCH
    // ════════════════════════════════════════════
    private void showSearchPanel() {
        setPageTitle("Search User");
        contentArea.removeAll();

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBar.setOpaque(false);
        searchBar.setBorder(new EmptyBorder(0, 0, 16, 0));

        JTextField field = styledField("Enter username...");
        field.setPreferredSize(new Dimension(280, 38));

        JButton btn = accentButton("Search", ACCENT, BG_DARK);

        searchBar.add(field);
        searchBar.add(btn);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Role","UserID","Username","Email","Phone","Extra"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = buildStyledTable(model);

        btn.addActionListener(e -> {
            model.setRowCount(0);
            String term = "%" + field.getText().trim() + "%";
            String query =
                    "SELECT 'Passenger' AS Role, u.UserID, u.Username, u.Email, u.Phone, " +
                            "CONVERT(VARCHAR, u.CreatedAt, 120) AS Extra " +
                            "FROM Users u " +
                            "JOIN Passengers p ON u.UserID = p.UserID " +
                            "WHERE u.Username LIKE ? " +
                            "UNION ALL " +
                            "SELECT 'Driver' AS Role, u.UserID, u.Username, u.Email, u.Phone, " +
                            "d.VehicleType AS Extra " +
                            "FROM Users u " +
                            "JOIN Drivers d ON u.UserID = d.UserID " +
                            "WHERE u.Username LIKE ?";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, term);
                ps.setString(2, term);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("Role"),
                            rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getString("Email"),
                            rs.getString("Phone"),
                            rs.getString("Extra")
                    });
                }
                if (model.getRowCount() == 0)
                    showError("No users found for: " + field.getText());
            } catch (SQLException ex) { showError(ex.getMessage()); }
        });

        field.addActionListener(e -> btn.doClick());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(buildBackBar(), BorderLayout.NORTH);
        topBar.add(searchBar, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(topBar, BorderLayout.NORTH);
        wrapper.add(buildScrollPane(table), BorderLayout.CENTER);

        contentArea.add(wrapper, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ════════════════════════════════════════════
    //  SHARED TABLE BUILDER
    // ════════════════════════════════════════════
    private JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(TABLE_ROW1);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setGridColor(BORDER_COLOR);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(TABLE_SELECT);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(TABLE_HEADER);
        table.getTableHeader().setForeground(ACCENT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? TABLE_SELECT : (row % 2 == 0 ? TABLE_ROW1 : TABLE_ROW2));
                setForeground(sel ? Color.WHITE : TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                return this;
            }
        });

        return table;
    }

    private JScrollPane buildScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.setBackground(BG_CARD);
        scroll.getViewport().setBackground(TABLE_ROW1);
        return scroll;
    }

    // ════════════════════════════════════════════
    //  BACK BUTTON HELPER
    // ════════════════════════════════════════════
    private JPanel buildBackBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 14, 0));

        JButton back = new JButton("← Back to Dashboard");
        back.setFont(new Font("Segoe UI", Font.BOLD, 13));
        back.setForeground(TEXT_SECONDARY);
        back.setBackground(BG_CARD2);
        back.setOpaque(true);
        back.setBorderPainted(true);
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(7, 16, 7, 16)));
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                back.setBackground(new Color(34, 45, 75));
                back.setForeground(ACCENT);
                back.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 1, true),
                        new EmptyBorder(7, 16, 7, 16)));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                back.setBackground(BG_CARD2);
                back.setForeground(TEXT_SECONDARY);
                back.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(7, 16, 7, 16)));
            }
        });
        back.addActionListener(e -> showHomeCards());

        bar.add(back);
        return bar;
    }

    // ════════════════════════════════════════════
    //  UI HELPERS
    // ════════════════════════════════════════════
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField(String text) {
        JTextField f = new JTextField(text);
        styleTextField(f);
        return f;
    }

    private void styleTextField(JTextField f) {
        f.setBackground(BG_CARD2);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton accentButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // ════════════════════════════════════════════
    //  ROUNDED PANEL
    // ════════════════════════════════════════════
    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bg;

        RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        void setBgColor(Color c) { this.bg = c; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RideAdminApp::new);
    }
}