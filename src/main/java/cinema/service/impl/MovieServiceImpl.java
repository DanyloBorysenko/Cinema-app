package cinema.service.impl;

import cinema.dao.MovieDao;
import cinema.model.Movie;
import cinema.service.MovieService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao;

    public MovieServiceImpl(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public Movie add(Movie movie) {
        if (movie != null) {
            return movieDao.add(movie);
        }
        throw new RuntimeException("Movie can't be null");
    }

    @Override
    public Movie get(Long id) {
        if (id == null) {
            throw new RuntimeException("Id can't be null");
        }
        return movieDao.get(id).orElseThrow(
                () -> new NoSuchElementException("Can't get movie by id " + id));
    }

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }
}
