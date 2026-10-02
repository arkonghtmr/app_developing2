package ru.mirea.fedorov.fragmentapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_STUDENT_NUMBER = "my_number_student";
    public static final int STUDENT_NUMBER = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemInsets();

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_STUDENT_NUMBER, STUDENT_NUMBER);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, BlankFragment.class, bundle)
                    .commit();
        }
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
