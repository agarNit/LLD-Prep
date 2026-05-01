import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class BorrowLog {
    private String id;
    private String memberId;
    private String bookCopyId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double lateFee;

    public BorrowLog(String memberId, String bookCopyId, LocalDate borrowDate) {
        this.id = UUID.randomUUID().toString();
        this.memberId = memberId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(14);
        this.returnDate = null;
        this.lateFee = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBookCopyId() {
        return bookCopyId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public void returnBook(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.lateFee = calculateLateFee();
    }

    private double calculateLateFee() {
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return 10.0 + (daysLate * 1.0);
        }
        return 0.0;
    }

    public void simulateBorrowDate(LocalDate pastDate) {
        this.borrowDate = pastDate;
        this.dueDate = pastDate.plusDays(14);
    }
}