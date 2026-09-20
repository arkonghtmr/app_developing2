package ru.mirea.fedorov.dogguide.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ru.mirea.fedorov.dogguide.data.model.UserEntity;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE login = :login LIMIT 1")
    UserEntity getByLogin(String login);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(UserEntity entity);
}
