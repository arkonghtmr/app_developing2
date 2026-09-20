package ru.mirea.fedorov.dogguide.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.DogGuideApp;
import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.usecases.GetBreedListUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetCurrentUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.LogoutUserUseCase;

public class MainActivity extends BaseActivity {
    private TextView textViewSession;
    private TextView textViewError;
    private ProgressBar progressBar;
    private BreedAdapter adapter;
    private GetCurrentUserUseCase getCurrentUserUseCase;
    private GetBreedListUseCase getBreedListUseCase;
    private LogoutUserUseCase logoutUserUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupChrome(false);

        DogGuideApp app = (DogGuideApp) getApplication();
        getCurrentUserUseCase = new GetCurrentUserUseCase(app.getUserRepository());
        getBreedListUseCase = new GetBreedListUseCase(app.getBreedRepository());
        logoutUserUseCase = new LogoutUserUseCase(app.getUserRepository());

        textViewSession = findViewById(R.id.textViewSession);
        textViewError = findViewById(R.id.textViewError);
        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBreeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BreedAdapter(breed -> {
            Intent intent = new Intent(MainActivity.this, BreedDetailsActivity.class);
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_ID, breed.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        loadBreeds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private void loadBreeds() {
        progressBar.setVisibility(View.VISIBLE);
        textViewError.setVisibility(View.GONE);
        executor.execute(() -> {
            List<Breed> breeds = getBreedListUseCase.execute();
            runOnUiThread(() -> {
                if (isDestroyed()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                adapter.submitList(breeds);
                if (breeds.isEmpty()) {
                    textViewError.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSession();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedIn = getCurrentUserUseCase.execute() != null;
        menu.findItem(R.id.action_login).setVisible(!loggedIn);
        menu.findItem(R.id.action_logout).setVisible(loggedIn);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        if (id == R.id.action_logout) {
            logoutUserUseCase.execute();
            refreshSession();
            invalidateOptionsMenu();
            return true;
        }
        if (id == R.id.action_favorites) {
            if (getCurrentUserUseCase.execute() == null) {
                Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }
        if (id == R.id.action_recognize) {
            startActivity(new Intent(this, RecognizeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshSession() {
        User user = getCurrentUserUseCase.execute();
        if (user == null) {
            textViewSession.setText("Режим: Гость");
        } else {
            textViewSession.setText("Режим: Пользователь (" + user.getLogin() + ")");
        }
    }
}
