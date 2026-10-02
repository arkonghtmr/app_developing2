package ru.mirea.fedorov.scrollviewapp;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigInteger;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_COUNT = 100;
    private static final int RATIO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemInsets();

        LinearLayout wrapper = findViewById(R.id.wrapper);
        BigInteger value = BigInteger.ONE;
        for (int i = 1; i <= ITEM_COUNT; i++) {
            View view = getLayoutInflater().inflate(R.layout.item, wrapper, false);
            TextView text = view.findViewById(R.id.textView);
            text.setText(String.format(Locale.US, "a%d = %s", i, value));
            wrapper.addView(view);
            value = value.multiply(BigInteger.valueOf(RATIO));
        }
    }

    private void applySystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top + actionBarHeight(), bars.right, bars.bottom);
            return insets;
        });
    }

    private int actionBarHeight() {
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return 0;
    }
}
