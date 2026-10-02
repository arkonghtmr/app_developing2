package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;

public class BreedDetailsFragment extends Fragment {
    public static final String ARG_BREED_ID = "breed_id";

    private BreedDetailsViewModel viewModel;
    private Button buttonFavorite;

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
        return inflater.inflate(R.layout.fragment_breed_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(BreedDetailsViewModel.class);

        ImageView imageView = view.findViewById(R.id.imageViewBreed);
        TextView textViewName = view.findViewById(R.id.textViewBreedName);
        TextView textViewDescription = view.findViewById(R.id.textViewBreedDescription);
        buttonFavorite = view.findViewById(R.id.buttonFavorite);

        viewModel.getBreed().observe(getViewLifecycleOwner(), breed -> {
            if (breed == null) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return;
            }
            bindBreed(breed, imageView, textViewName, textViewDescription);
        });
        viewModel.getFavorite().observe(getViewLifecycleOwner(), isFavorite ->
                buttonFavorite.setText(Boolean.TRUE.equals(isFavorite)
                        ? R.string.remove_favorite
                        : R.string.add_favorite)
        );
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if ("login_required".equals(message)) {
                Toast.makeText(requireContext(), R.string.login_required, Toast.LENGTH_SHORT).show();
            }
        });
        buttonFavorite.setOnClickListener(v -> viewModel.toggleFavorite());
        viewModel.load(requireArguments().getString(ARG_BREED_ID));
    }

    private void bindBreed(
            Breed breed,
            ImageView imageView,
            TextView textViewName,
            TextView textViewDescription
    ) {
        requireActivity().setTitle(breed.getName());
        textViewName.setText(breed.getName());
        textViewDescription.setText(breed.getDescription());
        Picasso.get()
                .load(breed.getImageUrl())
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imageView);
    }
}
