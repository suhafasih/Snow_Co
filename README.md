# Snow&Co - Library Catalogue System

### **Team:**
- **Project Manager**: Zoha Murtuza
- **Technical Manager**: Ngoc Kieu Minh Do
- **Front-End Lead**: Zainab Sohail
- **Back-End Lead**: Ilakkiyan Kuganendra
- **Software Quality Lead** : Suha Fasih
- **Developers**: All

## Overview

Snow is a desktop library catalogue application that allows users to browse, search, and borrow books. Book data is fetched in real time from the Open Library API. The application has two access levels, a user dashboard for browsing and borrowing, and an admin dashboard for managing the catalogue.


## What We Built

Over the course of the project, we added the following main features:

- **Book Detail Dialog** - clicking any book card opens a popup showing the full title, author, genre, a live summary, and an estimated price, all fetched from the Open Library API
- **Live Search with Autocomplete** - the search bar suggests results as you type using a debounced API call, displayed as a dropdown
- **Async Cover Image Loading** - book cover images load in the background, so the UI never freezes
- **Borrow & Return System** - users can borrow available books and return them, with a dedicated Currently Borrowed section that updates in real time
- **Admin Book Management** - admins can add, edit, and delete books from the catalog with full form dialogs and a right-click context menu

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


## Getting Started: Login Credentials

| Role | Username | Password |
|------|----------|----------|
| User | `user` | `password` |
| Admin | `admin` | `password` |


### User Dashboard Guide

The User Dashboard is your personal library hub, designed for discovering, borrowing, and managing your reading list.

#### Finding Books
* **Smart Search:** Start typing a title, author, or genre into the main search bar. The system will automatically provide a drop-down list of matches. Click any suggestion to immediately load that book.
* **Browse Categories:** Use the browse categories above the search bar to filter the library grid instantly.
* **Sorting:** Use the **Sort by** dropdown menu on the right side of the "All Books" section to organize the grid by Popularity, Title, Author, or Newest additions.

#### Viewing Book Features & Pricing
To learn more about a book, simply click on its card in the grid. This will open the Book Details window, which displays:
* The cover art, full title, author, and category badge.
* A fetched **Summary** of the book's plot.
* An **Estimated Price** block, showing current market values for New and Used conditions (based on edition counts and formats).

#### Borrowing Books
1. Locate a book with a green **Available** status badge.
2. Click the book card to open the details, or look at the bottom of the card on the main grid.
3. Click the green **Borrow** button.
4. The book will immediately move to your **Currently Borrowed** section at the top of your dashboard, and a due date will be assigned.

#### Returning Books
1. Locate the book in your **Currently Borrowed** top section.
2. Click the terracotta-colored **Return Book** button on the book's card.
3. The book will be removed from your account and returned to the "All Books" grid for others to enjoy.

### Admin Dashboard Guide

The Admin Dashboard serves as your main point of control over the library system. It enables various 
options such as, update, delete, and organize books.

### Managing books
#### Add Book
This feature allows admin to add new books to the library.
1. Click on the add book option.
2. This will open a form where you can fill in all the book details such as Book title, Author, Publish Year, Edition Count, Format, Price and Category.
3. Click Ok to proceed.
4. The new added book will be added to the library database.

#### Edit Book
This button allows admin to edit book details.
1. Locate the book that you wish to edit by using the search function
2. Select the book from the table and click on the **Edit book** button.
3. The book details will pop up ready to be edited and updated.

#### Delete Book
This function helps the admin delete any books.
1. Locate and select the book that you wish to delete
2. Click on the **Delete Book** button.
3. Confirm the book deletion by selecting yes/no.
4. This book will successfully be removed from the library database

#### Searching books
Use the search bar to quickly locate books by Title/author/Key Word. Click on the **Search** button to get results. The table will show all books with same/similar title.

#### Viewing book data
The main table displays a few books from the library database. This table consists of:
1. Book title - name of the book
2. Author - writer of the book
3. First publication year - original book release year
4. Edition count - Number of editions of the book
5. Format - Hardcover/E-Book/Audio Book/Paper Back
6. Price - Cost of the book in $
7. Category - Genre of the book: Fiction/ Non-Fiction/ Mystery/ Sci-fi/ Romance/ Fantasy/ Biography/ History


## Features

### User Dashboard
- Browse books by genre using category buttons
- Search with live autocomplete suggestions 
- Click any book card to view its title, author, summary, and estimated price
- Borrow and return books - borrowed books appear in a dedicated section
- Paginated results with a Load More button

### Admin Dashboard
- Search the Open Library database and load results into a table
- Add, edit, and delete books via form dialogs
- Right-click context menu for quick row actions


## Demo

![Demo](Demo_1.gif)

(The blurriness is due to video conversion to a GIF, not UI design.)

Full Demo link : [FULL DEMO](https://drive.google.com/file/d/1euqspFMc4J7-WVFaEJ4zW-E5mPfjfeMV/view?usp=drive_link)

---
### User Flow
1. Log in with the user account
2. Click a genre button to load books in that category, or type in the search bar
3. Click any book card to open the detail view, summary and price load automatically
4. Click **Borrow** on any available card, it moves to the Currently Borrowed section
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
│       ├── LoginWindow.java   # all UI - login, dashboards, dialogs
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
        JsonArray results = get(); 
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

A `javax.swing.Timer` (500ms) debounces the search field so the API is only called after the user stops typing. 
The suggestion dropdown uses `JPopupMenu` with `setFocusable(false)`, which is required to prevent the popup from stealing focus from the text field.

### Admin Table

The admin table uses `DefaultTableModel` with a parallel `List<Object[]> allBooksList` for filtering. Always use `booksTable.convertRowIndexToModel(selectedRow)` when editing or deleting; sorting changes the view index but not the model index.

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

## Areas for Improvement

- **Code organization** - most of the UI logic lives in a single file (`LoginWindow.java`). Splitting it into separate classes per panel would make the codebase easier to navigate and maintain.
- **Code reusability** - several components (color definitions, card layouts, button styles) are repeated across methods and could be extracted into shared utilities or constants.
- **UI polish** - the interface is functional and stylish but could benefit from smoother animations, transitions, and a more modern visual design.

## AI Disclosure:

This project was developed by the team listed above. AI was used in a limited capacity to assist with debugging, code refinement, and understanding some concepts (e.g., threading, UI behavior, and API integration).

All the core design decisions, feature implementations, and final code were written, reviewed, and validated by the team members. 

