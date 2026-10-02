package ru.mirea.fedorov.dogguide.data.remote;

import java.util.List;
import java.util.Map;

public interface NetworkApi {
    Map<String, List<String>> getAllBreeds() throws Exception;

    String getRandomImage(String breedPath) throws Exception;
}
