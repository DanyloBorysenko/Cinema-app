package cinema.controller;

import cinema.dto.request.MovieRequestDto;
import cinema.dto.response.MovieResponseDto;
import cinema.mapper.RequestDtoMapper;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.Movie;
import cinema.service.MovieService;
import java.util.List;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private static final Logger logger = LogManager.getLogger(MovieController.class);
    private final MovieService movieService;
    private final RequestDtoMapper<MovieRequestDto, Movie> movieRequestDtoMapper;
    private final ResponseDtoMapper<MovieResponseDto, Movie> movieResponseDtoMapper;

    public MovieController(MovieService movieService,
            RequestDtoMapper<MovieRequestDto, Movie> movieRequestDtoMapper,
            ResponseDtoMapper<MovieResponseDto, Movie> movieResponseDtoMapper) {
        this.movieService = movieService;
        this.movieRequestDtoMapper = movieRequestDtoMapper;
        this.movieResponseDtoMapper = movieResponseDtoMapper;
    }

    @PostMapping
    public MovieResponseDto add(@RequestBody @Valid MovieRequestDto requestDto) {
        logger.info("Method add was called. Params: title = {}, description = {}.",
                requestDto.getTitle(), requestDto.getDescription());
        Movie movie = movieService.add(movieRequestDtoMapper.mapToModel(requestDto));
        return movieResponseDtoMapper.mapToDto(movie);
    }

    @GetMapping
    public List<MovieResponseDto> getAll() {
        logger.info("Method getAll was called.");
        return movieService.getAll()
                .stream()
                .map(movieResponseDtoMapper::mapToDto)
                .toList();
    }
}
