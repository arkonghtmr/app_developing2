package ru.mirea.fedorov.lesson9.domain.repository;

import java.util.List;

import ru.mirea.fedorov.lesson9.domain.models.Movie;

public interface MovieRepository {
    boolean saveMovie(Movie movie);

    Movie getMovie();

    List<Movie> getMovies();
}
