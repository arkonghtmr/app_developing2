package ru.mirea.fedorov.dogguide.domain.models;

public class AuthOutcome {
    private final User user;
    private final String errorMessage;

    private AuthOutcome(User user, String errorMessage) {
        this.user = user;
        this.errorMessage = errorMessage;
    }

    public static AuthOutcome success(User user) {
        return new AuthOutcome(user, null);
    }

    public static AuthOutcome failure(String errorMessage) {
        return new AuthOutcome(null, errorMessage);
    }

    public boolean isSuccess() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
