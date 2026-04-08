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
    private java.util.List<String[]> borrowedBooks = new java.util.ArrayList<>(); // to refresh UI
    private JLabel countBadge;
    private java.util.List<String[]> allBooks = new java.util.ArrayList<>();
    private int currentPage = 0;
    private final int BOOKS_PER_PAGE = 8;

    // Dashboard panels
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel adminDashboard;
    private JPanel userDashboard;
    private CardLayout cardLayout;
    private JPanel borrowedGridPanel;
    private JPanel allBooksGrid;

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
//    /-------------------------------------------------------------------------- to show details + price too
    private void showBookDetailDialog(String title, String author, String coverUrl, String category) {
        Color cream     = new Color(245, 240, 230);
        Color sage      = new Color(156, 165, 118);
        Color warmBrown = new Color(140, 100, 85);
        Color darkBrown = new Color(80, 60, 50);
        Color sand      = new Color(220, 210, 190);
        Color moss      = new Color(110, 120, 90);

        JDialog dialog = new JDialog(this, "Book Details", true);
        dialog.setSize(680, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(cream);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(sage);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        JLabel headerTitle = new JLabel("Book Details");
        headerTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        headerTitle.setForeground(Color.WHITE);
        JButton closeBtn = new RoundedButton(" X Close");
        closeBtn.setBackground(warmBrown);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setPreferredSize(new Dimension(90, 32));
        closeBtn.addActionListener(e -> dialog.dispose());
        header.add(headerTitle, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);
        dialog.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setBackground(cream);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel coverPanel = new JPanel(new BorderLayout());
        coverPanel.setBackground(cream);
        coverPanel.setPreferredSize(new Dimension(165, 230));
        JLabel coverLabel = new JLabel("Loading...", SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(150, 200));
        coverLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        coverLabel.setForeground(sage);
        coverLabel.setOpaque(true);
        coverLabel.setBackground(sand);
        coverLabel.setBorder(BorderFactory.createLineBorder(sand, 2));
        coverPanel.add(coverLabel, BorderLayout.CENTER);
        if (coverUrl != null && !coverUrl.isEmpty()) {
            new SwingWorker<ImageIcon, Void>() {
                protected ImageIcon doInBackground() throws Exception {
                    BufferedImage img = ImageIO.read(new URL(coverUrl));
                    return new ImageIcon(img.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                }
                protected void done() {
                    try { coverLabel.setText(""); coverLabel.setIcon(get()); }
                    catch (Exception ex) { coverLabel.setText("No Cover"); }
                }
            }.execute();
        } else {
            coverLabel.setText("No Cover");
        }
        content.add(coverPanel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(cream);

        JLabel titleLabel = new JLabel("<html><div style='width:320px'><b>" + title + "</b></div></html>");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        titleLabel.setForeground(darkBrown);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("by " + author);
        authorLabel.setFont(new Font("Georgia", Font.ITALIC, 15));
        authorLabel.setForeground(warmBrown);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel catBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        catBadge.setBackground(cream);
        catBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        String catText = (category != null && !category.trim().isEmpty()) ? category : "General";
        JLabel catLabel = new JLabel("  " + catText + "  ");
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        catLabel.setForeground(moss);
        catLabel.setBackground(new Color(230, 240, 220));
        catLabel.setOpaque(true);
        catLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        catBadge.add(catLabel);

        JLabel summaryHeader = new JLabel("Summary");
        summaryHeader.setFont(new Font("Georgia", Font.BOLD, 14));
        summaryHeader.setForeground(darkBrown);
        summaryHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea summaryArea = new JTextArea("Fetching summary...");
        summaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        summaryArea.setForeground(darkBrown);
        summaryArea.setBackground(cream);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setEditable(false);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setBorder(BorderFactory.createLineBorder(sand, 1));
        summaryScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        summaryScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel priceHeader = new JLabel("Estimated Price");
        priceHeader.setFont(new Font("Georgia", Font.BOLD, 14));
        priceHeader.setForeground(darkBrown);
        priceHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel("Fetching price...");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(moss);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        new SwingWorker<String[], Void>() {
            protected String[] doInBackground() {
                String summary = "No summary available for this book.";
                String price   = "Not available";
                try {
                    String encoded = java.net.URLEncoder.encode(title + " " + author, "UTF-8");
                    URL searchUrl = new URL("https://openlibrary.org/search.json?q=" + encoded + "&limit=1");
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) searchUrl.openConnection();
                    conn.setRequestProperty("User-Agent", "LibraryCatalogueApp/1.0");
                    conn.setConnectTimeout(7000); conn.setReadTimeout(7000);
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JsonObject searchResult = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    JsonArray docs = searchResult.getAsJsonArray("docs");
                    if (docs != null && docs.size() > 0) {
                        JsonObject doc = docs.get(0).getAsJsonObject();
                        if (doc.has("key")) {
                            String workKey = doc.get("key").getAsString();
                            URL workUrl = new URL("https://openlibrary.org" + workKey + ".json");
                            java.net.HttpURLConnection wconn = (java.net.HttpURLConnection) workUrl.openConnection();
                            wconn.setRequestProperty("User-Agent", "LibraryCatalogueApp/1.0");
                            wconn.setConnectTimeout(7000); wconn.setReadTimeout(7000);
                            java.io.BufferedReader wreader = new java.io.BufferedReader(new java.io.InputStreamReader(wconn.getInputStream()));
                            StringBuilder wsb = new StringBuilder();
                            while ((line = wreader.readLine()) != null) wsb.append(line);
                            wreader.close();
                            JsonObject work = JsonParser.parseString(wsb.toString()).getAsJsonObject();
                            if (work.has("description")) {
                                JsonElement desc = work.get("description");
                                if (desc.isJsonObject() && desc.getAsJsonObject().has("value"))
                                    summary = desc.getAsJsonObject().get("value").getAsString();
                                else if (desc.isJsonPrimitive())
                                    summary = desc.getAsString();
                                if (summary.length() > 1000) summary = summary.substring(0, 999) + "...";
                            }
                        }
                        int editions = doc.has("edition_count") ? doc.get("edition_count").getAsInt() : 1;
                        double newPrice  = 24.99;
                        double usedPrice = Math.max(3.99, newPrice - (editions * 0.35));
                        price = String.format("Paper: $%.2f    |    Ebook: $%.2f", newPrice, usedPrice);
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
                return new String[]{summary, price};
            }
            protected void done() {
                try {
                    String[] result = get();
                    summaryArea.setText(result[0]); summaryArea.setCaretPosition(0);
                    priceLabel.setText(result[1]);
                } catch (Exception ex) {
                    summaryArea.setText("Could not load summary.");
                    priceLabel.setText("Price unavailable");
                }
            }
        }.execute();

        infoPanel.add(titleLabel);        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(authorLabel);       infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(catBadge);          infoPanel.add(Box.createVerticalStrut(18));
        infoPanel.add(summaryHeader);     infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(summaryScroll);     infoPanel.add(Box.createVerticalStrut(18));
        infoPanel.add(priceHeader);       infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(priceLabel);

        content.add(infoPanel, BorderLayout.CENTER);
        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
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
        overlay.add(loginButton);
        overlay.add(Box.createVerticalStrut(15));

        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Garamond", Font.PLAIN, 15));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        overlay.add(formPanel);
        overlay.add(Box.createVerticalStrut(20));
        overlay.add(loginButton);
        overlay.add(Box.createVerticalStrut(15));
        overlay.add(statusLabel);
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

        JPanel grid = new JPanel(new GridLayout(1,4,5,0));
        grid.setBackground(new Color(245,240,230));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));

        grid.add(createAdminCard("Manage Books",sage,darkBrown,sand));
        grid.add(createAdminCard("Manage Reviews",sage,darkBrown,sand));

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

        //horizontal wraper
        JPanel searchContainer = new JPanel(new BorderLayout(10, 0)); // 10px gap between bar and button
        searchContainer.setBackground(cream);
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        //search Field, center
        JTextField searchField = new RoundedTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.putClientProperty("JTextField.placeholderText", "Start typing to see suggestions...");

        //search Button, east
        JButton searchButton = new RoundedButton("Search ");
        searchButton.setBackground(warmBrown);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setPreferredSize(new Dimension(120, 45));

        //add to wraper
        searchContainer.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchButton, BorderLayout.EAST);

        //searching suggestion as dropdown menu
        JPopupMenu suggestionMenu = new JPopupMenu();
        suggestionMenu.setFocusable(false); //prevents the menu from stealing typing cursor

        javax.swing.Timer typingTimer = new javax.swing.Timer(500, e -> {
            String query = searchField.getText().trim();
            if (query.length() >= 3) {

                SwingWorker<JsonArray, Void> suggestionWorker = new SwingWorker<>() {
                    @Override
                    protected JsonArray doInBackground() {
                        // Using the general search so it catches authors, titles, and genres
                        return API.getAutocompleteSuggestions(query);
                    }

                    @Override
                    protected void done() {
                        try {
                            JsonArray results = get();
                            suggestionMenu.removeAll();

                            if (results.size() > 0) {
                                for (int i = 0; i < results.size(); i++) {
                                    JsonObject book = results.get(i).getAsJsonObject();

                                    //get Title
                                    String title = book.has("title") ? book.get("title").getAsString() : "";

                                    //get Author
                                    String author = "";
                                    if (book.has("author_name")) {
                                        author = book.get("author_name").getAsJsonArray().get(0).getAsString();
                                    }

                                    //get Genre (Subject)
                                    String genre = "";
                                    if (book.has("subject")) {
                                        genre = book.getAsJsonArray("subject").get(0).getAsString();
                                    }

                                    if (!title.isEmpty()) {
                                        //descriptive string for the user to understands the match
                                        String displayText = title;
                                        if (!author.isEmpty()) displayText += " by " + author;
                                        if (!genre.isEmpty()) displayText += " [" + genre + "]";

                                        //limt display
                                        if (displayText.length() > 65) {
                                            displayText = displayText.substring(0, 62) + "...";
                                        }

                                        JMenuItem item = new JMenuItem(displayText);
                                        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                                        item.setBackground(Color.WHITE);
                                        item.setForeground(new Color(80, 60, 50));

                                        item.addActionListener(ev -> {
                                            searchField.setText(title);
                                            suggestionMenu.setVisible(false);
                                            searchButton.doClick();
                                        });
                                        suggestionMenu.add(item);
                                    }
                                }
                                suggestionMenu.show(searchField, 0, searchField.getHeight());
                                searchField.requestFocus();
                            } else {
                                suggestionMenu.setVisible(false);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                suggestionWorker.execute();
            } else {
                suggestionMenu.setVisible(false);
            }
        });
        typingTimer.setRepeats(false);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { typingTimer.restart(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { typingTimer.restart(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { typingTimer.restart(); }
        });


        JLabel categoriesLabel = new JLabel("Browse Categories");
        categoriesLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        categoriesLabel.setForeground(darkBrown);
        categoriesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel categoryPills = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        categoryPills.setBackground(cream);
        categoryPills.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] categories = {
                "All", "Fiction", "Mystery", "Sci-Fi", "Romance",
                "Fantasy", "Biography", "History", "Non-Fiction"
        };

        allBooksGrid = createEarthyBooksGrid(sage, warmBrown, darkBrown, sand, moss, terracotta, false);
        allBooksGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String cat : categories) {
            JButton pill = new RoundedButton(cat);
            pill.setFont(new Font("Segoe UI", Font.BOLD, 13));
            pill.setForeground(Color.WHITE);
            pill.setBackground(new Color(235, 225, 210)); // soft sand
            pill.setFocusPainted(false);
            pill.setCursor(new Cursor(Cursor.HAND_CURSOR));

            //rounded + padded look
            pill.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(sage, 1),
                    BorderFactory.createEmptyBorder(10, 18, 10, 18)
            ));

            //smooth hover effect
            pill.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    pill.setBackground(sage);
                    pill.setForeground(Color.WHITE);
                    pill.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(moss, 2),
                            BorderFactory.createEmptyBorder(9, 17, 9, 17)
                    ));
                }

                public void mouseExited(MouseEvent e) {
                    pill.setBackground(new Color(235, 225, 210));
                    pill.setForeground(Color.WHITE);
                    pill.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(sage, 1),
                            BorderFactory.createEmptyBorder(10, 18, 10, 18)
                    ));
                }
            });

            //click effect (selected state)
            pill.addActionListener(e -> {
                for (Component c : categoryPills.getComponents()) {
                    if (c instanceof JButton btn) {
                        btn.setBackground(new Color(235, 225, 210));
                        btn.setForeground(Color.WHITE);
                    }
                }

                pill.setBackground(moss);
                pill.setForeground(Color.WHITE);
            });

            pill.addActionListener(e -> {

                String query;

                if (cat.equals("All")) {
                    query = "popular books";
                } else {
                    query = cat;
                }
                //put query into search field (optional but nice)
                searchField.setText(query);

                //trigger existing search logic
                searchButton.doClick();
            });
            categoryPills.add(pill);
        }

        //browse categories
        content.add(categoriesLabel);
        content.add(Box.createVerticalStrut(15));
        //genre pills
        content.add(categoryPills);
        content.add(Box.createVerticalStrut(20));
        //search container for both search bar and button
        content.add(searchContainer);
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
        countBadge = new JLabel("0 books");
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

        borrowedGridPanel = createBorrowedBooksGrid();
        borrowedGridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        borrowedGridPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, borrowedGridPanel.getPreferredSize().height));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(cream);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(borrowedGridPanel, BorderLayout.CENTER);
        content.add(Box.createVerticalStrut(15));
        content.add(wrapper);


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


        content.add(allBooksGrid);

        // Load More button
        JButton loadMoreBtn = new RoundedButton("Load More Books");
        loadMoreBtn.setBackground(sand);
        loadMoreBtn.setForeground(Color.WHITE);
        loadMoreBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadMoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadMoreBtn.setMaximumSize(new Dimension(200, 40));
        loadMoreBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loadMoreBtn.addActionListener(e -> {
            currentPage++;

            int maxPage = (int) Math.ceil((double) allBooks.size() / BOOKS_PER_PAGE);

            if (currentPage < maxPage) {
                renderBooks(allBooksGrid, sage, warmBrown, darkBrown, sand, moss, terracotta);
            }

            if (currentPage >= maxPage - 1) {
                loadMoreBtn.setEnabled(false);
                loadMoreBtn.setText("No More Books");
            }
        });

        loadMoreBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loadMoreBtn.setBackground(sage);
                loadMoreBtn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                loadMoreBtn.setBackground(sand);
                loadMoreBtn.setForeground(Color.WHITE);
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
                            JsonArray results = get();
                            allBooks.clear(); // clear old results
                            currentPage = 0;
                            loadMoreBtn.setEnabled(true);
                            loadMoreBtn.setText("Load More Books");
                            allBooksGrid.removeAll(); // clear UI FIRST

                            for (int i = 0; i < Math.min(results.size(), 20); i++) {
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

                                String category = "Unknown";
                                if (book.has("subject")) {
                                    JsonArray subjects = book.getAsJsonArray("subject");
                                    if (subjects.size() > 0) {
                                        category = subjects.get(0).getAsString();
                                    }
                                }

                                allBooks.add(new String[]{bookTitle, bookAuthor, coverUrl, "Available", category});
                            }

                            //render AFTER loop
                            renderBooks(allBooksGrid, sage, warmBrown, darkBrown, sand, moss, terracotta);

                            allBooksGrid.revalidate();
                            allBooksGrid.repaint();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
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

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBackground(sand);

        userDashboard.add(scrollPane, BorderLayout.CENTER);
        updateBorrowedCount();
    }
    private void updateBorrowedCount() {
        int count = borrowedBooks.size();

        if (count == 1) {
            countBadge.setText("1 book");
        } else {
            countBadge.setText(count + " books");
        }
    }

    private JPanel createBorrowedBooksGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBackground(new Color(245, 240, 230));

        for (String[] book : borrowedBooks) {
            grid.add(createEarthyBookCard(
                    book[0], // title
                    book[1], // author
                    book[2], // cover
                    "Due in 7 days",
                    "normal", "",
                    new Color(156,165,118),
                    new Color(140,100,85),
                    new Color(80,60,50),
                    new Color(220,210,190),
                    new Color(110,120,90),
                    new Color(190,120,90)
            ));
        }

        return grid;
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
                    {"The Silent Patient", "Alex Michaelides", "https://covers.openlibrary.org/b/isbn/9781250301697-L.jpg", "Available", "", "Mystery"},
                    {"Project Hail Mary", "Andy Weir", "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg", "Available", "", "Sci-Fi"},
                    {"Educated", "Tara Westover", "https://covers.openlibrary.org/b/isbn/9780399590504-L.jpg", "Available", "", "Biography"},
                    {"The Song of Achilles", "Madeline Miller", "https://covers.openlibrary.org/b/isbn/9780062060624-L.jpg", "Available", "", "Fantasy"},
                    {"Dune", "Frank Herbert", "https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg", "Available", "", "Sci-Fi"},
                    {"Becoming", "Michelle Obama", "https://covers.openlibrary.org/b/isbn/9781524763138-L.jpg", "Available", "", "Biography"},
                    {"Where the Crawdads Sing", "Delia Owens", "https://covers.openlibrary.org/b/isbn/9780735219090-L.jpg", "Available", "", "Romance"},
                    {"Atomic Habits", "James Clear", "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg", "Available", "", "Non-Fiction"}
            };
        }

        for (String[] book : books) {
            grid.add(createEarthyBookCard(book[0], book[1], book[2], book[3], book.length > 4 ? book[4] : "", book.length > 5 ? book[5] : "",
                    sage, warmBrown, darkBrown, sand, moss, terracotta));
        }

        return grid;
    }

    private JPanel createEarthyBookCard(String title, String author, String coverUrl, String status, String urgency, String category,
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

        // 1. Instantly show a placeholder so the UI doesn't freeze
        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setPreferredSize(new Dimension(140, 180));
        placeholder.setBackground(sand);

        JLabel coverLabel = new JLabel("Loading...", SwingConstants.CENTER);
        coverLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        coverLabel.setForeground(sage);
        placeholder.add(coverLabel, BorderLayout.CENTER);

        coverPanel.add(placeholder);

        // 2. Fetch the actual image in the background
        if (coverUrl != null && !coverUrl.isEmpty()) {
            SwingWorker<ImageIcon, Void> imageWorker = new SwingWorker<>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    // This happens in the background, freeing up your UI!
                    BufferedImage img = ImageIO.read(new URL(coverUrl));
                    Image scaled = img.getScaledInstance(140, 180, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }

                @Override
                protected void done() {
                    try {
                        ImageIcon icon = get();
                        // Once downloaded, swap the text for the image
                        coverLabel.setText("");
                        coverLabel.setIcon(icon);
                    } catch (Exception e) {
                        coverLabel.setText("No Cover");
                        coverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    }
                }
            };
            imageWorker.execute();
        } else {
            coverLabel.setText("No Cover");
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
                JOptionPane.showMessageDialog(null, "Successfully borrowed: " + title);

                // Add to borrowed list
                borrowedBooks.add(new String[]{title, author, coverUrl});

                //update book status
                for (String[] b : allBooks) {
                    if (b[0].equals(title)) {
                        b[3] = "Checked Out";
                        break;
                    }
                }

                // Update UI
                refreshBorrowedBooks();
                updateBorrowedCount();

                statusLabel.setText("Checked Out");
                statusLabel.setForeground(terracotta);
                statusBadge.setBackground(new Color(240, 225, 215));

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
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(sand, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent e) {
                showBookDetailDialog(title, author, coverUrl, category);
            }
        });
        //return button logic
        if (status.contains("Due")) {
            infoPanel.add(Box.createVerticalStrut(10));

            JButton returnBtn = new JButton("Return Book");
            returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            returnBtn.setBackground(terracotta);
            returnBtn.setForeground(Color.WHITE);
            returnBtn.setBorderPainted(false);
            returnBtn.setFocusPainted(false);
            returnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            returnBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Returned: " + title);

                // Remove from borrowed
                borrowedBooks.removeIf(b ->
                        b[0].equals(title) && b[1].equals(author)
                );

                // Update status
                for (String[] b : allBooks) {
                    if (b[0].equals(title) && b[1].equals(author)) {
                        b[3] = "Available";
                        break;
                    }
                }

                refreshBorrowedBooks();
                updateBorrowedCount();

                SwingUtilities.invokeLater(() -> {
                    refreshAllBooksGrid();
                });
            });

            infoPanel.add(returnBtn);
        }

        return card;
    }
    private void refreshAllBooksGrid() {
        allBooksGrid.removeAll();


        if (allBooks.isEmpty()) {
            allBooksGrid.add(new JLabel("No books available"));
        } else {
            renderBooks(
                    allBooksGrid,
                    new Color(156,165,118),
                    new Color(140,100,85),
                    new Color(80,60,50),
                    new Color(220,210,190),
                    new Color(110,120,90),
                    new Color(190,120,90)
            );
        }

        allBooksGrid.revalidate();
        allBooksGrid.repaint();

        allBooksGrid.getParent().revalidate();
        allBooksGrid.getParent().repaint();
    }

    private void loadAllBooksFromAPI() {
        // Show loading indicator
        JOptionPane.showMessageDialog(this,
                "Loading books from library database...",
                "Loading",
                JOptionPane.INFORMATION_MESSAGE);

        SwingWorker<JsonArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JsonArray doInBackground() {
                return API.searchBooks("popular fiction"); // Search for popular books
            }

            @Override
            protected void done() {
                try {
                    JsonArray results = get();

                    // Get the table model
                    DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
                    model.setRowCount(0); // Clear existing rows

                    if (results != null && results.size() > 0) {
                        java.util.Random rand = new java.util.Random();
                        String[] formats = {"Paper", "Hardcover", "E-Book", "Audiobook"};
                        for (int i = 0; i < Math.min(results.size(), 50); i++) {
                            JsonObject book = results.get(i).getAsJsonObject();

                            String title = book.has("title")
                                    ? book.get("title").getAsString()
                                    : "Unknown Title";

                            String author = "Unknown Author";
                            if (book.has("author_name")) {
                                JsonArray authors = book.get("author_name").getAsJsonArray();
                                if (authors.size() > 0) {
                                    author = authors.get(0).getAsString();
                                }
                            }

                            String year = book.has("first_publish_year")
                                    ? String.valueOf(book.get("first_publish_year").getAsInt())
                                    : "N/A";

                            String editionCount = book.has("edition_count")
                                    ? String.valueOf(book.get("edition_count").getAsInt())
                                    : "N/A";

                            String format = formats[rand.nextInt(formats.length)];
                            String price = String.format("%.2f", 12.99 + (rand.nextDouble() * 15));
                            String category = "Fiction";
                            if (book.has("subject")){
                                category = book.getAsJsonArray("subject").get(0).getAsString();
                            }

                            // Add row to table
                            model.addRow(new Object[]{title, author, year, editionCount, format, price, category});
                        }

                        JOptionPane.showMessageDialog(null,
                                "Successfully loaded " + model.getRowCount() + " books!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "No books found. Please check your internet connection.",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error loading books: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void refreshBorrowedBooks() {
        borrowedGridPanel.removeAll();

        for (String[] book : borrowedBooks) {
            borrowedGridPanel.add(createEarthyBookCard(
                    book[0],
                    book[1],
                    book[2],
                    "Due in 7 days",
                    "normal","",
                    new Color(156,165,118),
                    new Color(140,100,85),
                    new Color(80,60,50),
                    new Color(220,210,190),
                    new Color(110,120,90),
                    new Color(190,120,90)
            ));
        }

        borrowedGridPanel.revalidate();
        borrowedGridPanel.repaint();
    }
    private void renderBooks(JPanel allBooksGrid,
                             Color sage, Color warmBrown, Color darkBrown,
                             Color sand, Color moss, Color terracotta) {

        int start = currentPage * BOOKS_PER_PAGE;
        int end = Math.min(start + BOOKS_PER_PAGE, allBooks.size());

        for (int i = start; i < end; i++) {
            String[] b = allBooks.get(i);

            allBooksGrid.add(createEarthyBookCard(
                    b[0],
                    b[1],
                    b[2],
                    b[3],
                    "", b.length > 4 ? b[4] : "",
                    sage, warmBrown, darkBrown, sand, moss, terracotta
            ));
        }

        allBooksGrid.revalidate();
        allBooksGrid.repaint();
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

//Centre panel for cover image
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


    //Admin panel Components
    private JPanel createBooksManagementPanel() {
        String[] columns = {"Title", "Author", "First Publish Year", "Edition Count", "Format", "Price", "Category"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        this.booksTable = new JTable(model);

        // Store all books for filtering
        java.util.List<Object[]> allBooksList = new java.util.ArrayList<>();

        Color background = new Color(250,245,235);
        Color darkBrown = new Color(80, 60, 50);

        JPanel panel = new RoundedPanel(10);
        panel.setLayout(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(background);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,500));

        // Create enhanced search panel with filters
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(background);

        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        JTextField searchField = new RoundedTextField(15);
        searchField.setPreferredSize(new Dimension(150, 35));

        // Category filter dropdown
//        JLabel categoryLabel = new JLabel("Category:");
//        categoryLabel.setFont(new Font("Georgia", Font.BOLD, 14));
//        JComboBox<String> categoryFilter = new JComboBox<>();
//        categoryFilter.addItem("All Categories");
//        categoryFilter.addItem("Fiction");
//        categoryFilter.addItem("Mystery");
//        categoryFilter.addItem("Sci-Fi");
//        categoryFilter.addItem("Romance");
//        categoryFilter.addItem("Fantasy");
//        categoryFilter.addItem("Biography");
//        categoryFilter.addItem("History");
//        categoryFilter.addItem("Non-Fiction");
//        categoryFilter.addItem("Classic");
//        categoryFilter.addItem("Thriller");
//        categoryFilter.setPreferredSize(new Dimension(140, 35));
//        categoryFilter.setBackground(Color.WHITE);


        // Buttons
        JButton searchBtn = new RoundedButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 35));
        JButton refreshBtn = new RoundedButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        JButton clearFiltersBtn = new RoundedButton("Clear Filters");
        clearFiltersBtn.setPreferredSize(new Dimension(120, 35));

        // Add components to search panel
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
//        searchPanel.add(categoryLabel);
//        searchPanel.add(categoryFilter);
//        searchPanel.add(refreshBtn);
//        searchPanel.add(clearFiltersBtn);

        // Filter method
        java.util.function.Consumer<String[]> applyFilters = (String[] filters) -> {
            model.setRowCount(0);
            String searchText = filters[0].toLowerCase();
            String category = filters[1];
            String fromYear = filters[2];
            String toYear = filters[3];

            for (Object[] book : allBooksList) {
                String title = (String) book[0];
                String author = (String) book[1];
                String year = (String) book[2];
                String bookCategory = (String) book[6];

                boolean matches = true;

                // Search filter
                if (!searchText.isEmpty()) {
                    if (!title.toLowerCase().contains(searchText) &&
                            !author.toLowerCase().contains(searchText)) {
                        matches = false;
                    }
                }

                // Category filter
                if (matches && !category.equals("All Categories")) {
                    if (!bookCategory.equalsIgnoreCase(category)) {
                        matches = false;
                    }
                }

                if (matches) {
                    model.addRow(book);
                }
            }
        };

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

        // Refresh button - reload from API
        refreshBtn.addActionListener(e -> {
            loadAllBooksFromAPIWithCallback((books) -> {
                allBooksList.clear();
                allBooksList.addAll(books);
                applyFilters.accept(new String[]{"", "All Categories", "", ""});
                searchField.setText("");
                //categoryFilter.setSelectedIndex(0);
            });
        });

        // Clear filters button
        clearFiltersBtn.addActionListener(e -> {
            searchField.setText("");
            //categoryFilter.setSelectedIndex(0);
            applyFilters.accept(new String[]{"", "All Categories", "", ""});
        });

        // Rest of your existing table setup...
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        toolBar.setBackground(background);

        JButton addBtn = new RoundedButton("Add Book");
        JButton editBtn = new RoundedButton("Edit Book");
        JButton deleteBtn = new RoundedButton("Delete Book");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        addBtn.setFont(btnFont);
        editBtn.setFont(btnFont);
        deleteBtn.setFont(btnFont);

        addBtn.setPreferredSize(new Dimension(110,35));
        editBtn.setPreferredSize(new Dimension(110,35));
        deleteBtn.setPreferredSize(new Dimension(110,35));

        toolBar.add(addBtn);
        toolBar.add(editBtn);
        toolBar.add(deleteBtn);


        // Enhanced Add Book with category
        addBtn.addActionListener(e -> {
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField yearField = new JTextField();
            JTextField priceField = new JTextField("12.99");

            JComboBox<String> formatCombo = new JComboBox<>();
            formatCombo.addItem("Paper");
            formatCombo.addItem("Hardcover");
            formatCombo.addItem("E-Book");
            formatCombo.addItem("Audiobook");

            JComboBox<String> categoryCombo = new JComboBox<>();
            categoryCombo.addItem("Fiction");
            categoryCombo.addItem("Non-Fiction");
            categoryCombo.addItem("Mystery");
            categoryCombo.addItem("Sci-Fi");
            categoryCombo.addItem("Romance");
            categoryCombo.addItem("Fantasy");
            categoryCombo.addItem("Biography");
            categoryCombo.addItem("History");

            formatCombo.addActionListener(event ->{
                String selected = (String) formatCombo.getSelectedItem();
                if (selected.equals("Hardcover")) priceField.setText("24.99");
                else if (selected.equals("Paper")) priceField.setText("12.99");
                else if (selected.equals("E-Book")) priceField.setText("9.99");
                else if (selected.equals("Audiobook")) priceField.setText("19.99");
            });

            Object[] formElements = {
                    "Book Title:", titleField,
                    "Author:", authorField,
                    "Publish Year:", yearField,
                    "Format: ", formatCombo,
                    "Price ($): ", priceField,
                    "Category:", categoryCombo
            };

            int result = JOptionPane.showConfirmDialog(null, formElements,
                    "Add New Book", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION && !titleField.getText().trim().isEmpty()) {
                Object[] newBook = {
                        titleField.getText().trim(),
                        authorField.getText().trim(),
                        yearField.getText().trim(),
                        formatCombo.getSelectedItem(),
                        priceField.getText().trim(),
//                        "N/A",
                        (String) categoryCombo.getSelectedItem()
                };
                allBooksList.add(newBook);
                model.addRow(newBook);
            }
        });

        // Edit button with category
        // Edit button with category - FIXED VERSION
        editBtn.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a book to edit.");
                return;
            }

            int modelRow = booksTable.convertRowIndexToModel(selectedRow);

            // Check if the model row is valid
            if (modelRow < 0 || modelRow >= model.getRowCount()) {
                JOptionPane.showMessageDialog(null, "Invalid row selection.");
                return;
            }

            String currentTitle = (String) model.getValueAt(modelRow, 0);
            String currentAuthor = (String) model.getValueAt(modelRow, 1);
            String currentYear = (String) model.getValueAt(modelRow, 2);
            String currentEditions = (String) model.getValueAt(modelRow, 3);
            String currentFormat = (String) model.getValueAt(modelRow, 4);
            String currentPrice = (String) model.getValueAt(modelRow, 5);
            String currentCategory = (String) model.getValueAt(modelRow, 6); // Index 4 is category

            JTextField titleField = new JTextField(currentTitle);
            JTextField authorField = new JTextField(currentAuthor);
            JTextField yearField = new JTextField(currentYear);
            JTextField priceField = new JTextField(currentPrice);

            JComboBox<String> formatCombo = new JComboBox<>();
            formatCombo.addItem("Paper");
            formatCombo.addItem("Hardcover");
            formatCombo.addItem("E-Book");
            formatCombo.addItem("Audiobook");

            JComboBox<String> categoryCombo = new JComboBox<>();
            categoryCombo.addItem("Fiction");
            categoryCombo.addItem("Non-Fiction");
            categoryCombo.addItem("Mystery");
            categoryCombo.addItem("Sci-Fi");
            categoryCombo.addItem("Romance");
            categoryCombo.addItem("Fantasy");
            categoryCombo.addItem("Biography");
            categoryCombo.addItem("History");
            categoryCombo.addItem("Thriller");
            categoryCombo.addItem("Classic");
            categoryCombo.setSelectedItem(currentCategory);

            Object[] form = {
                    "Title:", titleField,
                    "Author:", authorField,
                    "Year:", yearField,
                    "Format: ", formatCombo,
                    "Price ($): ", priceField,
                    "Category:", categoryCombo
            };

            int result = JOptionPane.showConfirmDialog(null, form, "Edit Book",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                // Update the model (not the view directly)
                model.setValueAt(titleField.getText(), modelRow, 0);
                model.setValueAt(authorField.getText(), modelRow, 1);
                model.setValueAt(yearField.getText(), modelRow, 2);

                model.setValueAt(formatCombo.getSelectedItem(), modelRow, 4);
                model.setValueAt(priceField.getText(), modelRow, 5);

                model.setValueAt((String) categoryCombo.getSelectedItem(), modelRow, 6);

                // Update the stored list if it exists and matches
                if (modelRow < allBooksList.size()) {
                    allBooksList.set(modelRow, new Object[]{
                            titleField.getText(),
                            authorField.getText(),
                            yearField.getText(),
                            currentEditions,
                            formatCombo.getSelectedItem(),
                            priceField.getText(),
                            model.getValueAt(modelRow, 3), // Keep existing edition count
                            categoryCombo.getSelectedItem()
                    });
                }

                JOptionPane.showMessageDialog(panel, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

        // Table customization
        booksTable.setRowHeight(32);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        booksTable.getTableHeader().setBackground(new Color(140,100,85));
        booksTable.getTableHeader().setForeground(darkBrown);
        booksTable.setShowVerticalLines(false);
        booksTable.setGridColor(new Color(230,220,210));
        booksTable.setSelectionBackground(new Color(156,165,118));
        booksTable.setSelectionForeground(Color.WHITE);

        // Add right-click context menu for quick actions
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem copyTitleItem = new JMenuItem("Copy Title");

        deleteItem.addActionListener(e -> deleteBtn.doClick());
        editItem.addActionListener(e -> editBtn.doClick());
        copyTitleItem.addActionListener(e -> {
            int row = booksTable.getSelectedRow();
            if (row != -1) {
                String title = (String) model.getValueAt(row, 0);
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new java.awt.datatransfer.StringSelection(title), null);
                JOptionPane.showMessageDialog(panel, "Title copied to clipboard!");
            }
        });

        contextMenu.add(editItem);
        contextMenu.add(deleteItem);
        contextMenu.addSeparator();
        contextMenu.add(copyTitleItem);

        booksTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = booksTable.rowAtPoint(e.getPoint());
                    booksTable.setRowSelectionInterval(row, row);
                    contextMenu.show(booksTable, e.getX(), e.getY());
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = booksTable.rowAtPoint(e.getPoint());
                    booksTable.setRowSelectionInterval(row, row);
                    contextMenu.show(booksTable, e.getX(), e.getY());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        JPanel centerPanel = new RoundedPanel(10);
        centerPanel.setLayout(new BorderLayout(10,10));
        centerPanel.setBackground(background);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void loadAllBooksFromAPIWithCallback(java.util.function.Consumer<java.util.List<Object[]>> callback) {
        SwingWorker<JsonArray, Void> worker = new SwingWorker<>() {
            @Override
            protected JsonArray doInBackground() {
                return API.searchBooks("popular fiction");
            }

            @Override
            protected void done() {
                try {
                    JsonArray results = get();
                    java.util.List<Object[]> booksList = new java.util.ArrayList<>();


                    if (results != null && results.size() > 0) {
                        java.util.Random rand = new java.util.Random();
                        String[] formats = {"Paper", "Hardcover", "E-Book", "Audiobook"};

                        for (int i = 0; i < Math.min(results.size(), 50); i++) {
                            JsonObject book = results.get(i).getAsJsonObject();

                            String title = book.has("title") ? book.get("title").getAsString() : "Unknown Title";

                            String author = "Unknown Author";
                            if (book.has("author_name")) {
                                JsonArray authors = book.get("author_name").getAsJsonArray();
                                if (authors.size() > 0) author = authors.get(0).getAsString();
                            }

                            String year = book.has("first_publish_year") ?
                                    String.valueOf(book.get("first_publish_year").getAsInt()) : "N/A";

                            String editionCount = book.has("edition_count") ?
                                    String.valueOf(book.get("edition_count").getAsInt()) : "N/A";

                            String format = formats[rand.nextInt(formats.length)];
                            String price = String.format("%.2f", 9.99 + (rand.nextDouble() * 20.0));

                            // Guess category from subjects if available
                            String category = "General";
                            if (book.has("subject")) {
                                JsonArray subjects = book.getAsJsonArray("subject");
                                if (subjects.size() > 0) {
                                    String subject = subjects.get(0).getAsString().toLowerCase();
                                    if (subject.contains("fiction")) category = "Fiction";
                                    else if (subject.contains("mystery")) category = "Mystery";
                                    else if (subject.contains("sci-fi") || subject.contains("science fiction")) category = "Sci-Fi";
                                    else if (subject.contains("romance")) category = "Romance";
                                    else if (subject.contains("fantasy")) category = "Fantasy";
                                    else if (subject.contains("biography")) category = "Biography";
                                    else if (subject.contains("history")) category = "History";
                                    else if (subject.contains("non-fiction")) category = "Non-Fiction";
                                }
                            }

                            booksList.add(new Object[]{title, author, year, editionCount, format, price, category});
                        }
                    }
                    callback.accept(booksList);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.accept(new java.util.ArrayList<>());
                }
            }
        };
        worker.execute();
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

            SwingUtilities.invokeLater(() -> {
                loadAllBooksFromAPI();
            });

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
        statusLabel.setText("");
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