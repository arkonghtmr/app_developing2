package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.mirea.fedorov.dogguide.DogGuideApp;
import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.usecases.LoginUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RegisterUserUseCase;

public class LoginActivity extends BaseActivity {
    private EditText editTextLogin;
    private EditText editTextPassword;
    private LoginUserUseCase loginUserUseCase;
    private RegisterUserUseCase registerUserUseCase;

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

    private void submit(boolean login) {
        String name = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (name.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show();
            return;
        }
        User user = login
                ? loginUserUseCase.execute(name, password)
                : registerUserUseCase.execute(name, password);
        if (user == null) {
            Toast.makeText(
                    this,
                    login ? R.string.wrong_credentials : R.string.user_exists,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        finish();
    }
}
