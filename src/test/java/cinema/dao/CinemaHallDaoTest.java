package cinema.dao;

import cinema.AbstractTest;
import cinema.dao.impl.CinemaHallDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CinemaHallDaoTest extends AbstractTest {
    private CinemaHallDao cinemaHallDao;
    private CinemaHall cinemaHall;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{CinemaHall.class};
    }

    @BeforeEach
    void setUp() {
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        cinemaHall = new CinemaHall();
        cinemaHall.setDescription("Big");
        cinemaHall.setCapacity(25);
    }

    @Test
    void add_Ok() {
        CinemaHall actual = cinemaHallDao.add(cinemaHall);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_CinemaHallIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> {
            cinemaHallDao.add(null);
        });
    }

    @Test
    void get_OK() {
        cinemaHallDao.add(cinemaHall);
        Optional<CinemaHall> optionalCinemaHall = cinemaHallDao.get(1L);
        assertTrue(optionalCinemaHall.isPresent());
        assertEquals(cinemaHall, optionalCinemaHall.get());
    }

    @Test
    void get_IdIsNull_Not_OK() {
        assertThrows(DataProcessingException.class, () -> {
            cinemaHallDao.get(null);
        });
    }

    @Test
    void get_InvalidData_OK() {
        Optional<CinemaHall> optionalCinemaHall = cinemaHallDao.get(1L);
        assertTrue(optionalCinemaHall.isEmpty());
    }

    @Test
    void getAll_Ok() {
       cinemaHallDao.add(cinemaHall);
        List<CinemaHall> actual = cinemaHallDao.getAll();
        assertNotNull(actual);
        assertEquals(List.of(cinemaHall), actual);
    }
}