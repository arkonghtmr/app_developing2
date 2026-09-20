package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ru.mirea.fedorov.dogguide.DogGuideApp;
import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.usecases.AddBreedToFavoritesUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetBreedDetailsUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetCurrentUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.IsBreedFavoriteUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RemoveBreedFromFavoritesUseCase;

public class BreedDetailsActivity extends BaseActivity {
    public static final String EXTRA_BREED_ID = "breed_id";

    private Breed breed;
    private Button buttonFavorite;
    private GetCurrentUserUseCase getCurrentUserUseCase;
    private IsBreedFavoriteUseCase isBreedFavoriteUseCase;
    private AddBreedToFavoritesUseCase addBreedToFavoritesUseCase;
    private RemoveBreedFromFavoritesUseCase removeBreedFromFavoritesUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_details);
        setupChrome(true);

        DogGuideApp app = (DogGuideApp) getApplication();
        GetBreedDetailsUseCase getBreedDetailsUseCase =
                new GetBreedDetailsUseCase(app.getBreedRepository());
        getCurrentUserUseCase = new GetCurrentUserUseCase(app.getUserRepository());
        isBreedFavoriteUseCase = new IsBreedFavoriteUseCase(
                app.getFavoriteRepository(),
                app.getUserRepository()
        );
        addBreedToFavoritesUseCase = new AddBreedToFavoritesUseCase(
                app.getFavoriteRepository(),
                app.getUserRepository()
        );
        removeBreedFromFavoritesUseCase = new RemoveBreedFromFavoritesUseCase(
                app.getFavoriteRepository(),
                app.getUserRepository()
        );

        String breedId = getIntent().getStringExtra(EXTRA_BREED_ID);
        breed = getBreedDetailsUseCase.execute(breedId);
        if (breed == null) {
            finish();
            return;
        }

        setTitle(breed.getName());
        ImageView imageView = findViewById(R.id.imageViewBreed);
        TextView textViewName = findViewById(R.id.textViewBreedName);
        TextView textViewDescription = findViewById(R.id.textViewBreedDescription);
        buttonFavorite = findViewById(R.id.buttonFavorite);

        textViewName.setText(breed.getName());
        textViewDescription.setText(breed.getDescription());
        Glide.with(this)
                .load(breed.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imageView);

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteClicked();
            }
        });
        refreshFavoriteButton();
    }

    private void onFavoriteClicked() {
        User user = getCurrentUserUseCase.execute();
        if (user == null) {
            Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isBreedFavoriteUseCase.execute(breed.getId())) {
            removeBreedFromFavoritesUseCase.execute(breed.getId());
        } else {
            addBreedToFavoritesUseCase.execute(breed);
        }
        refreshFavoriteButton();
    }

    private void refreshFavoriteButton() {
        if (isBreedFavoriteUseCase.execute(breed.getId())) {
            buttonFavorite.setText(R.string.remove_favorite);
        } else {
            buttonFavorite.setText(R.string.add_favorite);
        }
    }
}
