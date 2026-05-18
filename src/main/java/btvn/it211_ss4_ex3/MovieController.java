package btvn.it211_ss4_ex3;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private List<Movie> movieList;

    public MovieController() {
        movieList = new ArrayList<>();
        movieList.add(new Movie("M001", "Inception", "Sci-Fi", 8.8));
        movieList.add(new Movie("M002", "Parasite", "Drama", 8.6));
        movieList.add(new Movie("M003", "Interstellar", "Sci-Fi", 8.7));
    }

    /**
     * TÌNH HUỐNG B - Lọc danh sách phim theo thể loại
     * Dùng @RequestParam vì 'genre' là tham số dùng để lọc (filter).
     * Tham số này được thiết lập required = false để nếu không truyền, API sẽ trả về toàn bộ phim.
     * URI: GET /api/v1/movies?genre=Sci-Fi
     */
    @GetMapping
    public List<Movie> getMovies(@RequestParam(value = "genre", required = false) String genre) {
        // Nếu client không truyền thể loại, trả về tất cả
        if (genre == null || genre.isEmpty()) {
            return movieList;
        }

        // Lọc phim theo thể loại được truyền vào
        return movieList.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    /**
     * TÌNH HUỐNG A - Xem chi tiết phim theo ID
     * Dùng @PathVariable vì 'movieId' là định danh duy nhất của tài nguyên.
     * URI: GET /api/v1/movies/{movieId} (Ví dụ: /api/v1/movies/M001)
     */
    @GetMapping("/{movieId}")
    public Movie getMovieById(@PathVariable("movieId") String movieId) {
        return movieList.stream()
                .filter(m -> m.getMovieId().equalsIgnoreCase(movieId))
                .findFirst()
                .orElse(null); // Trong thực tế, nên ném ra một Exception (vd: ResourceNotFoundException) để trả về lỗi 404
    }

    // --- Inner class Movie ---
    static class Movie {
        private String movieId;
        private String title;
        private String genre;
        private double rating;

        public Movie(String movieId, String title, String genre, double rating) {
            this.movieId = movieId;
            this.title = title;
            this.genre = genre;
            this.rating = rating;
        }

        // Getters
        public String getMovieId() { return movieId; }
        public String getTitle() { return title; }
        public String getGenre() { return genre; }
        public double getRating() { return rating; }
    }
}
