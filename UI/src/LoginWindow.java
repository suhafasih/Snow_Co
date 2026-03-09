//javac -cp ".;../lib/gson-2.10.1.jar" *.java
//java -cp ".;../lib/gson-2.10.1.jar" LoginWindow
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


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
        title.setFont(new Font("Georgia", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Library Catalogue System");
        subtitle.setFont(new Font("Garamond", Font.PLAIN, 11));
        subtitle.setForeground(new Color(220,220,220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);



        // Add title components
        overlay.add(title);
        overlay.add(Box.createVerticalStrut(5));
        overlay.add(subtitle);
        overlay.add(Box.createVerticalStrut(30));
        overlay.add(Box.createVerticalStrut(25));

        // Create a panel for the form inputs with left alignment
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false); // Make transparent
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(300, 200));

        // Email label - left aligned within form panel
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Garamond", Font.PLAIN, 13));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new RoundedTextField(20);
        usernameField.setMaximumSize(new Dimension(300,35));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password label - left aligned within form panel
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Garamond", Font.PLAIN, 13));
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new RoundedPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300,35));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components to form panel
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);

        // Login Button - center aligned (separate from form panel)
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

        // Add form panel, button, and status to main overlay
        overlay.add(formPanel);
        overlay.add(Box.createVerticalStrut(20));
        overlay.add(loginButton);
        overlay.add(Box.createVerticalStrut(15));
        overlay.add(statusLabel);

        // Add glue for vertical centering
        overlay.add(Box.createVerticalGlue(), 0);
        overlay.add(Box.createVerticalGlue());

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

        userDashboard = new JPanel(new BorderLayout());

        // ---------- HEADER ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(140,100,85));
        header.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

        JLabel title = new JLabel("Library Catalogue");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(Color.WHITE);


        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);

        JButton logoutBtn = new RoundedButton("Logout");
        logoutBtn.addActionListener(e -> performLogout());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        userDashboard.add(header, BorderLayout.NORTH);


        // ---------- MAIN CONTENT ----------
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(156, 165, 118));
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


        // ---------- SEARCH BAR ----------
        JTextField search = new RoundedTextField(20);
        search.setMaximumSize(new Dimension(Integer.MAX_VALUE,35));
        search.setAlignmentX(Component.LEFT_ALIGNMENT);  // Add this
        content.add(search);

        content.add(Box.createVerticalStrut(20));


        // ---------- CATEGORY BUTTONS ----------
        RoundedPanel categories = new RoundedPanel(30);
        categories.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categories.setBackground(new Color(156, 165, 118)); // cream
        categories.setForeground(new Color(140,100,85));
        categories.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        categories.setAlignmentX(Component.LEFT_ALIGNMENT);  // Add this
        categories.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        categories.setPreferredSize(new Dimension(800, 70));
        String[] cats = {
                "All Books","Fiction","Mystery","Science Fiction",
                "Romance","Fantasy","Biography","Historical Fiction","Non-Fiction"
        };

        for(String c : cats){
            JButton btn = new JButton(c);
            categories.add(btn);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Georgia", Font.PLAIN, 13));
            btn.setForeground(new Color(140,100,85));
            btn.setBackground(new Color(156, 165, 118));


        }
        
        content.add(categories);

        content.add(Box.createVerticalStrut(20));


        // ---------- CURRENTLY BORROWED ----------
        JLabel borrowedLabel = new JLabel("Currently Borrowed");
        borrowedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        borrowedLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        borrowedLabel.setForeground(Color.WHITE);
        borrowedLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, borrowedLabel.getPreferredSize().height)); // Add this
        content.add(borrowedLabel);

        JPanel booksGrid1 = createBooksGrid();
        booksGrid1.setAlignmentX(Component.LEFT_ALIGNMENT);  // Add this
        booksGrid1.setMaximumSize(new Dimension(Integer.MAX_VALUE, booksGrid1.getPreferredSize().height)); // Add this
        content.add(booksGrid1);

        content.add(Box.createVerticalStrut(20));


        // ---------- ALL BOOKS ----------
        JLabel allBooks = new JLabel("All Books");
        allBooks.setAlignmentX(Component.LEFT_ALIGNMENT);
        allBooks.setFont(new Font("Georgia", Font.BOLD, 20));
        allBooks.setForeground(Color.WHITE);
        allBooks.setMaximumSize(new Dimension(Integer.MAX_VALUE, allBooks.getPreferredSize().height)); // Add this
        content.add(allBooks);

        JPanel booksGrid2 = createBooksGrid();
        booksGrid2.setAlignmentX(Component.LEFT_ALIGNMENT);  // Add this
        booksGrid2.setMaximumSize(new Dimension(Integer.MAX_VALUE, booksGrid2.getPreferredSize().height)); // Add this
        content.add(booksGrid2);


        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Optional: smoother scrolling

        userDashboard.add(scroll, BorderLayout.CENTER);
    }
    private JPanel createBooksGrid(){

        JPanel grid = new JPanel(new GridLayout(1,5,20,20));
        grid.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        grid.setBackground(new Color(156, 165, 118));

        grid.add(createBookCard(
                "The Silent Patient",
                "Alex Michaelides",
                "https://covers.openlibrary.org/b/isbn/9781250301697-L.jpg"));

        grid.add(createBookCard(
                "Project Hail Mary",
                "Andy Weir",
                "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg"));


        grid.add(createBookCard(
                "Educated",
                "Tara Westover",
                "https://covers.openlibrary.org/b/isbn/9780399590504-L.jpg"));

        grid.add(createBookCard(
                "The Song of Achilles",
                "Madeline Miller",
                "https://covers.openlibrary.org/b/isbn/9780062060624-L.jpg"));

        grid.add(createBookCard(
                "Educated",
                "Tara Westover",
                "https://covers.openlibrary.org/b/isbn/9780399590504-L.jpg"));


        return grid;
    }
    private JPanel createBookCard(String title, String author, String coverUrl) {

        JPanel card = new RoundedPanel(30);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 300));
        card.setBackground(new Color(140, 108, 95));

        // Center panel for cover image
        JPanel coverPanel = new RoundedPanel(20);
        coverPanel.setBackground(new Color(140, 108, 95));

        try {
            BufferedImage img = ImageIO.read(new URL(coverUrl));
            Image scaled = img.getScaledInstance(160, 200, Image.SCALE_SMOOTH);

            JLabel cover = new JLabel(new ImageIcon(scaled));
            coverPanel.add(cover);

        } catch (Exception e) {
            JLabel placeholder = new JLabel("No Cover");
            placeholder.setFont(new Font("Garamond", Font.ITALIC, 14));
            placeholder.setForeground(Color.WHITE);
            coverPanel.add(placeholder);
        }

        card.add(coverPanel, BorderLayout.CENTER); // Changed from NORTH to CENTER

        // Info panel with centered text
        JPanel info = new RoundedPanel(20);
        info.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better centering
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        info.setBackground(new Color(140, 108, 95));

        // Panel to hold text with vertical BoxLayout
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(140, 108, 95));
        textPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Garamond", Font.BOLD, 16)); // Slightly smaller to fit
        t.setForeground(Color.WHITE);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Handle long titles
        if (title.length() > 20) {
            t.setText("<html><div style='text-align: center; width: 150px;'>" + title + "</div></html>");
        }

        JLabel a = new JLabel(author);
        a.setFont(new Font("Garamond", Font.BOLD, 14));
        a.setForeground(Color.WHITE);
        a.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Handle long author names
        if (author.length() > 25) {
            a.setText("<html><div style='text-align: center; width: 150px;'>" + author + "</div></html>");
        }

        textPanel.add(t);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(a);

        // Center the textPanel within info panel
        info.add(textPanel, new GridBagConstraints());

        card.add(info, BorderLayout.SOUTH);

        return card;
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