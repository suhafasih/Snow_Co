# Snow — Library Catalogue System

**Team:**  Zoha · Suha · Seline · Zainab · Ilakkiyan


## Overview

Snow is a desktop library catalogue application that allows users to browse, search, and borrow books. Book data is fetched in real time from the Open Library API. The application has two access levels — a user dashboard for browsing and borrowing, and an admin dashboard for managing the catalogue.


## What We Built

Over the course of the project, we added the following main features:

- **Book Detail Dialog** — clicking any book card opens a popup showing the full title, author, genre, a live summary, and an estimated price, all fetched from the Open Library API
- **Live Search with Autocomplete** — the search bar suggests results as you type using a debounced API call, displayed as a dropdown
- **Async Cover Image Loading** — book cover images load in the background, so the UI never freezes
- **Borrow & Return System** — users can borrow available books and return them, with a dedicated Currently Borrowed section that updates in real time
- **Admin Book Management** — admins can add, edit, and delete books from the catalog with full form dialogs and a right-click context menu

## Running the Application

### 1. Requirements
Make sure the following are installed on your system:
- Java JDK 17 or later
- Git
- Gson Library (`gson-2.10.1.jar` included in the `lib` folder)

### 2. How to Compile, Test, and Run

**For Windows Users**
1. Open Command Prompt or PowerShell and navigate to the root directory of the project (`Snow_Co`)
2. Execute the batch script to compile and launch:
   ```
   .\run.bat
   ```

**For Mac / Linux Users**
1. Open your terminal and navigate to the root directory of the project (`Snow_Co`)
2. Grant execution permissions to the shell script (only needed once):
   ```
   chmod +x run.sh
   ```
3. Execute the script to compile and launch:
   ```
   ./run.sh
   ```

**What these scripts do automatically:**

| Step | Action |
|------|--------|
| Clean | Deletes any outdated `.class` files in the `target/classes` directory |
| Link | Connects dependencies from `UI/lib/` (`gson.jar`, `junit.jar`) |
| Compile | Compiles all source and test files from `UI/src/` into `target/` |
| Launch | Starts the JVM and launches the `LoginWindow` application |

### 3. Run the Application
If the build is successful, the Login Window will open automatically. Log in using one of the test accounts below.


## Login Credentials

| Role | Username | Password |
|------|----------|----------|
| User | `user` | `password` |
| Admin | `admin` | `password` |


## Features

### User Dashboard
- Browse books by genre using category buttons
- Search with live autocomplete suggestions 
- Click any book card to view its title, author, summary, and estimated price
- Borrow and return books — borrowed books appear in a dedicated section
- Paginated results with a Load More button

### Admin Dashboard
- Search the Open Library database and load results into a table
- Add, edit, and delete books via form dialogs
- Right-click context menu for quick row actions
- Sortable columns


## Demo

### User Flow
1. Log in with the user account
2. Click a genre button to load books in that category, or type in the search bar
3. Click any book card to open the detail view — summary and price load automatically
4. Click **Borrow** on any available card — it moves to the Currently Borrowed section
5. Click **Return Book** to return it and restore its availability
6. Click **Load More Books** to load the next page of results

### Admin Flow
1. Log in with the admin account
2. Use the search bar to pull books from Open Library into the table
3. Use **Add Book**, **Edit Book**, or **Delete Book** to manage entries
4. Right-click any row for a shortcut menu
5. Click any column header to sort


## Project Structure

```
Snow_Co/
├── UI/
│   ├── lib/                   # gson-2.10.1.jar, junit.jar
│   ├── resources/             # libcat2.png (login background)
│   └── src/
│       ├── LoginWindow.java   # all UI — login, dashboards, dialogs
│       ├── API.java           # Open Library API calls
│       ├── RoundedButton.java
│       ├── RoundedPanel.java
│       ├── RoundedTextField.java
│       ├── RoundedPasswordField.java
│       ├── RoundedTable.java
│       ├── Books.java
│       └── User.java
├── run.bat
├── run.sh
└── README.md
```


## API

All book data comes from the [Open Library API](https://openlibrary.org/developers/api). No API key required.

| Endpoint | Used for |
|----------|----------|
| `/search.json?q=...` | Book search and autocomplete |
| `/works/[id].json` | Book summary in the detail dialog |
| `covers.openlibrary.org/b/id/[id]-L.jpg` | Cover images |

---

## Technical Notes

### Navigation

All three screens (login, user dashboard, admin dashboard) are panels inside a single `JFrame`, swapped using `CardLayout`:

```java
cardLayout.show(mainPanel, "USER_DASHBOARD");
```

### Threading

All network calls run on background threads using `SwingWorker` to keep the UI responsive. Never make API or image calls directly on the Event Dispatch Thread (EDT).

```java
new SwingWorker<JsonArray, Void>() {
    protected JsonArray doInBackground() {
        return API.searchBooks(query); // background thread
    }
    protected void done() {
        JsonArray results = get(); // back on EDT — safe to update UI
    }
}.execute();
```

This pattern is used for: search results, autocomplete suggestions, cover image loading, and the book detail dialog.

### Book Data Structure

Books in the user dashboard are stored as `List<String[]>` where each array is:

```
[0] title
[1] author
[2] coverUrl
[3] status  ("Available" or "Checked Out")
[4] category
```

Borrowed books are stored separately as `List<String[]>` with title, author, and coverUrl only.

### Autocomplete

A `javax.swing.Timer` (500ms) debounces the search field so the API is only called after the user stops typing. The suggestion dropdown uses `JPopupMenu` with `setFocusable(false)` — this is required to prevent the popup from stealing focus away from the text field.

### Admin Table

The admin table uses `DefaultTableModel` with a parallel `List<Object[]> allBooksList` for filtering. Always use `booksTable.convertRowIndexToModel(selectedRow)` when editing or deleting — sorting changes the view index but not the model index.

### Color Palette

Colors are redefined locally in each method. To clean this up, extract them as constants at the top of `LoginWindow`:

```java
static final Color CREAM      = new Color(245, 240, 230);
static final Color SAGE       = new Color(156, 165, 118);
static final Color WARM_BROWN = new Color(140, 100, 85);
static final Color DARK_BROWN = new Color(80, 60, 50);
static final Color SAND       = new Color(220, 210, 190);
static final Color MOSS       = new Color(110, 120, 90);
static final Color TERRACOTTA = new Color(190, 120, 90);
```


*This README was written with some assistance of AI for formatting. *
