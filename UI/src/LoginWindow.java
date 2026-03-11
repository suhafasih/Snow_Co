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
        // Set up the main window
        setTitle("Library Catalogue");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window

        // Create CardLayout for switching between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create all panels
        createLoginPanel();
        createAdminDashboard();
        createUserDashboard();

        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        mainPanel.add(userDashboard, "USER_DASHBOARD");

        // Add main panel to frame
        add(mainPanel);

        // Set up menu bar
        setupMenuBar();
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout(10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creating input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        // Add components to input panel
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        logoutButton.setEnabled(false); // Initially disabled

        buttonPanel.add(loginButton);
        buttonPanel.add(logoutButton);

        // Create status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Status: Not logged in");
        statusPanel.add(statusLabel);

        // Add all panels to main panel
        loginPanel.add(inputPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);
        loginPanel.add(statusPanel, BorderLayout.NORTH);

        // Add action listeners
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

        // Books Management Tabs
        JPanel booksPanel = createBooksManagementPanel();
        tabbedPane.addTab("Books Management", booksPanel);


        //Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton dashboardLogoutBtn = new JButton("Logout");
        dashboardLogoutBtn.addActionListener(e -> performLogout());
        bottomPanel.add(dashboardLogoutBtn);

        adminDashboard.add(headerPanel, BorderLayout.NORTH);
        adminDashboard.add(tabbedPane, BorderLayout.CENTER);
        adminDashboard.add(bottomPanel, BorderLayout.SOUTH);
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
        logoutButton.addActionListener(e -> performLogout());

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