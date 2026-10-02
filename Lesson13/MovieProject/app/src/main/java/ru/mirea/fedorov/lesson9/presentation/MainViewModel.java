package ru.mirea.fedorov.lesson9.presentation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.fedorov.lesson9.domain.models.Movie;
import ru.mirea.fedorov.lesson9.domain.repository.MovieRepository;
import ru.mirea.fedorov.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.fedorov.lesson9.domain.usecases.GetMovieListUseCase;
import ru.mirea.fedorov.lesson9.domain.usecases.SaveMovieToFavoriteUseCase;

public class MainViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<String> favoriteMovie = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> items = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel created");
        this.movieRepository = movieRepository;
        items.setValue(new GetMovieListUseCase(movieRepository).execute());
    }

    public MutableLiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public LiveData<List<Movie>> getItems() {
        return items;
    }

    public void setText(Movie movie) {
        Boolean result = new SaveMovieToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(String.format("Save result %s", result));
    }

    public void getText() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s", movie.getName()));
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel cleared");
        super.onCleared();
    }
}
