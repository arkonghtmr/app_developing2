package ru.mirea.fedorov.dogguide.domain.repository;

import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;

public interface RecognitionRepository {
    RecognitionResult recognize(String imageUri);
}
