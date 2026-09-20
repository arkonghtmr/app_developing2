package ru.mirea.fedorov.dogguide.data.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.mirea.fedorov.dogguide.data.remote.MockNetworkApi;
import ru.mirea.fedorov.dogguide.data.remote.NetworkApi;
import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;

public class BreedRepositoryImpl implements BreedRepository {
    private static final int MAX_BREEDS = 16;
    private static final List<String> PRIORITY = Arrays.asList(
            "husky",
            "retriever/golden",
            "pug",
            "hound/afghan",
            "corgi/cardigan",
            "shepherd/german",
            "beagle",
            "boxer",
            "dalmatian",
            "shiba",
            "chihuahua",
            "labrador",
            "malamute",
            "akita",
            "samoyed",
            "pomeranian"
    );

    private final NetworkApi networkApi;
    private final NetworkApi mockNetworkApi;
    private final List<Breed> cache = new ArrayList<>();

    public BreedRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
        this.mockNetworkApi = new MockNetworkApi();
    }

    @Override
    public synchronized List<Breed> getBreeds() {
        if (!cache.isEmpty()) {
            return new ArrayList<>(cache);
        }
        try {
            cache.addAll(loadFromApi(networkApi));
        } catch (Exception ignored) {
            try {
                cache.addAll(loadFromApi(mockNetworkApi));
            } catch (Exception mockError) {
                cache.addAll(fallback());
            }
        }
        return new ArrayList<>(cache);
    }

    @Override
    public synchronized Breed getBreedById(String id) {
        for (Breed breed : getBreeds()) {
            if (breed.getId().equals(id)) {
                return breed;
            }
        }
        try {
            String imageUrl = networkApi.getRandomImage(id);
            Breed breed = new Breed(id, displayName(id), imageUrl, description(id));
            cache.add(breed);
            return breed;
        } catch (Exception ignored) {
            return null;
        }
    }

    private List<Breed> loadFromApi(NetworkApi api) throws Exception {
        Map<String, List<String>> all = api.getAllBreeds();
        List<String> paths = new ArrayList<>();
        for (String path : PRIORITY) {
            if (exists(all, path)) {
                paths.add(path);
            }
        }
        for (Map.Entry<String, List<String>> entry : all.entrySet()) {
            if (paths.size() >= MAX_BREEDS) {
                break;
            }
            if (entry.getValue().isEmpty()) {
                addUnique(paths, entry.getKey());
            } else {
                addUnique(paths, entry.getKey() + "/" + entry.getValue().get(0));
            }
        }

        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<Breed>> futures = new ArrayList<>();
        for (String path : paths) {
            futures.add(pool.submit(new Callable<Breed>() {
                @Override
                public Breed call() throws Exception {
                    String imageUrl = api.getRandomImage(path);
                    return new Breed(path, displayName(path), imageUrl, description(path));
                }
            }));
        }
        List<Breed> result = new ArrayList<>();
        for (Future<Breed> future : futures) {
            try {
                result.add(future.get());
            } catch (Exception ignored) {
                // пропускаем породу, если фото не пришло
            }
        }
        pool.shutdown();
        if (result.isEmpty()) {
            throw new IllegalStateException("Dog CEO returned no breeds");
        }
        return result;
    }

    private boolean exists(Map<String, List<String>> all, String path) {
        String[] parts = path.split("/");
        List<String> sub = all.get(parts[0]);
        if (sub == null) {
            return false;
        }
        if (parts.length == 1) {
            return true;
        }
        return sub.contains(parts[1]);
    }

    private void addUnique(List<String> paths, String path) {
        if (!paths.contains(path)) {
            paths.add(path);
        }
    }

    private String displayName(String path) {
        String[] parts = path.split("/");
        if (parts.length == 1) {
            return capitalize(parts[0]);
        }
        return capitalize(parts[1]) + " " + capitalize(parts[0]);
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private String description(String path) {
        return "Данные загружены из Dog CEO API. Порода: " + displayName(path) + ".";
    }

    private List<Breed> fallback() {
        return Arrays.asList(
                new Breed(
                        "husky",
                        "Husky",
                        "https://images.dog.ceo/breeds/husky/n02110185_10047.jpg",
                        "Офлайн-заглушка: нет ответа от Dog CEO."
                ),
                new Breed(
                        "pug",
                        "Pug",
                        "https://images.dog.ceo/breeds/pug/n02110958_15626.jpg",
                        "Офлайн-заглушка: нет ответа от Dog CEO."
                )
        );
    }
}
