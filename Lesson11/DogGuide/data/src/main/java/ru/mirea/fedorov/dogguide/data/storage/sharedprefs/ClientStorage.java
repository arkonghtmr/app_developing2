package ru.mirea.fedorov.dogguide.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class ClientStorage {
    private static final String PREFS_NAME = "client_prefs";
    private static final String KEY_LOGIN = "client_login";

    private final SharedPreferences prefs;

    public ClientStorage(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveLogin(String login) {
        prefs.edit().putString(KEY_LOGIN, login).apply();
    }

    public String getLogin() {
        return prefs.getString(KEY_LOGIN, "");
    }

    public void clear() {
        prefs.edit().remove(KEY_LOGIN).apply();
    }
}
