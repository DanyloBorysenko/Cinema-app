package cinema.dao;

import cinema.AbstractTest;
import cinema.dao.impl.CinemaHallDaoImpl;
import cinema.dao.impl.MovieDaoImpl;
import cinema.dao.impl.MovieSessionDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieSessionDaoTest extends AbstractTest {
    private MovieSessionDao movieSessionDao;
    private MovieDao movieDao;
    private CinemaHallDao cinemaHallDao;
    private MovieSession movieSession;
    private Movie movie;
    private LocalDateTime showTime = LocalDateTime.now().plusHours(1).withNano(0);

    @BeforeEach
    void setUp() {
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        movieDao = new MovieDaoImpl(getSessionFactory());
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        movie = new Movie();
        movie.setTitle("Title");
        movie.setDescription("Description");
        movieDao.add(movie);
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(20);
        cinemaHall.setDescription("Big");
        cinemaHallDao.add(cinemaHall);
        movieSession = new MovieSession();
        movieSession.setShowTime(showTime);
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
    }

    @Test
    void add_Ok() {
        MovieSession actual = movieSessionDao.add(movieSession);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_MovieSessionIsNull_NotOk() {
        assertThrows(DataProcessingException.class,() -> {
            movieSessionDao.add(null);
        });
    }

    @Test
    void get_Ok() {
        movieSessionDao.add(movieSession);
        Optional<MovieSession> optionalMovieSession = movieSessionDao.get(1L);
        assertTrue(optionalMovieSession.isPresent());
        assertEquals(movieSession, optionalMovieSession.get());
    }

    @Test
    void get_MovieSessionIsNull_NotOk() {
        assertThrows(DataProcessingException.class,() -> {
            movieSessionDao.get(null);
        });
    }

    @Test
    void get_InvalidMovieSession_NotOk() {
        Optional<MovieSession> optionalMovieSession = movieSessionDao.get(1L);
        assertTrue(optionalMovieSession.isEmpty());
    }

    @Test
    void findAvailableSessions_Ok() {
        movieSessionDao.add(movieSession);
        List<MovieSession> actual = movieSessionDao.findAvailableSessions(1L, LocalDate.now());
        assertNotNull(actual);
        assertEquals(List.of(movieSession), actual);
    }

    @Test
    void findAvailableSessions_NoSessions_Ok() {
        List<MovieSession> actual = movieSessionDao.findAvailableSessions(1L, LocalDate.now());
        assertNotNull(actual);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findAvailableSessions_NotAvailableDate_Ok() {
        movieSessionDao.add(movieSession);
        List<MovieSession> actual = movieSessionDao.findAvailableSessions(1L, LocalDate.now().plusDays(1));
        assertNotNull(actual);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findAvailableSessions_MovieIdIsNull_Ok() {
        movieSessionDao.add(movieSession);
        List<MovieSession> actual = movieSessionDao.findAvailableSessions(null, LocalDate.now());
        assertNotNull(actual);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findAvailableSessions_DateIsNull_Ok() {
        assertThrows(DataProcessingException.class,() -> {
            movieSessionDao.findAvailableSessions(1L, null);
        });
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{MovieSession.class, Movie.class, CinemaHall.class};
    }
}