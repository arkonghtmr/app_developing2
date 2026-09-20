package ru.mirea.fedorov.dogguide.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.mirea.fedorov.dogguide.data.model.FavoriteBreedEntity;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE ownerLogin = :ownerLogin")
    List<FavoriteBreedEntity> getAll(String ownerLogin);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(FavoriteBreedEntity entity);

    @Query("DELETE FROM favorites WHERE ownerLogin = :ownerLogin AND id = :id")
    int deleteById(String ownerLogin, String id);

    @Query("SELECT COUNT(*) FROM favorites WHERE ownerLogin = :ownerLogin AND id = :id")
    int countById(String ownerLogin, String id);
}
