package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import cinema.dao.MovieDao;
import cinema.model.Movie;
import cinema.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieServiceTest {
    private MovieDao movieDaoMock;
    private MovieService movieService;
    private Movie movie;

    @BeforeEach
    void setUp() {
        movieDaoMock = Mockito.mock(MovieDao.class);
        movieService = new MovieServiceImpl(movieDaoMock);
        movie = new Movie();
        movie.setId(1L);
        movie.setDescription("Comedy");
        movie.setTitle("Smile");
    }

    @Test
    void add_Ok() {
        Mockito.when(movieDaoMock.add(movie)).thenReturn(movie);
        Movie actual = movieService.add(movie);
        assertNotNull(actual);
        assertEquals(movie, actual);
    }

    @Test
    void add_MovieIsNull_NotOk() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            movieService.add(null);
        });
        assertEquals("Movie can't be null", e.getMessage());
    }

    @Test
    void get_Ok() {
        Mockito.when(movieDaoMock.get(movie.getId())).thenReturn(Optional.of(movie));
        Movie actual = movieService.get(movie.getId());
        assertNotNull(actual);
        assertEquals(movie, actual);
    }

    @Test
    void get_InvalidId_Ok() {
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> {
            movieService.get(2L);
        });
        assertEquals("Can't get movie by id 2", e.getMessage());
    }

    @Test
    void get_IdIsNull_NotOk() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            movieService.get(null);
        });
        assertEquals("Id can't be null", e.getMessage());
    }

    @Test
    void getAll_Ok() {
        List<Movie> movies = List.of(movie);
        Mockito.when(movieDaoMock.getAll()).thenReturn(movies);
        List<Movie> actual = movieService.getAll();
        assertNotNull(actual);
        assertEquals(movies, actual);
    }
}