package ru.mirea.fedorov.dogguide.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.RecognitionRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;
import ru.mirea.fedorov.dogguide.domain.usecases.AddBreedToFavoritesUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetBreedDetailsUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetCurrentUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RecognizeBreedUseCase;

public class RecognizeViewModel extends ViewModel {
    private final RecognizeBreedUseCase recognizeBreedUseCase;
    private final GetBreedDetailsUseCase getBreedDetailsUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final AddBreedToFavoritesUseCase addBreedToFavoritesUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<RecognitionResult> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public RecognizeViewModel(
            RecognitionRepository recognitionRepository,
            BreedRepository breedRepository,
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.recognizeBreedUseCase = new RecognizeBreedUseCase(recognitionRepository);
        this.getBreedDetailsUseCase = new GetBreedDetailsUseCase(breedRepository);
        this.getCurrentUserUseCase = new GetCurrentUserUseCase(userRepository);
        this.addBreedToFavoritesUseCase = new AddBreedToFavoritesUseCase(
                favoriteRepository,
                userRepository
        );
    }

    public LiveData<RecognitionResult> getResult() {
        return result;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void recognize(String imageUri) {
        loading.setValue(true);
        result.setValue(null);
        executor.execute(() -> {
            RecognitionResult recognized = recognizeBreedUseCase.execute(imageUri);
            result.postValue(recognized);
            loading.postValue(false);
        });
    }

    public void saveResult() {
        RecognitionResult current = result.getValue();
        if (getCurrentUserUseCase.execute() == null) {
            message.setValue("login_required");
            return;
        }
        if (current == null) {
            return;
        }
        executor.execute(() -> {
            Breed breed = getBreedDetailsUseCase.execute(current.getBreedId());
            if (breed == null) {
                message.postValue("not_in_catalog");
                return;
            }
            boolean added = addBreedToFavoritesUseCase.execute(breed);
            message.postValue(added ? "saved:" + breed.getName() : "already");
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        super.onCleared();
    }
}
