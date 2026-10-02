package ru.mirea.fedorov.lesson9.domain.usecases;

import ru.mirea.fedorov.lesson9.domain.models.Movie;
import ru.mirea.fedorov.lesson9.domain.repository.MovieRepository;

public class SaveMovieToFavoriteUseCase {
    private MovieRepository movieRepository;

    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        if (movie.getName() == null || movie.getName().trim().isEmpty()) {
            return false;
        }
        return movieRepository.saveMovie(movie);
    }
}
