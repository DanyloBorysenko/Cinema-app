package cinema.dao;

import cinema.AbstractTest;
import cinema.dao.impl.MovieDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieDaoTest extends AbstractTest {
    private MovieDao movieDao;
    private Movie movie;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Movie.class};
    }

    @BeforeEach
    void setUp() {
        movieDao = new MovieDaoImpl(getSessionFactory());
        movie = new Movie();
        movie.setTitle("Title");
        movie.setDescription("Description");
    }

    @Test
    void add_Ok() {
        Movie actual = movieDao.add(movie);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_MovieIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> {
            movieDao.add(null);
        });
    }

    @Test
    void get_OK() {
        movieDao.add(movie);
        Optional<Movie> optionalMovie = movieDao.get(1L);
        assertTrue(optionalMovie.isPresent());
        assertEquals(movie, optionalMovie.get());
    }

    @Test
    void get_MovieIsNull_NotOK() {
        assertThrows(DataProcessingException.class, () -> {
            movieDao.get(null);
        });
    }

    @Test
    void get_InvalidId_OK() {
        Optional<Movie> optionalMovie = movieDao.get(1L);
        assertTrue(optionalMovie.isEmpty());
    }

    @Test
    void getAll_Ok() {
        movieDao.add(movie);
        List<Movie> actual = movieDao.getAll();
        assertNotNull(actual);
        assertEquals(List.of(movie), actual);
    }
}