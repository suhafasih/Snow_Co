//Books.java
public class Books {

    private String title;
    private String author;
    private String publisher;
    private String publishedDate;
    private String isbn;
    private String description;

    public Books(String title, String author, String publisher,
                 String publishedDate, String isbn, String description) {

        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.description = description;
    }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getPublisher() { return publisher; }

    public String getPublishedDate() { return publishedDate; }

    public String getIsbn() { return isbn; }

    public String getDescription() { return description; }
}