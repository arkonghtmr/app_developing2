package ru.mirea.fedorov.dogguide.data.repository;

import com.google.firebase.auth.FirebaseAuthException;

import java.util.Locale;

import ru.mirea.fedorov.dogguide.data.firebase.FirebaseAuthDataSource;
import ru.mirea.fedorov.dogguide.data.storage.sharedprefs.ClientStorage;
import ru.mirea.fedorov.dogguide.domain.models.AuthOutcome;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseAuthDataSource firebaseAuthDataSource;
    private final ClientStorage clientStorage;
    private User currentUser;

    public UserRepositoryImpl(
            FirebaseAuthDataSource firebaseAuthDataSource,
            ClientStorage clientStorage
    ) {
        this.firebaseAuthDataSource = firebaseAuthDataSource;
        this.clientStorage = clientStorage;
        restoreSession();
    }

    @Override
    public AuthOutcome login(String login, String password) {
        try {
            firebaseAuthDataSource.login(toEmail(login), password);
            return AuthOutcome.success(persist(login));
        } catch (Exception exception) {
            return AuthOutcome.failure(messageFrom(exception));
        }
    }

    @Override
    public AuthOutcome register(String login, String password) {
        try {
            firebaseAuthDataSource.register(toEmail(login), password);
            return AuthOutcome.success(persist(login));
        } catch (Exception exception) {
            return AuthOutcome.failure(messageFrom(exception));
        }
    }

    @Override
    public void logout() {
        firebaseAuthDataSource.logout();
        clientStorage.clear();
        currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    private void restoreSession() {
        if (firebaseAuthDataSource.getCurrentUser() == null) {
            return;
        }
        String login = clientStorage.getLogin();
        if (login == null || login.isEmpty()) {
            return;
        }
        currentUser = new User(login, login);
    }

    private User persist(String login) {
        String normalized = normalize(login);
        clientStorage.saveLogin(normalized);
        currentUser = new User(normalized, normalized);
        return currentUser;
    }

    private String toEmail(String login) {
        String normalized = normalize(login);
        if (normalized.contains("@")) {
            return normalized;
        }
        return normalized + "@dogguide.app";
    }

    private String normalize(String login) {
        return login.trim().toLowerCase(Locale.ROOT);
    }

    private String messageFrom(Exception exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof FirebaseAuthException) {
                String code = ((FirebaseAuthException) cause).getErrorCode();
                if (code == null) {
                    code = "";
                }
                String normalized = code.toLowerCase(Locale.ROOT);
                if (normalized.contains("email_already_in_use")
                        || normalized.contains("email-already-in-use")) {
                    return "Такой логин уже зарегистрирован";
                }
                if (normalized.contains("weak_password") || normalized.contains("weak-password")) {
                    return "Пароль не короче 6 символов";
                }
                if (normalized.contains("invalid_email") || normalized.contains("invalid-email")) {
                    return "Некорректный логин. Укажите email или латиницу без пробелов";
                }
                if (normalized.contains("wrong_password") || normalized.contains("wrong-password")
                        || normalized.contains("invalid_credential")
                        || normalized.contains("invalid-credential")
                        || normalized.contains("user_not_found")
                        || normalized.contains("user-not-found")) {
                    return "Неверный логин или пароль";
                }
                if (normalized.contains("operation_not_allowed")
                        || normalized.contains("operation-not-allowed")) {
                    return "В Firebase не включён вход по Email/Password";
                }
                return "Ошибка Firebase: " + code;
            }
            cause = cause.getCause();
        }
        String message = exception.getMessage();
        if (message == null || message.isEmpty()) {
            return "Не удалось выполнить вход";
        }
        return "Ошибка Firebase: " + message;
    }
}
