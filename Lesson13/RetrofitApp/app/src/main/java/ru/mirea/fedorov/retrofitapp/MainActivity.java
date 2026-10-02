package ru.mirea.fedorov.retrofitapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private ApiService apiService;
    private ProgressBar progressBar;
    private TextView textViewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemInsets();

        textViewMode = findViewById(R.id.textViewMode);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textViewMode.setText(R.string.picasso_crop);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Call<List<Todo>> call = apiService.getTodos();
        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Todo>> call,
                    @NonNull Response<List<Todo>> response
            ) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Todo> todos = response.body();
                    todoAdapter = new TodoAdapter(MainActivity.this, todos, apiService);
                    recyclerView.setAdapter(todoAdapter);
                } else {
                    Log.e(TAG, "onResponse: " + response.code());
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.http_error, response.code()),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Todo>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(
                        getApplicationContext(),
                        t.getMessage() == null ? getString(R.string.load_error) : t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (todoAdapter == null) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_crop) {
            todoAdapter.setPicassoMode(TodoAdapter.MODE_CROP);
            textViewMode.setText(R.string.picasso_crop);
            return true;
        }
        if (id == R.id.action_inside) {
            todoAdapter.setPicassoMode(TodoAdapter.MODE_INSIDE);
            textViewMode.setText(R.string.picasso_inside);
            return true;
        }
        if (id == R.id.action_resize) {
            todoAdapter.setPicassoMode(TodoAdapter.MODE_RESIZE);
            textViewMode.setText(R.string.picasso_resize);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applySystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View appBar = findViewById(R.id.appBarLayout);
        View content = findViewById(R.id.contentContainer);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (view, insets) -> {
            Insets status = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            view.setPadding(0, status.top, 0, 0);
            return insets;
        });
        final int left = content.getPaddingLeft();
        final int top = content.getPaddingTop();
        final int right = content.getPaddingRight();
        final int bottom = content.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(content, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
            view.setPadding(left, top, right, bottom + bars.bottom);
            return insets;
        });
    }
}
