package ru.mirea.fedorov.dogguide.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.fedorov.dogguide.DogGuideApp;

public class DogGuideViewModelFactory implements ViewModelProvider.Factory {
    private final DogGuideApp app;

    public DogGuideViewModelFactory(Application application) {
        this.app = (DogGuideApp) application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BreedListViewModel.class)) {
            return (T) new BreedListViewModel(
                    app.getBreedRepository(),
                    app.getFavoriteRepository(),
                    app.getUserRepository()
            );
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(app.getUserRepository());
        }
        if (modelClass.isAssignableFrom(BreedDetailsViewModel.class)) {
            return (T) new BreedDetailsViewModel(
                    app.getBreedRepository(),
                    app.getFavoriteRepository(),
                    app.getUserRepository()
            );
        }
        if (modelClass.isAssignableFrom(FavoritesViewModel.class)) {
            return (T) new FavoritesViewModel(
                    app.getFavoriteRepository(),
                    app.getUserRepository()
            );
        }
        if (modelClass.isAssignableFrom(RecognizeViewModel.class)) {
            return (T) new RecognizeViewModel(
                    app.getRecognitionRepository(),
                    app.getBreedRepository(),
                    app.getFavoriteRepository(),
                    app.getUserRepository()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
    }
}
