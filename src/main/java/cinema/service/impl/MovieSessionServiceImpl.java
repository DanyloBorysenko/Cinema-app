package cinema.service.impl;

import cinema.dao.MovieSessionDao;
import cinema.exception.InputDataFormatException;
import cinema.model.MovieSession;
import cinema.service.MovieSessionService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieSessionServiceImpl implements MovieSessionService {
    private final MovieSessionDao movieSessionDao;

    public MovieSessionServiceImpl(MovieSessionDao movieSessionDao) {
        this.movieSessionDao = movieSessionDao;
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        if (movieId != null && date != null) {
            return movieSessionDao.findAvailableSessions(movieId, date);
        }
        throw new InputDataFormatException();
    }

    @Override
    public MovieSession add(MovieSession session) {
        if (session != null) {
            return movieSessionDao.add(session);
        }
        throw new InputDataFormatException();
    }

    @Override
    public MovieSession get(Long id) {
        if (id == null) {
            throw new InputDataFormatException();
        }
        return movieSessionDao.get(id).orElseThrow(
                () -> new RuntimeException("Session with id " + id + " not found"));
    }

    @Override
    public MovieSession update(MovieSession movieSession) {
        if (movieSession != null) {
            return movieSessionDao.update(movieSession);
        }
        throw new InputDataFormatException();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new InputDataFormatException();
        }
        movieSessionDao.delete(id);
    }
}
