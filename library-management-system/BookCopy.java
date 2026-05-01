import java.util.UUID;

public class BookCopy {
    private String id;
    private BookStatus status;
    private String bookId;

    public BookCopy(String bookId) {
        this.id = UUID.randomUUID().toString();
        this.status = BookStatus.AVAILABLE;
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }
    
    public void markAsBorrowed() {
        this.status = BookStatus.BORROWED;
    }
    
    public void markAsAvailable() {
        this.status = BookStatus.AVAILABLE;
    }
}