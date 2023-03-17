package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import cinema.dao.CinemaHallDao;
import cinema.exception.InputDataException;
import cinema.model.CinemaHall;
import cinema.service.impl.CinemaHallServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
class CinemaHallServiceTest {
    private CinemaHallDao cinemaHallDaoMock;
    private CinemaHallService cinemaHallService;
    private CinemaHall cinemaHall;
    private int capacity = 20;
    private String description = "VIP";

    @BeforeEach
    void setUp() {
        cinemaHallDaoMock = Mockito.mock(CinemaHallDao.class);
        cinemaHallService = new CinemaHallServiceImpl(cinemaHallDaoMock);
        cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(capacity);
        cinemaHall.setDescription(description);
        cinemaHall.setId(1L);
    }

    @Test
    void add_Ok() {
        Mockito.when(cinemaHallDaoMock.add(cinemaHall)).thenReturn(cinemaHall);
        CinemaHall actual = cinemaHallService.add(cinemaHall);
        assertNotNull(actual);
        assertEquals(cinemaHall, actual);
    }

    @Test
    void add_CinemaHallIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            cinemaHallService.add(null);
        });
    }

    @Test
    void get_Ok() {
        Mockito.when(cinemaHallDaoMock.get(cinemaHall.getId())).thenReturn(Optional.of(cinemaHall));
        CinemaHall actual = cinemaHallService.get(cinemaHall.getId());
        assertNotNull(actual);
        assertEquals(cinemaHall, actual);
    }

    @Test
    void get_IdIsNull_NotOk() {
        assertThrows(InputDataException.class, () -> {
            cinemaHallService.get(null);
        });
    }

    @Test
    void get_InvalidId_NotOk() {
        assertThrows(NoSuchElementException.class, () -> {
            cinemaHallService.get(2L);
        });
    }

    @Test
    void getAll_Ok() {
        Mockito.when(cinemaHallDaoMock.getAll()).thenReturn(List.of(cinemaHall));
        List<CinemaHall> actual = cinemaHallService.getAll();
        assertNotNull(actual);
        assertEquals(List.of(cinemaHall), actual);
    }
}