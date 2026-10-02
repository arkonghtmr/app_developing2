package ru.mirea.fedorov.lesson9.data.storage;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.fedorov.lesson9.data.storage.models.Movie;

public class StubMovieCatalog {
    private StubMovieCatalog() {
    }

    public static List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(1, "The Shawshank Redemption", "1994"));
        movies.add(new Movie(2, "The Godfather", "1972"));
        movies.add(new Movie(3, "The Dark Knight", "2008"));
        movies.add(new Movie(4, "Pulp Fiction", "1994"));
        movies.add(new Movie(5, "Inception", "2010"));
        movies.add(new Movie(6, "Interstellar", "2014"));
        movies.add(new Movie(7, "Parasite", "2019"));
        movies.add(new Movie(8, "Spirited Away", "2001"));
        movies.add(new Movie(9, "The Matrix", "1999"));
        movies.add(new Movie(10, "Forrest Gump", "1994"));
        return movies;
    }
}
