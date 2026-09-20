package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;
import ru.mirea.fedorov.dogguide.domain.repository.RecognitionRepository;

public class RecognizeBreedUseCase {
    private final RecognitionRepository recognitionRepository;

    public RecognizeBreedUseCase(RecognitionRepository recognitionRepository) {
        this.recognitionRepository = recognitionRepository;
    }

    public RecognitionResult execute(String imageUri) {
        return recognitionRepository.recognize(imageUri);
    }
}
