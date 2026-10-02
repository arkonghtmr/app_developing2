package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.databinding.FragmentBreedDetailsBinding;
import ru.mirea.fedorov.dogguide.domain.models.Breed;

public class BreedDetailsFragment extends Fragment {
    public static final String ARG_BREED_ID = "breed_id";

    private FragmentBreedDetailsBinding binding;
    private BreedDetailsViewModel viewModel;

    public static BreedDetailsFragment newInstance(String breedId) {
        Bundle args = new Bundle();
        args.putString(ARG_BREED_ID, breedId);
        BreedDetailsFragment fragment = new BreedDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentBreedDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(BreedDetailsViewModel.class);

        viewModel.getBreed().observe(getViewLifecycleOwner(), breed -> {
            if (breed == null) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return;
            }
            bindBreed(breed);
        });
        viewModel.getFavorite().observe(getViewLifecycleOwner(), isFavorite ->
                binding.buttonFavorite.setText(Boolean.TRUE.equals(isFavorite)
                        ? R.string.remove_favorite
                        : R.string.add_favorite)
        );
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if ("login_required".equals(message)) {
                Toast.makeText(requireContext(), R.string.login_required, Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonFavorite.setOnClickListener(v -> viewModel.toggleFavorite());
        viewModel.load(requireArguments().getString(ARG_BREED_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bindBreed(Breed breed) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(breed.getName());
        }
        binding.textViewBreedName.setText(breed.getName());
        binding.textViewBreedDescription.setText(breed.getDescription());
        Picasso.get()
                .load(breed.getImageUrl())
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.imageViewBreed);
    }
}
