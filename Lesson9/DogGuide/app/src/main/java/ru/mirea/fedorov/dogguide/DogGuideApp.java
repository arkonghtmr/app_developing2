package ru.mirea.fedorov.dogguide;

import android.app.Application;

import androidx.room.Room;

import ru.mirea.fedorov.dogguide.data.repository.BreedRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.repository.FavoriteRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.repository.RecognitionRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.repository.UserRepositoryImpl;
import ru.mirea.fedorov.dogguide.data.storage.AppDatabase;
import ru.mirea.fedorov.dogguide.data.storage.PasswordHasher;
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
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "dogguide.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        userRepository = new UserRepositoryImpl(database.userDao(), new PasswordHasher());
        breedRepository = new BreedRepositoryImpl();
        favoriteRepository = new FavoriteRepositoryImpl(database.favoriteDao());
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
