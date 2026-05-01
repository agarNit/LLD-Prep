import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Library library = new Library();

        // --- Setup books ---
        Book harryPotter = new Book("Harry Potter", "J.K. Rowling", "978-0-123");
        library.addBook(harryPotter);
        library.addBookCopy(harryPotter.getId());
        library.addBookCopy(harryPotter.getId());

        Book cleanCode = new Book("Clean Code", "Robert C. Martin", "978-0-456");
        library.addBook(cleanCode);
        library.addBookCopy(cleanCode.getId());

        // --- Setup members ---
        Member alice = new Member("Alice", "1234567890");
        Member bob   = new Member("Bob",   "0987654321");
        library.addMember(alice);
        library.addMember(bob);

        // --- Happy path ---
        System.out.println("\n=== Happy Path ===");
        library.borrowBook(bob.getId(), cleanCode.getId());

        double fee = library.returnBook(
                harryPotter.getBookCopies().get(0).getId());
        System.out.println("Fee: $" + fee);
        // Expected → Fee: $0.0

        // --- Late return ---
        System.out.println("\n=== Late Return ===");
        String log2 = library.borrowBook(
                alice.getId(), harryPotter.getId());
        BorrowLog lateLog = library.getBorrowLog(log2);
        lateLog.simulateBorrowDate(LocalDate.now().minusDays(20));

        double lateFee = library.returnBook(
                harryPotter.getBookCopies().get(0).getId());
        System.out.println("Late fee: $" + lateFee);
        // Expected → Late fee: $16.0 ($10 fixed + $6 per day)

        // --- No copies available ---
        System.out.println("\n=== No Copies Available ===");
        try {
            library.borrowBook(alice.getId(), cleanCode.getId());
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // --- Borrow limit ---
        System.out.println("\n=== Borrow Limit ===");
        Book b1 = new Book("Book1", "Author1", "111"); library.addBook(b1);
        Book b2 = new Book("Book2", "Author2", "222"); library.addBook(b2);
        Book b3 = new Book("Book3", "Author3", "333"); library.addBook(b3);
        Book b4 = new Book("Book4", "Author4", "444"); library.addBook(b4);
        Book b5 = new Book("Book5", "Author5", "555"); library.addBook(b5);
        library.addBookCopy(b1.getId());
        library.addBookCopy(b2.getId());
        library.addBookCopy(b3.getId());
        library.addBookCopy(b4.getId());
        library.addBookCopy(b5.getId());

        library.borrowBook(alice.getId(), harryPotter.getId());
        library.borrowBook(alice.getId(), b1.getId());
        library.borrowBook(alice.getId(), b2.getId());
        library.borrowBook(alice.getId(), b3.getId());
        library.borrowBook(alice.getId(), b4.getId());

        try {
            library.borrowBook(alice.getId(), b5.getId());
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        // Expected → Alice has reached borrow limit of 5
    }
}