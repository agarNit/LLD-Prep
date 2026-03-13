import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Setup sample data
        Movie inception = new Movie("M1", "Inception");
        Movie interstellar = new Movie("M2", "Interstellar");

        Theatre theatre = new Theatre("T1", "Downtown Cinema", new ArrayList<>());

        Showtime showtime1 = new Showtime(
            "S1",
            theatre,
            inception,
            LocalDateTime.now().plusHours(2),
            "Screen 1"
        );

        Showtime showtime2 = new Showtime(
            "S2",
            theatre,
            interstellar,
            LocalDateTime.now().plusHours(3),
            "Screen 2"
        );

        List<Showtime> theatreShowtimes = new ArrayList<>();
        theatreShowtimes.add(showtime1);
        theatreShowtimes.add(showtime2);

        // re-create theatre with showtimes
        theatre = new Theatre("T1", "Downtown Cinema", theatreShowtimes);

        List<Theatre> theatres = new ArrayList<>();
        theatres.add(theatre);

        BookingSystem bookingSystem = new BookingSystem(theatres);

        System.out.println("==== Test 1: Successful booking ====");
        Reservation res1 = bookingSystem.book("S1", Arrays.asList("A1", "A2"));
        System.out.println("Booked reservation id: " + res1.getConfirmationId());
        System.out.println("Seats: " + res1.getSeatIds());

        System.out.println("\n==== Test 2: Seat not available ====");
        try {
            bookingSystem.book("S1", Arrays.asList("A2"));
            System.out.println("ERROR: booking should have failed for already booked seat");
        } catch (IllegalStateException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        System.out.println("\n==== Test 3: Cancel reservation and rebook ====");
        bookingSystem.cancelReservation(res1.getConfirmationId());
        Reservation res2 = bookingSystem.book("S1", Arrays.asList("A1", "A2"));
        System.out.println("Booked reservation after cancel id: " + res2.getConfirmationId());

        System.out.println("\n==== Test 4: Invalid showtime id ====");
        try {
            bookingSystem.book("INVALID", Arrays.asList("A1"));
            System.out.println("ERROR: booking should have failed for invalid showtime");
        } catch (Exception e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        System.out.println("\n==== Test 5: Search movies (showtimes) ====");
        List<Showtime> foundShowtimes = bookingSystem.searchMovies("in");
        for (Showtime st : foundShowtimes) {
            System.out.println("Found showtime: " + st.getId()
                    + " for movie " + st.getMovie().getTitle()
                    + " at " + st.getDatetime());
        }

        System.out.println("\n==== Test 6: Invalid seat id ====");
        try {
            bookingSystem.book("S1", Arrays.asList("Z99"));
            System.out.println("ERROR: booking should have failed for invalid seat id");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        System.out.println("\n==== Test 7: Empty seat list ====");
        try {
            bookingSystem.book("S1", new ArrayList<>());
            System.out.println("ERROR: booking should have failed for empty seat list");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }
    }
}