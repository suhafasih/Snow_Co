import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton logoutButton;
    private JLabel statusLabel;
    private boolean isLoggedIn = false;
    private String currentUser = "";
    private String currentUserRole = "";

    // Dashboard panels
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel adminDashboard;
    private JPanel userDashboard;
    private CardLayout cardLayout;

    public LoginWindow() {
        setTitle("Library Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,600); // bigger for background
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createAdminDashboard();
        createUserDashboard();

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        mainPanel.add(userDashboard, "USER_DASHBOARD");

        add(mainPanel); // only add this

        setupMenuBar();
    }

    private void createLoginPanel() {

        // Main panel with background image
        loginPanel = new BackgroundPanel("libraryimage.jfif");
        loginPanel.setLayout(new GridBagLayout());

        // Dark overlay panel (simulates blur)
        JPanel overlay = new JPanel();
        overlay.setBackground(new Color(50, 30, 25, 180));
        overlay.setPreferredSize(new Dimension(420, 500));
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
        overlay.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        // Title - center aligned
        JLabel title = new JLabel("Snow");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Library Catalogue System");
        subtitle.setForeground(new Color(220,220,220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);



        // Add components with center alignment
        overlay.add(title);
        overlay.add(Box.createVerticalStrut(5));
        overlay.add(subtitle);
        overlay.add(Box.createVerticalStrut(30));

        overlay.add(Box.createVerticalStrut(25));

        // Email label - center aligned
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new RoundedTextField(20);
        usernameField.setMaximumSize(new Dimension(300,35));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password label - center aligned
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new RoundedPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300,35));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login Button - center aligned
        loginButton = new RoundedButton("Sign In");
        loginButton.setMaximumSize(new Dimension(300,40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(140,100,85));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Status label - center aligned
        statusLabel = new JLabel("Status: Not logged in");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add all components with spacing
        overlay.add(emailLabel);
        overlay.add(Box.createVerticalStrut(5));
        overlay.add(usernameField);
        overlay.add(Box.createVerticalStrut(15));
        overlay.add(passLabel);
        overlay.add(Box.createVerticalStrut(5));
        overlay.add(passwordField);
        overlay.add(Box.createVerticalStrut(25));
        overlay.add(loginButton);
        overlay.add(Box.createVerticalStrut(15));
        overlay.add(statusLabel);

        // Add rigid areas at top and bottom for vertical centering
        // This pushes content to the vertical center
        overlay.add(Box.createVerticalGlue(), 0); // Add at beginning
        overlay.add(Box.createVerticalGlue());     // Add at end

        loginPanel.add(overlay);

        setupActionListeners();
    }

    private void createAdminDashboard() {
        adminDashboard = new JPanel(new BorderLayout(10, 10));
        adminDashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create tabbed pane for admin functions
        JTabbedPane tabbedPane = new JTabbedPane();

        // Books Management Tab
        JPanel booksPanel = createBooksManagementPanel();
        tabbedPane.addTab("Books Management", booksPanel);


        // Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton dashboardLogoutBtn = new JButton("Logout");
        dashboardLogoutBtn.addActionListener(e -> performLogout());
        bottomPanel.add(dashboardLogoutBtn);

        adminDashboard.add(headerPanel, BorderLayout.NORTH);
        adminDashboard.add(tabbedPane, BorderLayout.CENTER);
        adminDashboard.add(bottomPanel, BorderLayout.SOUTH);
    }

    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String path) {
            backgroundImage = new ImageIcon(path).getImage();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void createUserDashboard() {
        userDashboard = new JPanel(new BorderLayout(10, 10));
        userDashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("User Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 153, 76));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Welcome message with user info
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome to the Library System");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomePanel.add(welcomeLabel);

        // Create tabbed pane for user functions
        JTabbedPane tabbedPane = new JTabbedPane();

        // Browse Books Tab
        JPanel browsePanel = createBrowseBooksPanel();
        tabbedPane.addTab("Browse Books", browsePanel);

        // My Account Tab
        JPanel accountPanel = createMyAccountPanel();
        tabbedPane.addTab("My Account", accountPanel);

        // Search Tab
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab("Search", searchPanel);

        // Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton dashboardLogoutBtn = new JButton("Logout");
        dashboardLogoutBtn.addActionListener(e -> performLogout());
        bottomPanel.add(dashboardLogoutBtn);

        userDashboard.add(headerPanel, BorderLayout.NORTH);
        userDashboard.add(welcomePanel, BorderLayout.CENTER);
        userDashboard.add(tabbedPane, BorderLayout.SOUTH);
    }

    // Admin Panel Components
    private JPanel createBooksManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton("Add Book"));
        toolBar.add(new JButton("Edit Book"));
        toolBar.add(new JButton("Delete Book"));
        toolBar.addSeparator();

        // Table for books
        String[] columns = {"ID", "Title", "Author", "ISBN", "Status", "Location"};
        Object[][] data = {
                {1, "The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", "Available", "A1"},
                {2, "To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", "Checked Out", "B2"},
                {3, "1984", "George Orwell", "978-0-452-28423-4", "Available", "C3"}
        };

        JTable booksTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(booksTable);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(new JTextField(20));
        searchPanel.add(new JButton("Search"));

        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }


    // User Panel Components
    private JPanel createBrowseBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Category filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Category:"));
        String[] categories = {"All", "Fiction", "Non-Fiction", "Science", "History", "Biography"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        filterPanel.add(categoryBox);
        filterPanel.add(new JButton("Apply Filter"));

        // Books display
        String[] columns = {"Title", "Author", "Category", "Status", "Due Date"};
        Object[][] data = {
                {"The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Available", "-"},
                {"To Kill a Mockingbird", "Harper Lee", "Fiction", "Checked Out", "2024-02-01"},
                {"A Brief History of Time", "Stephen Hawking", "Science", "Available", "-"}
        };

        JTable booksTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(booksTable);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(new JButton("View Details"));
        actionPanel.add(new JButton("Add to Wishlist"));

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMyAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // User info
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));

        infoPanel.add(new JLabel("Username:"));
        infoPanel.add(new JLabel(currentUser));
        infoPanel.add(new JLabel("Role:"));
        infoPanel.add(new JLabel(currentUserRole));
        infoPanel.add(new JLabel("Member Since:"));
        infoPanel.add(new JLabel("2024-01-01"));
        infoPanel.add(new JLabel("Books Borrowed:"));
        infoPanel.add(new JLabel("3"));

        // Currently borrowed books
        String[] columns = {"Book Title", "Borrow Date", "Due Date", "Status", "Fine"};
        Object[][] data = {
                {"The Great Gatsby", "2024-01-10", "2024-01-24", "Active", "$0.00"},
                {"1984", "2024-01-05", "2024-01-19", "Overdue", "$2.50"}
        };

        JTable borrowedTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(borrowedTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Currently Borrowed Books"));

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search bar
        JPanel searchBarPanel = new JPanel(new BorderLayout(5, 5));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);

        // Advanced search options
        JPanel advancedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        advancedPanel.add(new JLabel("Search in:"));
        String[] searchIn = {"All Fields", "Title", "Author", "ISBN"};
        JComboBox<String> searchInBox = new JComboBox<>(searchIn);
        advancedPanel.add(searchInBox);

        String[] categories = {"All Categories", "Fiction", "Non-Fiction", "Science"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        advancedPanel.add(categoryBox);

        advancedPanel.add(new JCheckBox("Available only"));

        panel.add(searchBarPanel, BorderLayout.NORTH);
        panel.add(advancedPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportCard(String title, String stat1, String stat2, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stat1Label = new JLabel(stat1);
        stat1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stat2Label = new JLabel(stat2);
        stat2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewBtn = new JButton("View Details");
        viewBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(stat1Label);
        card.add(stat2Label);
        card.add(Box.createVerticalStrut(10));
        card.add(viewBtn);
        card.add(Box.createVerticalStrut(10));

        return card;
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);


        setJMenuBar(menuBar);
    }

    private void setupActionListeners() {
        loginButton.addActionListener(e -> performLogin());
//        logoutButton.addActionListener(e -> performLogout());

        usernameField.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Check credentials and determine role
        if (username.equals("admin") && password.equals("password")) {
            currentUser = username;
            currentUserRole = "ADMIN";
            isLoggedIn = true;

            statusLabel.setText("Status: Logged in as Administrator");
            statusLabel.setForeground(Color.GREEN);

            // Switch to admin dashboard
            cardLayout.show(mainPanel, "ADMIN_DASHBOARD");

            JOptionPane.showMessageDialog(this,
                    "Admin login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } else if (username.equals("user") && password.equals("password")) {
            currentUser = username;
            currentUserRole = "USER";
            isLoggedIn = true;

            statusLabel.setText("Status: Logged in as User");
            statusLabel.setForeground(Color.GREEN);

            // Update welcome message in user dashboard
            updateUserDashboard();

            // Switch to user dashboard
            cardLayout.show(mainPanel, "USER_DASHBOARD");

            JOptionPane.showMessageDialog(this,
                    "User login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void updateUserDashboard() {
        // Find and update the welcome message in user dashboard
        Component[] components = userDashboard.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] subComponents = panel.getComponents();
                for (Component subComp : subComponents) {
                    if (subComp instanceof JPanel) {
                        JPanel subPanel = (JPanel) subComp;
                        Component[] subSubComponents = subPanel.getComponents();
                        for (Component subSubComp : subSubComponents) {
                            if (subSubComp instanceof JLabel) {
                                JLabel label = (JLabel) subSubComp;
                                if (label.getText().equals("Welcome to the Library System")) {
                                    label.setText("Welcome, " + currentUser + "! (User)");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void performLogout() {
        isLoggedIn = false;
        currentUser = "";
        currentUserRole = "";
        statusLabel.setText("Status: Not logged in");
        statusLabel.setForeground(Color.BLACK);

        // Update UI for logged out state
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);

        // Clear fields
        usernameField.setText("");
        passwordField.setText("");

        // Switch back to login panel
        cardLayout.show(mainPanel, "LOGIN");

        JOptionPane.showMessageDialog(this,
                "You have been logged out successfully!",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginWindow().setVisible(true);
        });
    }
}