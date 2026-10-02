package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.databinding.FragmentProfileBinding;
import ru.mirea.fedorov.dogguide.domain.models.User;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private BreedListViewModel sessionViewModel;
    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DogGuideViewModelFactory factory = new DogGuideViewModelFactory(requireActivity().getApplication());
        sessionViewModel = new ViewModelProvider(requireActivity(), factory).get(BreedListViewModel.class);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        sessionViewModel.getSession().observe(getViewLifecycleOwner(), this::renderSession);
        loginViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean enabled = !Boolean.TRUE.equals(loading);
            binding.buttonLogin.setEnabled(enabled);
            binding.buttonRegister.setEnabled(enabled);
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

        binding.buttonLogin.setOnClickListener(v -> loginViewModel.login(
                binding.editTextLogin.getText().toString(),
                binding.editTextPassword.getText().toString()
        ));
        binding.buttonRegister.setOnClickListener(v -> loginViewModel.register(
                binding.editTextLogin.getText().toString(),
                binding.editTextPassword.getText().toString()
        ));
        binding.buttonLogout.setOnClickListener(v -> sessionViewModel.logout());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionViewModel != null) {
            sessionViewModel.refreshSession();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void renderSession(User user) {
        if (user == null) {
            binding.textViewProfileStatus.setText(R.string.profile_guest);
            binding.layoutGuest.setVisibility(View.VISIBLE);
            binding.buttonLogout.setVisibility(View.GONE);
            return;
        }
        binding.textViewProfileStatus.setText(getString(R.string.profile_user, user.getLogin()));
        binding.layoutGuest.setVisibility(View.GONE);
        binding.buttonLogout.setVisibility(View.VISIBLE);
    }
}
