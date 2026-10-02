package ru.mirea.fedorov.dogguide.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "favorites", primaryKeys = {"ownerLogin", "id"})
public class FavoriteBreedEntity {
    @NonNull
    private String ownerLogin;
    @NonNull
    private String id;
    private String name;
    private String imageUrl;
    private String description;

    public FavoriteBreedEntity(
            @NonNull String ownerLogin,
            @NonNull String id,
            String name,
            String imageUrl,
            String description
    ) {
        this.ownerLogin = ownerLogin;
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @NonNull
    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(@NonNull String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
