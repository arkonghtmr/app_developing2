package ru.mirea.fedorov.dogguide.data;

import android.content.Context;

import ru.mirea.fedorov.dogguide.data.storage.AppDatabase;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;

public final class DataModule {
    private DataModule() {
    }

    public static FavoriteRepository createFavoriteRepository(Context context) {
        return AppDatabase.createFavoriteRepository(context);
    }
}
