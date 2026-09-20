package ru.mirea.fedorov.dogguide.data.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mirea.fedorov.dogguide.data.dao.FavoriteDao;
import ru.mirea.fedorov.dogguide.data.dao.UserDao;
import ru.mirea.fedorov.dogguide.data.model.FavoriteBreedEntity;
import ru.mirea.fedorov.dogguide.data.model.UserEntity;

@Database(
        entities = {FavoriteBreedEntity.class, UserEntity.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();

    public abstract UserDao userDao();
}
