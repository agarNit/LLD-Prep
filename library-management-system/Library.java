import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private Map<String, BorrowLog> borrowLogs;

    public Library() {
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.borrowLogs = new HashMap<>();
    }

    // --- Book operations ---

    public void addBook(Book book) {
        books.put(book.getId(), book);
        System.out.println("Book added: " + book.getTitle());
    }

    public void addBookCopy(String bookId) {
        if (!books.containsKey(bookId)) {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
        Book book = books.get(bookId);
        BookCopy copy = new BookCopy(bookId);
        book.addCopy(copy);
        System.out.println("Copy added for book: " + book.getTitle());
    }

    // --- Member operations ---

    public void addMember(Member member) {
        members.put(member.getId(), member);
        System.out.println("Member added: " + member.getName());
    }

    // --- Borrow operations ---

    public String borrowBook(String memberId, String bookId) {
        // 1. Validate member exists
        Member member = getMember(memberId);

        // 2. Validate book exists
        Book book = getBook(bookId);

        // 3. Check member borrow limit (max 5)
        int activeBorrows = getActiveBorrowCount(memberId);
        if (activeBorrows >= 5) {
            throw new IllegalStateException(member.getName() + " has reached borrow limit of 5");
        }

        // 4. Check if book is available
        BookCopy copy = book.getAvailableCopy();
        if (copy == null) {
            throw new IllegalStateException("No available copies for: " + book.getTitle());
        }

        // 5. Mark copy as borrowed
        copy.markAsBorrowed();

        // 6. Create borrow log
        BorrowLog log = new BorrowLog(memberId, copy.getId(), LocalDate.now());
        borrowLogs.put(log.getId(), log);

        System.out.println(member.getName() + " borrowed: " + book.getTitle() + " | Due: " + log.getDueDate());
        return log.getId();
    }

    public double returnBook(String bookCopyId) {
        // 1. Find active borrow log for this copy
        BorrowLog log = getActiveBorrowLog(bookCopyId);

        // 2. Mark copy as available
        BookCopy copy = findBookCopy(bookCopyId);
        copy.markAsAvailable();

        // 3. Update borrow log with return date
        log.returnBook(LocalDate.now());

        double fee = log.getLateFee();
        System.out.println("Book returned." + (fee > 0 ? " Late fee: $" + fee : " No late fee."));
        return fee;
    }

    // --- Search operations ---

    public List<Book> searchByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public Book searchByIsbn(String isbn) {
        for (Book book : books.values()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    // --- Helper methods ---

    private int getActiveBorrowCount(String memberId) {
        int count = 0;
        for (BorrowLog log : borrowLogs.values()) {
            if (log.getMemberId().equals(memberId) && !log.isReturned()) {
                count++;
            }
        }
        return count;
    }

    private BorrowLog getActiveBorrowLog(String bookCopyId) {
        for (BorrowLog log : borrowLogs.values()) {
            if (log.getBookCopyId().equals(bookCopyId)  && !log.isReturned()) {
                return log;
            }
        }
        throw new IllegalStateException("No active borrow log found for copy: " + bookCopyId);
    }

    private BookCopy findBookCopy(String bookCopyId) {
        for (Book book : books.values()) {
            for (BookCopy copy : book.getBookCopies()) {
                if (copy.getId().equals(bookCopyId)) {
                    return copy;
                }
            }
        }
        throw new IllegalArgumentException("BookCopy not found: " + bookCopyId);
    }

    private Member getMember(String memberId) {
        if (!members.containsKey(memberId)) {
            throw new IllegalArgumentException("Member not found: " + memberId);
        }
        return members.get(memberId);
    }

    private Book getBook(String bookId) {
        if (!books.containsKey(bookId)) {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
        return books.get(bookId);
    }

    public BorrowLog getBorrowLog(String logId) {
        if (!borrowLogs.containsKey(logId)) {
            throw new IllegalArgumentException("BorrowLog not found: " + logId);
        }
        return borrowLogs.get(logId);
    }
}