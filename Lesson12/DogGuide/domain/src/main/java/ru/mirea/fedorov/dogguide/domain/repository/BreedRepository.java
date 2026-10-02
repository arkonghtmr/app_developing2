package ru.mirea.fedorov.dogguide.domain.repository;

import java.util.List;

import ru.mirea.fedorov.dogguide.domain.models.Breed;

public interface BreedRepository {
    List<Breed> getBreeds();

    Breed getBreedById(String id);
}
