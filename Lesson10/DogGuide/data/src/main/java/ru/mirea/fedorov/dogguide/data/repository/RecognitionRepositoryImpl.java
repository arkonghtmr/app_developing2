package ru.mirea.fedorov.dogguide.data.repository;

import android.content.Context;

import ru.mirea.fedorov.dogguide.data.ml.DogCeoLabelMapper;
import ru.mirea.fedorov.dogguide.data.ml.TfliteBreedClassifier;
import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;
import ru.mirea.fedorov.dogguide.domain.repository.RecognitionRepository;

public class RecognitionRepositoryImpl implements RecognitionRepository {
    private final TfliteBreedClassifier classifier;

    public RecognitionRepositoryImpl(Context context) {
        this.classifier = new TfliteBreedClassifier(context, new DogCeoLabelMapper());
    }

    @Override
    public RecognitionResult recognize(String imageUri) {
        return classifier.classify(imageUri);
    }
}
