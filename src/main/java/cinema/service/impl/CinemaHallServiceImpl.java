package cinema.service.impl;

import cinema.dao.CinemaHallDao;
import cinema.exception.InputDataFormatException;
import cinema.model.CinemaHall;
import cinema.service.CinemaHallService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
    private final CinemaHallDao cinemaHallDao;

    public CinemaHallServiceImpl(CinemaHallDao cinemaHallDao) {
        this.cinemaHallDao = cinemaHallDao;
    }

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        if (cinemaHall != null) {
            return cinemaHallDao.add(cinemaHall);
        }
        throw new InputDataFormatException();
    }

    @Override
    public CinemaHall get(Long id) {
        if (id == null) {
            throw new InputDataFormatException();
        }
        return cinemaHallDao.get(id).orElseThrow(
                () -> new NoSuchElementException("Can't get cinema hall by id " + id));
    }

    @Override
    public List<CinemaHall> getAll() {
        return cinemaHallDao.getAll();
    }
}
