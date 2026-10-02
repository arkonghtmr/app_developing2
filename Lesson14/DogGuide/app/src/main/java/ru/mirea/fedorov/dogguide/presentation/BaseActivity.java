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
        View bottomNav = findViewById(R.id.bottomNavigation);

        if (appBar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(appBar, (view, insets) -> {
                Insets status = insets.getInsets(WindowInsetsCompat.Type.statusBars());
                view.setPadding(0, status.top, 0, 0);
                return insets;
            });
        }

        if (bottomNav != null) {
            final int navLeft = bottomNav.getPaddingLeft();
            final int navTop = bottomNav.getPaddingTop();
            final int navRight = bottomNav.getPaddingRight();
            ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (view, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                view.setPadding(navLeft, navTop, navRight, bars.bottom);
                return insets;
            });
        }

        if (content != null) {
            final int left = content.getPaddingLeft();
            final int top = content.getPaddingTop();
            final int right = content.getPaddingRight();
            final int bottom = content.getPaddingBottom();
            ViewCompat.setOnApplyWindowInsetsListener(content, (view, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
                boolean bottomNavVisible = bottomNav != null && bottomNav.getVisibility() == View.VISIBLE;
                int extraBottom = bottomNavVisible ? 0 : Math.max(bars.bottom, ime.bottom);
                view.setPadding(left, top, right, bottom + extraBottom);
                return insets;
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
