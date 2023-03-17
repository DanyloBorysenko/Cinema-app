package cinema.service;

import static cinema.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.exception.InputDataException;
import cinema.model.*;
import cinema.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ShoppingCartServiceTest {
    private ShoppingCartDao shoppingCartDaoMock;
    private TicketDao ticketDaoMock;
    private ShoppingCartService shoppingCartService;
    private User user;

    @BeforeEach
    void setUp() {
        shoppingCartDaoMock = Mockito.mock(ShoppingCartDao.class);
        ticketDaoMock = Mockito.mock(TicketDao.class);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartDaoMock, ticketDaoMock);
        user = new User();
        user.setRoles(Set.of(new Role(ADMIN)));
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setPassword("password");
    }

    @Test
    void addSession_Ok() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(new ArrayList<>());
        Mockito.when(shoppingCartDaoMock.getByUser(user)).thenReturn(shoppingCart);
        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(new CinemaHall());
        movieSession.setId(1L);
        movieSession.setMovie(new Movie());
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));
        shoppingCartService.addSession(movieSession, user);
        Mockito.verify(ticketDaoMock).add(any());
        Mockito.verify(shoppingCartDaoMock).update(shoppingCart);
    }

    @Test
    void addSession_MovieSessionIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            shoppingCartService.addSession(null, user);
        });
    }

    @Test
    void addSession_UserIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            shoppingCartService.addSession(new MovieSession(), null);
        });
    }

    @Test
    void getByUser_Ok() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(1L);
        shoppingCart.setTickets(new ArrayList<>());
        Mockito.when(shoppingCartDaoMock.getByUser(user)).thenReturn(shoppingCart);
        ShoppingCart actual = shoppingCartService.getByUser(user);
        assertNotNull(actual);
        assertEquals(shoppingCart, actual);
    }

    @Test
    void getByUser_UserIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            shoppingCartService.getByUser(null);
        });
    }

    @Test
    void registerNewShoppingCart_Ok() {
        shoppingCartService.registerNewShoppingCart(user);
        Mockito.verify(shoppingCartDaoMock).add(any());
    }

    @Test
    void registerNewShoppingCart_UserIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            shoppingCartService.registerNewShoppingCart(null);
        });
    }

    @Test
    void clear_Ok() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartService.clear(shoppingCart);
        Mockito.verify(shoppingCartDaoMock).update(shoppingCart);
    }

    @Test
    void clear_ShoppingCartIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            shoppingCartService.clear(null);
        });
    }
}