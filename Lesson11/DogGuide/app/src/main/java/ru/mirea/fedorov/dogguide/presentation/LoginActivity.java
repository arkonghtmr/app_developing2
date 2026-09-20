package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import ru.mirea.fedorov.dogguide.R;

public class LoginActivity extends BaseActivity {
    private LoginViewModel viewModel;
    private EditText editTextLogin;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupChrome(true);
        setTitle(R.string.login);

        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(getApplication())
        ).get(LoginViewModel.class);

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        viewModel.getLoading().observe(this, loading -> {
            boolean enabled = !Boolean.TRUE.equals(loading);
            findViewById(R.id.buttonLogin).setEnabled(enabled);
            findViewById(R.id.buttonRegister).setEnabled(enabled);
        });
        viewModel.getOutcome().observe(this, outcome -> {
            if (outcome == null) {
                return;
            }
            if (!outcome.isSuccess()) {
                Toast.makeText(this, outcome.getErrorMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            finish();
        });

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.login(
                        editTextLogin.getText().toString(),
                        editTextPassword.getText().toString()
                );
            }
        });
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.register(
                        editTextLogin.getText().toString(),
                        editTextPassword.getText().toString()
                );
            }
        });
    }
}
