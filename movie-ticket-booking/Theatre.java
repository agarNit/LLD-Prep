import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Theatre {
    private final String id;
    private final String name;
    private final List<Showtime> showtimes;

    public Theatre(String id, String name, List<Showtime> showtimes) {
        this.id = id;
        this.name = name;
        this.showtimes = showtimes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public List<Showtime> getShowtimesForMovies(Movie movie) {
        List<Showtime> result = new ArrayList<>();
        for (Showtime st : showtimes) {
            if (st.getMovie().getId().equals(movie.getId())) {
                result.add(st);
            }
        }
        return result;
    }
}