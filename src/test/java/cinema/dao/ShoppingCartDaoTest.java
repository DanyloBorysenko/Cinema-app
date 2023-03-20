package cinema.dao;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import cinema.AbstractTest;
import cinema.dao.impl.ShoppingCartDaoImpl;
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
import cinema.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartDaoTest extends AbstractTest {
    private ShoppingCartDao shoppingCartDao;
    private ShoppingCart shoppingCart;
    private static final LocalDateTime movieSessionTime
            = LocalDateTime.now().plusDays(1).withNano(0);
    private UserDao userDao;
    private User user;
    private Ticket ticket;
    private Role role;
    private RoleDao roleDao;
    private  TicketDao ticketDao;
    private MovieDao movieDao;
    private  CinemaHallDao cinemaHallDao;
    private MovieSessionDao movieSessionDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{ShoppingCart.class, Movie.class, MovieSession.class,
                Ticket.class, CinemaHall.class, Role.class, User.class};
    }


    @BeforeEach
    void setUp() {
        shoppingCartDao = new ShoppingCartDaoImpl(getSessionFactory());
        shoppingCart = new ShoppingCart();
        role = addRoleToDb();
        user = addUserToDb(role);
        shoppingCart.setUser(user);
        List<Ticket> tickets = new ArrayList<>();
        shoppingCart.setTickets(tickets);
    }

    @Test
    void add_Ok() {
        ShoppingCart actual = shoppingCartDao.add(shoppingCart);
        assertNotNull(actual);
        assertEquals(1L, shoppingCart.getId());
    }

    @Test
    void add_ShoppingCartIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.add(null));
    }

    @Test
    void getByUser_Ok() {
        ShoppingCart expected = shoppingCartDao.add(shoppingCart);
        ShoppingCart actual = shoppingCartDao.getByUser(user);
        assertNotNull(actual);
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getId(), actual.getId());
        assertTrue(actual.getTickets().isEmpty());
    }

    @Test
    void getByUser_UserIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.getByUser(null));
    }

    @Test
    void update_Ok() {
        ticket = addTicket(user);
        shoppingCartDao.add(shoppingCart);
        shoppingCart.getTickets().add(ticket);
        ShoppingCart actual = shoppingCartDao.update(shoppingCart);
        assertNotNull(actual);
        assertEquals(actual.getTickets().get(0), ticket);
    }

    @Test
    void update_ShoppingCartIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.update(null));
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
    private Ticket addTicket(User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(addMovieSession());
        ticket.setUser(user);
        ticketDao = new TicketDaoImpl(getSessionFactory());
        return ticketDao.add(ticket);
    }
}