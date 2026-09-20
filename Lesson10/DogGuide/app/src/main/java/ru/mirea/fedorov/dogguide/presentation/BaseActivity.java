package ru.mirea.fedorov.dogguide.presentation;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import ru.mirea.fedorov.dogguide.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupChrome(boolean showUp) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showUp);
        }

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
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int extraBottom = Math.max(bars.bottom, ime.bottom);
            view.setPadding(left, top, right, bottom + extraBottom);
            return insets;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
