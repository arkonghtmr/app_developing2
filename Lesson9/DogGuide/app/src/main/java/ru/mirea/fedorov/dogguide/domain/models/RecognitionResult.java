package ru.mirea.fedorov.dogguide.domain.models;

public class RecognitionResult {
    private final String breedId;
    private final String breedName;
    private final float confidence;
    private final String topSummary;

    public RecognitionResult(String breedId, String breedName, float confidence, String topSummary) {
        this.breedId = breedId;
        this.breedName = breedName;
        this.confidence = confidence;
        this.topSummary = topSummary;
    }

    public String getBreedId() {
        return breedId;
    }

    public String getBreedName() {
        return breedName;
    }

    public float getConfidence() {
        return confidence;
    }

    public String getTopSummary() {
        return topSummary;
    }
}
