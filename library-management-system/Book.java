import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Book {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private List<BookCopy> bookCopies;

    public Book(String title, String author, String isbn) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.bookCopies = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    public boolean isAvailable() {
        for (BookCopy copy : bookCopies) {
            if (copy.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public BookCopy getAvailableCopy() {
        for (BookCopy copy : bookCopies) {
            if (copy.isAvailable()) {
                return copy;
            }
        }
        return null;
    }

    public void addCopy(BookCopy copy) {
        bookCopies.add(copy);
    }
}