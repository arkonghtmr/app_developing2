package ru.mirea.fedorov.lesson9.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.fedorov.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.fedorov.lesson9.data.storage.MovieStorage;
import ru.mirea.fedorov.lesson9.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.fedorov.lesson9.domain.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(context);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
        return (T) new MainViewModel(movieRepository);
    }
}
