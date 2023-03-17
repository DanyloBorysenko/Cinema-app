package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import cinema.dao.MovieSessionDao;
import cinema.exception.AuthenticationException;
import cinema.exception.InputDataException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.service.impl.MovieSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieSessionServiceTest {
    private static final LocalDateTime SHOW_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private MovieSessionDao movieSessionDaoMock;
    private MovieSessionService movieSessionService;
    private MovieSession movieSession;
    private Movie movie;
    private CinemaHall cinemaHall;

    @BeforeEach
    void setUp() {
        movieSessionDaoMock = Mockito.mock(MovieSessionDao.class);
        movieSessionService = new MovieSessionServiceImpl(movieSessionDaoMock);
        movie = new Movie();
        movie.setTitle("2012");
        movie.setDescription("catastrophe");
        movie.setId(1L);
        cinemaHall = new CinemaHall();
        cinemaHall.setId(1L);
        cinemaHall.setCapacity(10);
        cinemaHall.setDescription("small");
        movieSession = new MovieSession();
        movieSession.setId(1L);
        movieSession.setMovie(movie);
        movieSession.setShowTime(SHOW_TIME);
        movieSession.setCinemaHall(cinemaHall);
    }

    @Test
    void findAvailableSessions_Ok() {
        Mockito.when(movieSessionDaoMock.findAvailableSessions(movie.getId(), LOCAL_DATE))
                .thenReturn(List.of(movieSession));
        List<MovieSession> actual = movieSessionService.findAvailableSessions(movie.getId(), LOCAL_DATE);
        assertNotNull(actual);
        assertEquals(List.of(movieSession), actual);
    }

    @Test
    void findAvailableSessions_MovieIdIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            movieSessionService.findAvailableSessions(null, LOCAL_DATE);
        });
    }

    @Test
    void findAvailableSessions_DateIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            movieSessionService.findAvailableSessions(movie.getId(), null);
        });
    }

    @Test
    void add_Ok() {
        Mockito.when(movieSessionDaoMock.add(movieSession)).thenReturn(movieSession);
        MovieSession actual = movieSessionService.add(movieSession);
        assertNotNull(actual);
        assertEquals(movieSession, actual);
    }

    @Test
    void add_SessionIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            movieSessionService.add(null);
        });
    }

    @Test
    void get_Ok() {
        Mockito.when(movieSessionDaoMock.get(movie.getId())).thenReturn(Optional.of(movieSession));
        MovieSession actual = movieSessionService.get(movieSession.getId());
        assertNotNull(actual);
        assertEquals(movieSession, actual);
    }

    @Test
    void get_IdIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            movieSessionService.get(null);
        });
    }

    @Test
    void update_Ok() {
        Mockito.when(movieSessionDaoMock.update(movieSession)).thenReturn(movieSession);
        MovieSession actual = movieSessionService.update(movieSession);
        assertNotNull(actual);
        assertEquals(movieSession, actual);
    }

    @Test
    void update_SessionIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            movieSessionService.update(null);
        });
    }

    @Test
    void delete_Ok() {
        movieSessionService.delete(movieSession.getId());
        Mockito.verify(movieSessionDaoMock).delete(movieSession.getId());
    }

    @Test
    void delete_IdIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            movieSessionService.delete(null);
        });
    }
}
