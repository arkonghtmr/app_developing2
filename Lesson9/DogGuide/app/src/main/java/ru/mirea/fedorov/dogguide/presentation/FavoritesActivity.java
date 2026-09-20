package ru.mirea.fedorov.dogguide.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mirea.fedorov.dogguide.DogGuideApp;
import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.usecases.GetFavoriteBreedsUseCase;

public class FavoritesActivity extends BaseActivity {
    private BreedAdapter adapter;
    private TextView textViewEmpty;
    private GetFavoriteBreedsUseCase getFavoriteBreedsUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setupChrome(true);
        setTitle(R.string.favorites);

        DogGuideApp app = (DogGuideApp) getApplication();
        getFavoriteBreedsUseCase = new GetFavoriteBreedsUseCase(
                app.getFavoriteRepository(),
                app.getUserRepository()
        );

        textViewEmpty = findViewById(R.id.textViewEmpty);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BreedAdapter(breed -> {
            Intent intent = new Intent(FavoritesActivity.this, BreedDetailsActivity.class);
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_ID, breed.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Breed> favorites = getFavoriteBreedsUseCase.execute();
        adapter.submitList(favorites);
        textViewEmpty.setVisibility(favorites.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
