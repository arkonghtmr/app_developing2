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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.User;

public class MainActivity extends BaseActivity {
    private BreedListViewModel viewModel;
    private TextView textViewSession;
    private TextView textViewError;
    private ProgressBar progressBar;
    private BreedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupChrome(false);

        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(getApplication())
        ).get(BreedListViewModel.class);

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

        viewModel.getCatalog().observe(this, breeds -> {
            adapter.submitList(breeds);
            textViewError.setVisibility(
                    breeds == null || breeds.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
        viewModel.getLoading().observe(this, loading ->
                progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE)
        );
        viewModel.getSession().observe(this, this::renderSession);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshSession();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedIn = viewModel.getSession().getValue() != null;
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
            viewModel.logout();
            invalidateOptionsMenu();
            return true;
        }
        if (id == R.id.action_favorites) {
            if (viewModel.getSession().getValue() == null) {
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

    private void renderSession(User user) {
        if (user == null) {
            textViewSession.setText("Режим: Гость");
        } else {
            textViewSession.setText("Режим: Пользователь (" + user.getLogin() + ")");
        }
        invalidateOptionsMenu();
    }
}
