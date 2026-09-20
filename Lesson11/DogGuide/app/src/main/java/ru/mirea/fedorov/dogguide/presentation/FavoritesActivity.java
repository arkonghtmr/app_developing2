package ru.mirea.fedorov.dogguide.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.fedorov.dogguide.R;

public class FavoritesActivity extends BaseActivity {
    private FavoritesViewModel viewModel;
    private BreedAdapter adapter;
    private TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setupChrome(true);
        setTitle(R.string.favorites);

        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(getApplication())
        ).get(FavoritesViewModel.class);

        textViewEmpty = findViewById(R.id.textViewEmpty);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BreedAdapter(breed -> {
            Intent intent = new Intent(FavoritesActivity.this, BreedDetailsActivity.class);
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_ID, breed.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        viewModel.getFavorites().observe(this, favorites -> {
            adapter.submitList(favorites);
            textViewEmpty.setVisibility(
                    favorites == null || favorites.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.load();
    }
}
