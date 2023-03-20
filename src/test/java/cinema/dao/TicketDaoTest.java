package cinema.dao;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Set;
import cinema.AbstractTest;
import cinema.dao.impl.CinemaHallDaoImpl;
import cinema.dao.impl.MovieSessionDaoImpl;
import cinema.dao.impl.TicketDaoImpl;
import cinema.dao.impl.MovieDaoImpl;
import cinema.dao.impl.RoleDaoImpl;
import cinema.dao.impl.UserDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.Movie;
import cinema.model.CinemaHall;
import cinema.model.MovieSession;
import cinema.model.Role;
import cinema.model.Ticket;
import cinema.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketDaoTest extends AbstractTest {
    private static final LocalDateTime movieSessionTime
            = LocalDateTime.now().plusDays(1).withNano(0);
    private RoleDao roleDao;
    private CinemaHallDao cinemaHallDao;
    private UserDao userDao;
    private MovieDao movieDao;
    private MovieSessionDao movieSessionDao;
    private TicketDao ticketDao;
    private Ticket ticket;
    private User user;
    private MovieSession movieSession;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Ticket.class, MovieSession.class,
                CinemaHall.class, Movie.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        ticketDao = new TicketDaoImpl(getSessionFactory());
    }

    @Test
    void add_Ok() {
        role = addRoleToDb();
        user = addUserToDb(role);
        movieSession = addMovieSession();
        ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        Ticket actual = ticketDao.add(ticket);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_TicketIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> ticketDao.add(null));
    }

    private Role addRoleToDb() {
        Role role = new Role(USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        return roleDao.add(role);
    }

    private User addUserToDb(Role role) {
        User user = new User();
        user.setPassword("password");
        user.setEmail("email");
        user.setRoles(Set.of(role));
        userDao = new UserDaoImpl(getSessionFactory());
        return userDao.add(user);
    }

    private Movie addMovie() {
        Movie movie = new Movie();
        movie.setTitle("Title");
        movie.setDescription("description");
        movieDao = new MovieDaoImpl(getSessionFactory());
        return movieDao.add(movie);
    }

    private CinemaHall addCinemaHall() {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setDescription("description");
        cinemaHall.setCapacity(20);
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        return cinemaHallDao.add(cinemaHall);
    }

    private MovieSession addMovieSession() {
        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(addCinemaHall());
        movieSession.setMovie(addMovie());
        movieSession.setShowTime(movieSessionTime);
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        return movieSessionDao.add(movieSession);
    }
}