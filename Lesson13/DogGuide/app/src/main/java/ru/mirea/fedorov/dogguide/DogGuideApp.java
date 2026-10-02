package ru.mirea.fedorov.dogguide;

import android.app.Application;

import ru.mirea.fedorov.dogguide.data.DataModule;
import ru.mirea.fedorov.dogguide.data.firebase.FirebaseAuthDataSource;
import ru.mirea.fedorov.dogguide.data.remote.DogCeoApi;
import ru.mirea.fedorov.dogguide.data.repository.BreedRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.repository.RecognitionRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.repository.UserRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.storage.sharedprefs.ClientStorage;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.RecognitionRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class DogGuideApp extends Application {
    private UserRepository userRepository;
    private BreedRepository breedRepository;
    private FavoriteRepository favoriteRepository;
    private RecognitionRepository recognitionRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        userRepository = new UserRepositoryImpl(
                new FirebaseAuthDataSource(),
                new ClientStorage(this)
        );
        breedRepository = new BreedRepositoryImpl(new DogCeoApi());
        favoriteRepository = DataModule.createFavoriteRepository(this);
        recognitionRepository = new RecognitionRepositoryImpl(this);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public BreedRepository getBreedRepository() {
        return breedRepository;
    }

    public FavoriteRepository getFavoriteRepository() {
        return favoriteRepository;
    }

    public RecognitionRepository getRecognitionRepository() {
        return recognitionRepository;
    }
}
