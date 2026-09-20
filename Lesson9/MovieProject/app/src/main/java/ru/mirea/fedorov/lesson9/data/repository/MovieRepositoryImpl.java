package ru.mirea.fedorov.lesson9.data.repository;

import android.content.Context;

import ru.mirea.fedorov.lesson9.data.storage.MovieStorage;
import ru.mirea.fedorov.lesson9.domain.models.Movie;
import ru.mirea.fedorov.lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private final MovieStorage storage;

    public MovieRepositoryImpl(Context context) {
        this.storage = new MovieStorage(context);
    }

    @Override
    public boolean saveMovie(Movie movie) {
        storage.save(movie.getId(), movie.getName());
        return true;
    }

    @Override
    public Movie getMovie() {
        String name = storage.getName();
        if (name == null || name.isEmpty()) {
            return new Movie(-1, "Нет данных!");
        }
        return new Movie(storage.getId(), name);
    }
}
