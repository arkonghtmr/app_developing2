package ru.mirea.fedorov.dogguide.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.fedorov.dogguide.data.dao.FavoriteDao;
import ru.mirea.fedorov.dogguide.data.mapper.FavoriteBreedMapper;
import ru.mirea.fedorov.dogguide.data.model.FavoriteBreedEntity;
import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoriteDao favoriteDao;

    public FavoriteRepositoryImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Override
    public boolean add(String ownerLogin, Breed breed) {
        if (ownerLogin == null || breed == null || isFavorite(ownerLogin, breed.getId())) {
            return false;
        }
        long rowId = favoriteDao.insert(FavoriteBreedMapper.toEntity(ownerLogin, breed));
        return rowId != -1;
    }

    @Override
    public boolean remove(String ownerLogin, String breedId) {
        return favoriteDao.deleteById(ownerLogin, breedId) > 0;
    }

    @Override
    public boolean isFavorite(String ownerLogin, String breedId) {
        return favoriteDao.countById(ownerLogin, breedId) > 0;
    }

    @Override
    public List<Breed> getAll(String ownerLogin) {
        List<Breed> result = new ArrayList<>();
        for (FavoriteBreedEntity entity : favoriteDao.getAll(ownerLogin)) {
            result.add(FavoriteBreedMapper.toDomain(entity));
        }
        return result;
    }
}
