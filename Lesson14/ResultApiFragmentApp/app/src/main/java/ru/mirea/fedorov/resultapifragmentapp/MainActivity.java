package ru.mirea.fedorov.resultapifragmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity implements FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemInsets();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, DataFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void sendResult(String message) {
        Toast.makeText(this, getString(R.string.received_prefix, message), Toast.LENGTH_SHORT).show();
    }

    private void applySystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View appBar = findViewById(R.id.appBarLayout);
        View content = findViewById(R.id.fragment_container_view);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (view, insets) -> {
            Insets status = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            view.setPadding(0, status.top, 0, 0);
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(content, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            view.setPadding(0, 0, 0, Math.max(bars.bottom, ime.bottom));
            return insets;
        });
    }
}
