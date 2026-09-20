package ru.mirea.fedorov.lesson9.data.repository;

import java.time.LocalDate;

import ru.mirea.fedorov.lesson9.data.storage.MovieStorage;
import ru.mirea.fedorov.lesson9.data.storage.models.Movie;
import ru.mirea.fedorov.lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private final MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(ru.mirea.fedorov.lesson9.domain.models.Movie movie) {
        movieStorage.save(mapToStorage(movie));
        return true;
    }

    @Override
    public ru.mirea.fedorov.lesson9.domain.models.Movie getMovie() {
        Movie movie = movieStorage.get();
        return mapToDomain(movie);
    }

    private Movie mapToStorage(ru.mirea.fedorov.lesson9.domain.models.Movie movie) {
        return new Movie(movie.getId(), movie.getName(), LocalDate.now().toString());
    }

    private ru.mirea.fedorov.lesson9.domain.models.Movie mapToDomain(Movie movie) {
        return new ru.mirea.fedorov.lesson9.domain.models.Movie(movie.getId(), movie.getName());
    }
}
