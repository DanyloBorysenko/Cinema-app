package cinema.service;

import static cinema.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import cinema.dao.OrderDao;
import cinema.exception.InputDataException;
import cinema.model.*;
import cinema.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderServiceTest {
    private OrderDao orderDaoMock;
    private ShoppingCartService shoppingCartServiceMock;
    private OrderService orderService;
    private User user;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        orderDaoMock = Mockito.mock(OrderDao.class);
        shoppingCartServiceMock = Mockito.mock(ShoppingCartService.class);
        orderService = new OrderServiceImpl(orderDaoMock, shoppingCartServiceMock);
        user = new User();
        user.setEmail("user@gmail.com");
        user.setId(1L);
        user.setPassword("password");
        user.setRoles(Set.of(new Role(ADMIN)));
        ticket = new Ticket();
        ticket.setUser(user);
        ticket.setId(1L);
        MovieSession movieSession = new MovieSession();
        ticket.setMovieSession(movieSession);
    }

    @Test
    void completeOrder_Ok() {
        ShoppingCart shoppingCart = createShoppingCart(user, ticket);
        Order order = orderService.completeOrder(shoppingCart);
        assertNotNull(order);
        assertTrue(order.getOrderTime().isBefore(LocalDateTime.now()));
        assertEquals(shoppingCart.getTickets(), order.getTickets());
        assertEquals(shoppingCart.getUser(), order.getUser());
        Mockito.verify(orderDaoMock).add(order);
        Mockito.verify(shoppingCartServiceMock).clear(shoppingCart);
    }

    @Test
    void completeOrder_ShoppingCartIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            orderService.completeOrder(null);
        });
    }

    @Test
    void getOrdersHistory_Ok() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setTickets(List.of(ticket));
        order.setOrderTime(LocalDateTime.now());
        Mockito.when(orderDaoMock.getOrdersHistory(user)).thenReturn(List.of(order));
        List<Order> actual = orderService.getOrdersHistory(user);
        assertNotNull(actual);
        assertEquals(List.of(order), actual);
    }

    @Test
    void getOrdersHistory_UserIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            orderService.getOrdersHistory(null);
        });
    }

    private ShoppingCart createShoppingCart(User user, Ticket ticket) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setTickets(List.of(ticket));
        return shoppingCart;
    }
}