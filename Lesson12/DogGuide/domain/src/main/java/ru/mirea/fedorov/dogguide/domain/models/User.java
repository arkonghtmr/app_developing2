package ru.mirea.fedorov.dogguide.domain.models;

public class User {
    private final String id;
    private final String login;

    public User(String id, String login) {
        this.id = id;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
}
