package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;

public class GetBreedDetailsUseCase {
    private final BreedRepository breedRepository;

    public GetBreedDetailsUseCase(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public Breed execute(String id) {
        return breedRepository.getBreedById(id);
    }
}
