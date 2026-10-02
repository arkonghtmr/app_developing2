package ru.mirea.fedorov.fragmentmanagerapp;

public class Country {
    private final String name;
    private final String capital;
    private final String description;

    public Country(String name, String capital, String description) {
        this.name = name;
        this.capital = capital;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public String getDescription() {
        return description;
    }
}
