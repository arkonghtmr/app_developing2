package ru.mirea.fedorov.dogguide.data.mapper;

import ru.mirea.fedorov.dogguide.data.model.FavoriteBreedEntity;
import ru.mirea.fedorov.dogguide.domain.models.Breed;

public class FavoriteBreedMapper {
    public static FavoriteBreedEntity toEntity(String ownerLogin, Breed breed) {
        return new FavoriteBreedEntity(
                ownerLogin,
                breed.getId(),
                breed.getName(),
                breed.getImageUrl(),
                breed.getDescription()
        );
    }

    public static Breed toDomain(FavoriteBreedEntity entity) {
        return new Breed(
                entity.getId(),
                entity.getName(),
                entity.getImageUrl(),
                entity.getDescription()
        );
    }
}
