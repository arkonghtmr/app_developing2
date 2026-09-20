package ru.mirea.fedorov.lesson9.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class MovieStorage {
    private static final String PREFS_NAME = "favorite_movie";
    private static final String KEY_ID = "movie_id";
    private static final String KEY_NAME = "movie_name";

    private final SharedPreferences prefs;

    public MovieStorage(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void save(int id, String name) {
        prefs.edit()
                .putInt(KEY_ID, id)
                .putString(KEY_NAME, name)
                .apply();
    }

    public int getId() {
        return prefs.getInt(KEY_ID, -1);
    }

    public String getName() {
        return prefs.getString(KEY_NAME, "");
    }
}
