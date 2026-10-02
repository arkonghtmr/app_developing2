package ru.mirea.fedorov.dogguide.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.domain.models.AuthOutcome;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;
import ru.mirea.fedorov.dogguide.domain.usecases.LoginUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RegisterUserUseCase;

public class LoginViewModel extends ViewModel {
    private final LoginUserUseCase loginUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<AuthOutcome> outcome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LoginViewModel(UserRepository userRepository) {
        this.loginUserUseCase = new LoginUserUseCase(userRepository);
        this.registerUserUseCase = new RegisterUserUseCase(userRepository);
    }

    public LiveData<AuthOutcome> getOutcome() {
        return outcome;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void login(String login, String password) {
        submit(true, login, password);
    }

    public void register(String login, String password) {
        submit(false, login, password);
    }

    private void submit(boolean login, String name, String password) {
        loading.setValue(true);
        executor.execute(() -> {
            AuthOutcome result = login
                    ? loginUserUseCase.execute(name, password)
                    : registerUserUseCase.execute(name, password);
            outcome.postValue(result);
            loading.postValue(false);
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        super.onCleared();
    }
}
