package ru.mirea.fedorov.dogguide.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String login;
    @NonNull
    private String passwordHash;

    public UserEntity(@NonNull String login, @NonNull String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NonNull String login) {
        this.login = login;
    }

    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@NonNull String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
