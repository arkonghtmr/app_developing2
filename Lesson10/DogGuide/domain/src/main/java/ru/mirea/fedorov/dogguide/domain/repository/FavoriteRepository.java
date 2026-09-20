package ru.mirea.fedorov.dogguide.domain.repository;

import java.util.List;

import ru.mirea.fedorov.dogguide.domain.models.Breed;

public interface FavoriteRepository {
    boolean add(String ownerLogin, Breed breed);

    boolean remove(String ownerLogin, String breedId);

    boolean isFavorite(String ownerLogin, String breedId);

    List<Breed> getAll(String ownerLogin);
}
