package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.DogGuideApp;
import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.AuthOutcome;
import ru.mirea.fedorov.dogguide.domain.usecases.LoginUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RegisterUserUseCase;

public class LoginActivity extends BaseActivity {
    private EditText editTextLogin;
    private EditText editTextPassword;
    private LoginUserUseCase loginUserUseCase;
    private RegisterUserUseCase registerUserUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupChrome(true);
        setTitle(R.string.login);

        DogGuideApp app = (DogGuideApp) getApplication();
        loginUserUseCase = new LoginUserUseCase(app.getUserRepository());
        registerUserUseCase = new RegisterUserUseCase(app.getUserRepository());

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(true);
            }
        });
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private void submit(boolean login) {
        String name = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (name.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.buttonLogin).setEnabled(false);
        findViewById(R.id.buttonRegister).setEnabled(false);
        executor.execute(() -> {
            AuthOutcome outcome = login
                    ? loginUserUseCase.execute(name, password)
                    : registerUserUseCase.execute(name, password);
            runOnUiThread(() -> {
                if (isDestroyed()) {
                    return;
                }
                findViewById(R.id.buttonLogin).setEnabled(true);
                findViewById(R.id.buttonRegister).setEnabled(true);
                if (!outcome.isSuccess()) {
                    Toast.makeText(
                            LoginActivity.this,
                            outcome.getErrorMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                finish();
            });
        });
    }
}
