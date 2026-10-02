package ru.mirea.fedorov.lesson9.domain.models;

public class Movie {
    private int id;
    private String name;
    private String year;

    public Movie(int id, String name) {
        this(id, name, "");
    }

    public Movie(int id, String name, String year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }
}
