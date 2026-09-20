package ru.mirea.fedorov.dogguide.data.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.mirea.fedorov.dogguide.data.dao.FavoriteDao;
import ru.mirea.fedorov.dogguide.data.dao.UserDao;
import ru.mirea.fedorov.dogguide.data.model.FavoriteBreedEntity;
import ru.mirea.fedorov.dogguide.data.model.UserEntity;
import ru.mirea.fedorov.dogguide.data.repository.FavoriteRepositoryImpl;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;

@Database(
        entities = {FavoriteBreedEntity.class, UserEntity.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();

    public abstract UserDao userDao();

    public static FavoriteRepository createFavoriteRepository(Context context) {
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "dogguide.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        return new FavoriteRepositoryImpl(database.favoriteDao());
    }
}
