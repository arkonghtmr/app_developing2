package ru.mirea.fedorov.dogguide.domain.models;

public class Breed {
    private final String id;
    private final String name;
    private final String imageUrl;
    private final String description;

    public Breed(String id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
