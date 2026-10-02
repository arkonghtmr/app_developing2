package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;

public class BreedDetailsActivity extends BaseActivity {
    public static final String EXTRA_BREED_ID = "breed_id";

    private BreedDetailsViewModel viewModel;
    private Button buttonFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_details);
        setupChrome(true);

        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(getApplication())
        ).get(BreedDetailsViewModel.class);

        ImageView imageView = findViewById(R.id.imageViewBreed);
        TextView textViewName = findViewById(R.id.textViewBreedName);
        TextView textViewDescription = findViewById(R.id.textViewBreedDescription);
        buttonFavorite = findViewById(R.id.buttonFavorite);

        viewModel.getBreed().observe(this, breed -> {
            if (breed == null) {
                finish();
                return;
            }
            bindBreed(breed, imageView, textViewName, textViewDescription);
        });
        viewModel.getFavorite().observe(this, isFavorite ->
                buttonFavorite.setText(Boolean.TRUE.equals(isFavorite)
                        ? R.string.remove_favorite
                        : R.string.add_favorite)
        );
        viewModel.getMessage().observe(this, message -> {
            if ("login_required".equals(message)) {
                Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.toggleFavorite();
            }
        });

        viewModel.load(getIntent().getStringExtra(EXTRA_BREED_ID));
    }

    private void bindBreed(
            Breed breed,
            ImageView imageView,
            TextView textViewName,
            TextView textViewDescription
    ) {
        setTitle(breed.getName());
        textViewName.setText(breed.getName());
        textViewDescription.setText(breed.getDescription());
        Glide.with(this)
                .load(breed.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imageView);
    }
}
