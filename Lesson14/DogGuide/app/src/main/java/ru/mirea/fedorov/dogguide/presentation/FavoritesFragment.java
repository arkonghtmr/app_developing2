package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.fedorov.dogguide.R;

public class FavoritesFragment extends Fragment {
    private FavoritesViewModel viewModel;
    private BreedAdapter adapter;
    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity(),
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(FavoritesViewModel.class);

        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BreedAdapter(breed -> {
            if (requireActivity() instanceof BreedNavigator) {
                ((BreedNavigator) requireActivity()).openBreedDetails(breed.getId());
            }
        });
        recyclerView.setAdapter(adapter);

        viewModel.getFavorites().observe(getViewLifecycleOwner(), favorites -> {
            adapter.setItems(favorites);
            textViewEmpty.setVisibility(
                    favorites == null || favorites.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.load();
    }
}
