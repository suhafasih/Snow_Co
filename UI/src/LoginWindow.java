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
import javax.swing.table.DefaultTableModel;
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
    private JTable booksTable;

    // Dashboard panels
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel adminDashboard;
    private JPanel userDashboard;
    private CardLayout cardLayout;

    public LoginWindow() {
        setTitle("Library Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createAdminDashboard();
        createUserDashboard();

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        mainPanel.add(userDashboard, "USER_DASHBOARD");

        add(mainPanel);

        setupMenuBar();
    }

    private void createLoginPanel() {

        // Main panel with background image
        loginPanel = new BackgroundPanel("libcat2.png");
        loginPanel.setLayout(new GridBagLayout());


        // Dark overlay panel
        JPanel overlay = new RoundedPanel(150);
        overlay.setBackground(new Color(50, 30, 25, 180));
        overlay.setPreferredSize(new Dimension(420, 500));
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
        overlay.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        // Title
        JLabel title = new JLabel("Snow");
        title.setFont(new Font("Georgia", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Library Catalogue System");
        subtitle.setFont(new Font("Garamond", Font.BOLD, 26));
        subtitle.setForeground(new Color(220,220,220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);



        //title components
        overlay.add(title);
        overlay.add(Box.createVerticalStrut(5));
        overlay.add(subtitle);
        overlay.add(Box.createVerticalStrut(30));
        overlay.add(Box.createVerticalStrut(25));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false); // Make transparent
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(300, 200));

        // Email label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Garamond", Font.PLAIN, 20));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new RoundedTextField(20);
        usernameField.setMaximumSize(new Dimension(300,35));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Garamond", Font.PLAIN, 20));
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new RoundedPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300,35));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);

        // Login Button
        loginButton = new RoundedButton("Sign In");
        loginButton.setMaximumSize(new Dimension(300,40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(140,100,85));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Garamond", Font.BOLD, 20));

        // Status label
        statusLabel = new JLabel("Status: Not logged in");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Garamond", Font.PLAIN, 15));

        overlay.add(formPanel);
        overlay.add(Box.createVerticalStrut(20));
        overlay.add(loginButton);
        overlay.add(Box.createVerticalStrut(15));
        overlay.add(statusLabel);

        overlay.add(Box.createVerticalGlue(), 0);
        overlay.add(Box.createVerticalGlue());

        loginPanel.add(overlay);

        setupActionListeners();
    }

    private void createAdminDashboard() {

        adminDashboard = new JPanel(new BorderLayout());

        // color pallete
        Color cream = new Color(245,240,230);
        Color sage = new Color(156,165,118);
        Color warmBrown = new Color(140,100,85);
        Color darkBrown = new Color(80,60,50);
        Color sand = new Color(220,210,190);

        adminDashboard.setBackground(cream);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(sage);
        header.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));

        JLabel title = new JLabel("Admin Control Panel");
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JButton logoutBtn = new RoundedButton("Logout");
        logoutBtn.setBackground(warmBrown);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setPreferredSize(new Dimension(100,35));
        logoutBtn.addActionListener(e -> performLogout());

        header.add(title, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        adminDashboard.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(cream);
        content.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        JLabel sectionTitle = new JLabel("Library Management");
        sectionTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        sectionTitle.setForeground(darkBrown);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(20));

        JPanel actionGrid = createAdminActionCards(sage,warmBrown,darkBrown,sand);
        actionGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(actionGrid);
        content.add(Box.createVerticalStrut(30));

        JPanel booksPanel = createBooksManagementPanel();
        booksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(booksPanel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        adminDashboard.add(scroll, BorderLayout.CENTER);
    }

    private JPanel createAdminActionCards(Color sage, Color warmBrown, Color darkBrown, Color sand) {

        JPanel grid = new JPanel(new GridLayout(1,4,10,0));
        grid.setBackground(new Color(245,240,230));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));

        grid.add(createAdminCard("Manage Books",sage,darkBrown,sand));
        grid.add(createAdminCard("Manage Users",sage,darkBrown,sand));
        grid.add(createAdminCard("Overdue Books",sage,darkBrown,sand));

        return grid;
    }
    private JPanel createAdminCard(String title,Color sage,Color darkBrown,Color sand){

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(sand,1),
                BorderFactory.createEmptyBorder(20,20,20,20)
        ));


        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Georgia",Font.BOLD,16));
        titleLabel.setForeground(darkBrown);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);

        card.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(sage,2),
                        BorderFactory.createEmptyBorder(19,19,19,19)
                ));
            }
            public void mouseExited(MouseEvent e){
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(sand,1),
                        BorderFactory.createEmptyBorder(20,20,20,20)
                ));
            }
        });

        return card;
    }

    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String path) {
            try {
                java.net.URL imageUrl = getClass().getResource(path);

                if (imageUrl != null) {
                    System.out.println("Loaded from classpath: " + imageUrl);
                    backgroundImage = new ImageIcon(imageUrl).getImage();
                    return;
                }

                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    System.out.println("Loaded from file system: " + file.getAbsolutePath());
                    backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
                    return;
                }

                String[] fallbackPaths = {
                        "libcat2.png",
                        "UI/resources/libcat2.png",
                        "./UI/resources/libcat2.png",
                        "../UI/resources/libcat2.png"
                };

                for (String fallback : fallbackPaths) {
                    file = new java.io.File(fallback);
                    if (file.exists()) {
                        System.out.println("Loaded from fallback: " + file.getAbsolutePath());
                        backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
                        return;
                    }
                }

                System.out.println("Image not found anywhere: " + path);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void createUserDashboard() {
        userDashboard = new JPanel(new BorderLayout());

        // Earthy color palette
        Color cream = new Color(245, 240, 230);      // Warm cream background
        Color sage = new Color(156, 165, 118);        // Sage green
        Color warmBrown = new Color(140, 100, 85);    // Warm brown
        Color darkBrown = new Color(80, 60, 50);       // Dark brown for text
        Color sand = new Color(220, 210, 190);         // Sand color
        Color moss = new Color(110, 120, 90);          // Moss green
        Color terracotta = new Color(190, 120, 90);    // Terracotta

        userDashboard.setBackground(cream);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(sage);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Library Catalogue");
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JButton logoutBtn = new RoundedButton("Logout");
        logoutBtn.setBackground(warmBrown);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.addActionListener(e -> performLogout());

        header.add(title, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        userDashboard.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(cream);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(cream);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JTextField searchField = new RoundedTextField(20);
        searchField.setPreferredSize(new Dimension(200, 45));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.putClientProperty("JTextField.placeholderText", "Search for books, authors, or genres...");


        JButton searchButton = new RoundedButton("Search");
        searchButton.setBackground(warmBrown);
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(100, 45));
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        content.add(searchPanel);
        content.add(Box.createVerticalStrut(30));

        JLabel categoriesLabel = new JLabel("Browse Categories");
        categoriesLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        categoriesLabel.setForeground(darkBrown);
        categoriesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(categoriesLabel);
        content.add(Box.createVerticalStrut(15));

        JPanel categoryPills = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        categoryPills.setBackground(cream);
        categoryPills.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] categories = {
                "All", "Fiction", "Mystery", "Sci-Fi", "Romance",
                "Fantasy", "Biography", "History", "Non-Fiction"
        };

        for (String cat : categories) {
            JButton pill = new JButton(cat);
            pill.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            pill.setForeground(darkBrown);
            pill.setBackground(sand);
            pill.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(sage, 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            pill.setFocusPainted(false);
            pill.setCursor(new Cursor(Cursor.HAND_CURSOR));

            pill.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    pill.setBackground(sage);
                    pill.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e) {
                    pill.setBackground(sand);
                    pill.setForeground(darkBrown);
                }
            });

            categoryPills.add(pill);
        }

        content.add(categoryPills);
        content.add(Box.createVerticalStrut(30));

        //currently borrowed
        JPanel borrowedHeader = new JPanel(new BorderLayout());
        borrowedHeader.setBackground(cream);
        borrowedHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        borrowedHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel borrowedLabel = new JLabel("Currently Borrowed");
        borrowedLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        borrowedLabel.setForeground(darkBrown);

        //count badge
        JLabel countBadge = new JLabel("2 books");
        countBadge.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countBadge.setForeground(sage);
        countBadge.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel borrowedTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        borrowedTitlePanel.setBackground(cream);
        borrowedTitlePanel.add(borrowedLabel);
        borrowedTitlePanel.add(countBadge);

        borrowedHeader.add(borrowedTitlePanel, BorderLayout.WEST);

        content.add(borrowedHeader);
        content.add(Box.createVerticalStrut(15));

        JPanel borrowedGrid = createEarthyBooksGrid(sage, warmBrown, darkBrown, sand, moss, terracotta, true);
        borrowedGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(borrowedGrid);
        content.add(Box.createVerticalStrut(40));

        // all books
        JPanel allBooksHeader = new JPanel(new BorderLayout());
        allBooksHeader.setBackground(cream);
        allBooksHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        allBooksHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel allBooksLabel = new JLabel("All Books");
        allBooksLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        allBooksLabel.setForeground(darkBrown);

        // Sort dropdown
        JComboBox<String> sortDropdown = new JComboBox<>(new String[]{"Sort by: Popular", "Sort by: Title", "Sort by: Author", "Sort by: Newest"});
        sortDropdown.setBackground(Color.WHITE);
        sortDropdown.setForeground(darkBrown);
        sortDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortDropdown.setPreferredSize(new Dimension(150, 30));

        allBooksHeader.add(allBooksLabel, BorderLayout.WEST);
        allBooksHeader.add(sortDropdown, BorderLayout.EAST);

        content.add(allBooksHeader);
        content.add(Box.createVerticalStrut(15));

        JPanel allBooksGrid = createEarthyBooksGrid(sage, warmBrown, darkBrown, sand, moss, terracotta, false);
        allBooksGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(allBooksGrid);

        // Load More button
        JButton loadMoreBtn = new RoundedButton("Load More Books");
        loadMoreBtn.setBackground(sand);
        loadMoreBtn.setForeground(darkBrown);
        loadMoreBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadMoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadMoreBtn.setMaximumSize(new Dimension(200, 40));
        loadMoreBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loadMoreBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loadMoreBtn.setBackground(sage);
                loadMoreBtn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                loadMoreBtn.setBackground(sand);
                loadMoreBtn.setForeground(darkBrown);
            }
        });

        content.add(Box.createVerticalStrut(20));
        content.add(loadMoreBtn);
        content.add(Box.createVerticalStrut(20));

        //search bar logic
        ActionListener performSearch = e -> {
            String query = searchField.getText();

            if (!query.trim().isEmpty()) {
                searchButton.setText("Loading...");
                searchButton.setEnabled(false);
                allBooksGrid.removeAll();

                // Use swignWorker to fetch data in the background
                SwingWorker<JsonArray, Void> worker = new SwingWorker<JsonArray, Void>() {
                    @Override
                    protected JsonArray doInBackground() throws Exception {
                        //preventing lag
                        return (com.google.gson.JsonArray) API.searchBooks(query);
                    }
                    @Override
                    protected void done() {
                        try {
                            JsonArray results = get(); // Get the background results

                            for (int i = 0; i < Math.min(results.size(), 8); i++) {
                                JsonObject book = results.get(i).getAsJsonObject();

                                String bookTitle = book.has("title") ? book.get("title").getAsString() : "Unknown Title";
                                String bookAuthor = "Unknown Author";
                                if (book.has("author_name")) {
                                    bookAuthor = book.get("author_name").getAsJsonArray().get(0).getAsString();
                                }

                                String coverUrl = "";
                                if (book.has("cover_i")) {
                                    String coverId = book.get("cover_i").getAsString();
                                    coverUrl = "https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg";
                                }

                                allBooksGrid.add(createEarthyBookCard(bookTitle, bookAuthor, coverUrl, "Available", "",
                                        sage, warmBrown, darkBrown, sand, moss, terracotta));
                            }

                            allBooksGrid.revalidate();
                            allBooksGrid.repaint();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            // Reset the button once the search is done
                            searchButton.setText("Search");
                            searchButton.setEnabled(true);
                        }
                    }
                };
                worker.execute();
            }
        };

        searchField.addActionListener(performSearch);
        searchButton.addActionListener(performSearch);
        // --------------------------------------

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBackground(sand);

        userDashboard.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createEarthyBooksGrid(Color sage, Color warmBrown, Color darkBrown, Color sand, Color moss, Color terracotta, boolean isBorrowed) {
        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBackground(new Color(245, 240, 230)); // cream
        grid.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        String[][] books;
        if (isBorrowed) {
            books = new String[][]{
                    {"The Silent Patient", "Alex Michaelides", "https://covers.openlibrary.org/b/isbn/9781250301697-L.jpg", "Due in 3 days", "high"},
                    {"Project Hail Mary", "Andy Weir", "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg", "Due tomorrow", "critical"}
            };
        } else {
            books = new String[][]{
                    {"The Silent Patient", "Alex Michaelides", "https://covers.openlibrary.org/b/isbn/9781250301697-L.jpg", "Available", ""},
                    {"Project Hail Mary", "Andy Weir", "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg", "Available", ""},
                    {"Educated", "Tara Westover", "https://covers.openlibrary.org/b/isbn/9780399590504-L.jpg", "Checked Out", ""},
                    {"The Song of Achilles", "Madeline Miller", "https://covers.openlibrary.org/b/isbn/9780062060624-L.jpg", "Available", ""},
                    {"Dune", "Frank Herbert", "https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg", "Available", ""},
                    {"Becoming", "Michelle Obama", "https://covers.openlibrary.org/b/isbn/9781524763138-L.jpg", "Available", ""},
                    {"Where the Crawdads Sing", "Delia Owens", "https://covers.openlibrary.org/b/isbn/9780735219090-L.jpg", "Checked Out", ""},
                    {"Atomic Habits", "James Clear", "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg", "Available", ""}
            };
        }

        for (String[] book : books) {
            grid.add(createEarthyBookCard(book[0], book[1], book[2], book[3], book.length > 4 ? book[4] : "",
                    sage, warmBrown, darkBrown, sand, moss, terracotta));
        }

        return grid;
    }

    private JPanel createEarthyBookCard(String title, String author, String coverUrl, String status, String urgency,
                                        Color sage, Color warmBrown, Color darkBrown, Color sand, Color moss, Color terracotta) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(sand, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(200, 340));

        // Cover image
        JPanel coverPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        coverPanel.setBackground(Color.WHITE);

        try {
            BufferedImage img = ImageIO.read(new URL(coverUrl));
            Image scaled = img.getScaledInstance(140, 180, Image.SCALE_SMOOTH);
            JLabel cover = new JLabel(new ImageIcon(scaled));
            coverPanel.add(cover);
        } catch (Exception e) {
            JPanel placeholder = new JPanel();
            placeholder.setPreferredSize(new Dimension(140, 180));
            placeholder.setBackground(sand);

            JLabel noCoverLabel = new JLabel("📚");
            noCoverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            noCoverLabel.setForeground(sage);
            placeholder.add(noCoverLabel);

            coverPanel.add(placeholder);
        }

        card.add(coverPanel, BorderLayout.NORTH);

        // Book info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel titleLabel = new JLabel(title.length() > 20 ? title.substring(0, 18) + "..." : title);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        titleLabel.setForeground(darkBrown);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        authorLabel.setForeground(warmBrown);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(authorLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        // Status badge
        JPanel statusBadge = new JPanel();
        if (status.equals("Available")) {
            statusBadge.setBackground(new Color(230, 240, 220)); // light sage
            statusBadge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        } else if (status.contains("Due")) {
            if (urgency.equals("critical")) {
                statusBadge.setBackground(new Color(255, 220, 220)); // light red for urgent
            } else {
                statusBadge.setBackground(new Color(255, 235, 215)); // light orange
            }
            statusBadge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        } else {
            statusBadge.setBackground(new Color(240, 225, 215)); // light terracotta
            statusBadge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        statusBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        if (status.equals("Available")) {
            statusLabel.setForeground(moss);
        } else if (status.contains("Due")) {
            if (urgency.equals("critical")) {
                statusLabel.setForeground(new Color(200, 50, 50)); // red for urgent
            } else {
                statusLabel.setForeground(new Color(200, 100, 50)); // orange
            }
        } else {
            statusLabel.setForeground(terracotta);
        }
        statusBadge.add(statusLabel);

        infoPanel.add(statusBadge);

        // Add borrow button for available books
        if (status.equals("Available")) {
            infoPanel.add(Box.createVerticalStrut(10));

            JButton borrowBtn = new JButton("Borrow");
            borrowBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            borrowBtn.setBackground(sage);
            borrowBtn.setForeground(Color.WHITE);
            borrowBtn.setBorderPainted(false);
            borrowBtn.setFocusPainted(false);
            borrowBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            borrowBtn.setPreferredSize(new Dimension(80, 25));
            borrowBtn.setMaximumSize(new Dimension(80, 25));

            // Hover effect for borrow button
            borrowBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    borrowBtn.setBackground(moss);
                }
                public void mouseExited(MouseEvent e) {
                    borrowBtn.setBackground(sage);
                }
            });

            borrowBtn.addActionListener(e -> {
               JOptionPane.showMessageDialog(null, "Successfully borrowed: " + title + "Book loaned");

               statusLabel.setText("Due in 7 days");
               statusLabel.setForeground(new Color(200, 100, 50));
               statusBadge.setBackground(new Color(255, 235, 215));

               borrowBtn.setVisible(false);
               card.revalidate();
               card.repaint();
            });

            infoPanel.add(borrowBtn);
        }

        card.add(infoPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(sage, 2),
                        BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(sand, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy");
        return sdf.format(new java.util.Date());
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

        card.add(coverPanel, BorderLayout.CENTER);

        //Info panel
        JPanel info = new RoundedPanel(20);
        info.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better centering
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        info.setBackground(new Color(140, 108, 95));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(140, 108, 95));
        textPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Garamond", Font.BOLD, 16)); // Slightly smaller to fit
        t.setForeground(Color.WHITE);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (title.length() > 20) {
            t.setText("<html><div style='text-align: center; width: 150px;'>" + title + "</div></html>");
        }

        JLabel a = new JLabel(author);
        a.setFont(new Font("Garamond", Font.BOLD, 14));
        a.setForeground(Color.WHITE);
        a.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (author.length() > 25) {
            a.setText("<html><div style='text-align: center; width: 150px;'>" + author + "</div></html>");
        }

        textPanel.add(t);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(a);

        info.add(textPanel, new GridBagConstraints());

        card.add(info, BorderLayout.SOUTH);

        return card;
    }


    //Admin panel components
    private JPanel createBooksManagementPanel() {

        String[] columns = {"Title","Author","First Publish Year","Edition Count"};

        DefaultTableModel model = new DefaultTableModel(columns,0);
        this.booksTable = new JTable(model);
        model.addRow(new Object[]{"Test Book", "Test Author", "2024", "1"});

        Color background = new Color(250,245,235);
        Color darkBrown = new Color(80, 60, 50);

        JPanel panel = new RoundedPanel(10);
        panel.setLayout(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(background);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,500));


        // toolbar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        toolBar.setBackground(background);

        JButton addBtn = new RoundedButton("Add Book");
        JButton editBtn = new RoundedButton("Edit Book");
        JButton deleteBtn = new RoundedButton("Delete Book");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        addBtn.setFont(btnFont);
        editBtn.setFont(btnFont);
        deleteBtn.setFont(btnFont);

        addBtn.setPreferredSize(new Dimension(120,35));
        editBtn.setPreferredSize(new Dimension(120,35));
        deleteBtn.setPreferredSize(new Dimension(120,35));

        // //API search for admin dashboard
        // searchBtn.addActionListener(e ->{
        //     if (!query.isEmpty()){
        //         searchBtn.setText("Fetch....");
        //         searchBtn.setEnabled(false);
        //         model.setRowCount(0); //will clear the current data table
                
        //         SwingWorker<JsonArray, Void> worker = new SwingWorker<>(){
        //             @Override
        //             protected JsonArray doInBackground() {
        //                 return API.searchBooks(query);
        //             }

        //             @Override
        //             protected void done(){
        //                 try {
        //                     JsonArray results = get();
        //                     for (int i = 0; i < Math.min(results.size(), 15); i++){
        //                         JsonObject book = results.get(i).getAsJsonObject();
        //                         String t = book.has("title") ? book.get("title").getAsString() : "Unknown";
        //                         String a = book.has("author_name") ? book.get("author_name").getAsJsonArray().get(0).getAsString() : "Unknown";
        //                         model.addRow(new Object[]{t , a});
        //                     }
        //                 } catch (Exception ex){
        //                     ex.printStackTrace();
        //                 }
        //             }
        //         };
        //         worker.execute();
        //     }
        // });


        toolBar.add(addBtn);
        toolBar.add(editBtn);
        toolBar.add(deleteBtn);

        //ADD BOOK LOGIC
        addBtn.addActionListener(e ->{
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField yearField = new JTextField();
            JTextField isbnField = new JTextField();

            Object[]  formElements = {
                    "Book Title: ", titleField,
                    "Author: ", authorField,
                    "Publish Year: ", yearField,
                    "ISBN: ", isbnField
            };

            int result = JOptionPane.showConfirmDialog(null, formElements, "Add new book to local Library", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION && !titleField.getText().trim().isEmpty()){
                //DefaultTableModel currentData = (DefaultTableModel) booksTable.getModel();
                booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                booksTable.setRowSelectionAllowed(true);
                booksTable.setColumnSelectionAllowed(false);

                //int newRowIndex = currentData.getRowCount();
                //currentData.setRowCount(newRowIndex + 1);

                //currentData.setValueAt(titleField.getText().trim(), newRowIndex, 0);
                //currentData.setValueAt(authorField.getText().trim(), newRowIndex, 1);
                //currentData.setValueAt(yearField.getText().trim(), newRowIndex, 2);
                //currentData.setValueAt(isbnField.getText().trim(), newRowIndex, 3);

                //booksTable.revalidate();
                //booksTable.repaint();
            }
        });
        //edit button logic
        editBtn.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a book to edit.");
                return;
            }

            String currentTitle = (String) model.getValueAt(selectedRow, 0);
            String currentAuthor = (String) model.getValueAt(selectedRow, 1);
            String currentYear = (String) model.getValueAt(selectedRow, 2);
            String currentISBN = (String) model.getValueAt(selectedRow, 3);

            JTextField titleField = new JTextField(currentTitle);
            JTextField authorField = new JTextField(currentAuthor);
            JTextField yearField = new JTextField(currentYear);
            JTextField isbnField = new JTextField(currentISBN);

            Object[] form = {
                    "Title:", titleField,
                    "Author:", authorField,
                    "Year:", yearField,
                    "ISBN:", isbnField
            };

            int result = JOptionPane.showConfirmDialog(null, form, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                model.setValueAt(titleField.getText(), selectedRow, 0);
                model.setValueAt(authorField.getText(), selectedRow, 1);
                model.setValueAt(yearField.getText(), selectedRow, 2);
                model.setValueAt(isbnField.getText(), selectedRow, 3);
            }
            });

        //delete button logic
        deleteBtn.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a book to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
            }
        });


        panel.add(toolBar, BorderLayout.NORTH);



        booksTable.setRowHeight(32);

        booksTable.setFont(new Font("Segoe UI", Font.PLAIN,14));

        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD,14));
        booksTable.getTableHeader().setBackground(new Color(140,100,85));
        booksTable.getTableHeader().setForeground(darkBrown);

        booksTable.setShowVerticalLines(false);
        booksTable.setGridColor(new Color(230,220,210));

        booksTable.setSelectionBackground(new Color(156,165,118));
        booksTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        //search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        searchPanel.setBackground(background);

        JLabel searchLabel = new JLabel("Search Books:");
        searchLabel.setFont(new Font("Georgia", Font.BOLD, 14));

        JTextField searchField = new RoundedTextField(20);
        searchField.setPreferredSize(new Dimension(200,35));

        JButton searchBtn = new RoundedButton("Search");
        searchBtn.setPreferredSize(new Dimension(100,35));

        //API search
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();

            if (!query.isEmpty()) {
                searchBtn.setText("Loading...");
                searchBtn.setEnabled(false);

                model.setRowCount(0); // clear table

                SwingWorker<JsonArray, Void> worker = new SwingWorker<>() {

                    @Override
                    protected JsonArray doInBackground() {
                        return API.searchBooks(query);
                    }

                    @Override
                    protected void done() {
                        try {
                            JsonArray results = get();

                            for (int i = 0; i < Math.min(results.size(), 15); i++) {
                                JsonObject book = results.get(i).getAsJsonObject();

                                String title = book.has("title")
                                        ? book.get("title").getAsString()
                                        : "Unknown";

                                String author = "Unknown";
                                if (book.has("author_name")) {
                                    author = book.get("author_name")
                                            .getAsJsonArray().get(0).getAsString();
                                }

                                String year = book.has("first_publish_year")
                                        ? book.get("first_publish_year").getAsString()
                                        : "N/A";

                                String edition = book.has("edition_count")
                                        ? book.get("edition_count").getAsString()
                                        : "N/A";

                                model.addRow(new Object[]{title, author, year, edition});
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            searchBtn.setText("Search");
                            searchBtn.setEnabled(true);
                        }
                    }
                };

                worker.execute();
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        JPanel centerPanel = new RoundedTable(new BorderLayout(10,10));
        centerPanel.setBackground(background);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));        centerPanel.setBackground(background);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMyAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        JPanel searchBarPanel = new JPanel(new BorderLayout(5, 5));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);

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

        //check credentials and determine role
        if (username.equals("admin") && password.equals("password")) {
            currentUser = username;
            currentUserRole = "ADMIN";
            isLoggedIn = true;

            statusLabel.setText("Status: Logged in as Administrator");
            statusLabel.setForeground(Color.GREEN);

            //switch to admin
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

            updateUserDashboard();

            //switch to user
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
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Garamond", Font.PLAIN, 15));

        loginButton.setEnabled(true);

        usernameField.setEnabled(true);
        passwordField.setEnabled(true);

        usernameField.setText("");
        passwordField.setText("");

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
