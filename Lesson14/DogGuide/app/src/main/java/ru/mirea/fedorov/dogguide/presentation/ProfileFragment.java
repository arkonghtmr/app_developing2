package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.User;

public class ProfileFragment extends Fragment {
    private BreedListViewModel sessionViewModel;
    private LoginViewModel loginViewModel;
    private TextView textViewProfileStatus;
    private View layoutGuest;
    private View buttonLogout;
    private EditText editTextLogin;
    private EditText editTextPassword;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DogGuideViewModelFactory factory = new DogGuideViewModelFactory(requireActivity().getApplication());
        sessionViewModel = new ViewModelProvider(requireActivity(), factory).get(BreedListViewModel.class);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        textViewProfileStatus = view.findViewById(R.id.textViewProfileStatus);
        layoutGuest = view.findViewById(R.id.layoutGuest);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        editTextLogin = view.findViewById(R.id.editTextLogin);
        editTextPassword = view.findViewById(R.id.editTextPassword);

        sessionViewModel.getSession().observe(getViewLifecycleOwner(), this::renderSession);
        loginViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean enabled = !Boolean.TRUE.equals(loading);
            view.findViewById(R.id.buttonLogin).setEnabled(enabled);
            view.findViewById(R.id.buttonRegister).setEnabled(enabled);
        });
        loginViewModel.getOutcome().observe(getViewLifecycleOwner(), outcome -> {
            if (outcome == null) {
                return;
            }
            if (!outcome.isSuccess()) {
                Toast.makeText(requireContext(), outcome.getErrorMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            sessionViewModel.refreshSession();
        });

        view.findViewById(R.id.buttonLogin).setOnClickListener(v -> loginViewModel.login(
                editTextLogin.getText().toString(),
                editTextPassword.getText().toString()
        ));
        view.findViewById(R.id.buttonRegister).setOnClickListener(v -> loginViewModel.register(
                editTextLogin.getText().toString(),
                editTextPassword.getText().toString()
        ));
        buttonLogout.setOnClickListener(v -> sessionViewModel.logout());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionViewModel != null) {
            sessionViewModel.refreshSession();
        }
    }

    private void renderSession(User user) {
        if (user == null) {
            textViewProfileStatus.setText(R.string.profile_guest);
            layoutGuest.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.GONE);
            return;
        }
        textViewProfileStatus.setText(getString(R.string.profile_user, user.getLogin()));
        layoutGuest.setVisibility(View.GONE);
        buttonLogout.setVisibility(View.VISIBLE);
    }
}
