package cinema.dao;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.Collections;
import java.util.List;
import cinema.AbstractTest;
import cinema.dao.impl.CinemaHallDaoImpl;
import cinema.dao.impl.MovieSessionDaoImpl;
import cinema.dao.impl.TicketDaoImpl;
import cinema.dao.impl.MovieDaoImpl;
import cinema.dao.impl.OrderDaoImpl;
import cinema.dao.impl.RoleDaoImpl;
import cinema.dao.impl.UserDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.Movie;
import cinema.model.CinemaHall;
import cinema.model.MovieSession;
import cinema.model.Order;
import cinema.model.Role;
import cinema.model.Ticket;
import cinema.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderDaoTest extends AbstractTest {
    private static final LocalDateTime movieSessionTime
            = LocalDateTime.now().plusDays(1).withNano(0);
    private static final LocalDateTime orderTime = LocalDateTime.now().withNano(0);
    private UserDao userDao;
    private OrderDao orderDao;
    private Order order;
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
        return new Class[]{Order.class, Movie.class, MovieSession.class,
                Ticket.class, CinemaHall.class, Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        role = addRoleToDb();
        user = addUserToDb(role);
        ticket = addTicket(user);
        order = new Order();
        order.setUser(user);
        order.setOrderTime(orderTime);
        order.setTickets(List.of(ticket));
        orderDao = new OrderDaoImpl(getSessionFactory());
    }

    @Test
    void add_Ok() {
        Order actual = orderDao.add(order);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_OrderIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> orderDao.add(null));
    }

    @Test
    void getOrdersHistory_Ok() {
        orderDao.add(order);
        List<Order> actual = orderDao.getOrdersHistory(user);
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertIterableEquals(List.of(ticket), actual.get(0).getTickets());
        assertEquals(user, actual.get(0).getUser());
    }

    @Test
    void getOrdersHistory_UserIsNull_Ok() {
        List<Order> actual = orderDao.getOrdersHistory(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void getOrdersHistory_InvalidUser_NotOk() {
        user.setEmail("newUser");
        List<Order> actual = orderDao.getOrdersHistory(user);
        assertIterableEquals(Collections.emptyList(), actual);
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