package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.mirea.fedorov.dogguide.databinding.FragmentFavoritesBinding;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private BreedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity(),
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(FavoritesViewModel.class);

        binding.recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BreedAdapter(breed -> {
            if (requireActivity() instanceof BreedNavigator) {
                ((BreedNavigator) requireActivity()).openBreedDetails(breed.getId());
            }
        });
        binding.recyclerViewFavorites.setAdapter(adapter);

        viewModel.getFavorites().observe(getViewLifecycleOwner(), favorites -> {
            adapter.setItems(favorites);
            binding.textViewEmpty.setVisibility(
                    favorites == null || favorites.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
