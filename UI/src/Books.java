//Books.java

/**
 * Represents a book in the software system
 *
 * Class acts as a data model where information is stored
 * about books that are retrieved from the API or stored locally.
 */
public class Books {

    // Attributes of the book
    private String title;
    private String author;
    private String publisher;
    private String publishedDate;
    private String isbn;
    private String description;

    /**
     * Constructor for Book object to be initialized
     *
     * @param title Title of Book
     * @param author Author of Book
     * @param publisher Publisher of Book
     * @param publishedDate Publication Date
     * @param isbn ISBN of Book
     * @param description Description/Summary of Book
     */
    public Books(String title, String author, String publisher,
                 String publishedDate, String isbn, String description) {

        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.description = description;
    }

    // All Getter methods for each attribute (no setters since it is an
    // immutable-style usage
    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getPublisher() { return publisher; }

    public String getPublishedDate() { return publishedDate; }

    public String getIsbn() { return isbn; }

    public String getDescription() { return description; }
}