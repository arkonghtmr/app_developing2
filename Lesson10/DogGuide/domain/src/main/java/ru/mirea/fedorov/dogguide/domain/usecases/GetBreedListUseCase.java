package ru.mirea.fedorov.dogguide.domain.usecases;

import java.util.List;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;

public class GetBreedListUseCase {
    private final BreedRepository breedRepository;

    public GetBreedListUseCase(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public List<Breed> execute() {
        return breedRepository.getBreeds();
    }
}
