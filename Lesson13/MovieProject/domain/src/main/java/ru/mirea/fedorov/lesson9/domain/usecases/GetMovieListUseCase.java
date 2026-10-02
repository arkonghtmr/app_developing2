package ru.mirea.fedorov.lesson9.domain.usecases;

import java.util.List;

import ru.mirea.fedorov.lesson9.domain.models.Movie;
import ru.mirea.fedorov.lesson9.domain.repository.MovieRepository;

public class GetMovieListUseCase {
    private final MovieRepository movieRepository;

    public GetMovieListUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> execute() {
        return movieRepository.getMovies();
    }
}
