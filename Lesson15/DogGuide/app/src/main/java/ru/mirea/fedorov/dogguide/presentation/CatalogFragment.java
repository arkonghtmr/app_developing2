package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import ru.mirea.fedorov.dogguide.databinding.FragmentCatalogBinding;
import ru.mirea.fedorov.dogguide.domain.models.User;

public class CatalogFragment extends Fragment {
    private FragmentCatalogBinding binding;
    private BreedListViewModel viewModel;
    private BreedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity(),
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(BreedListViewModel.class);

        adapter = new BreedAdapter(breed -> {
            if (requireActivity() instanceof BreedNavigator) {
                ((BreedNavigator) requireActivity()).openBreedDetails(breed.getId());
            }
        });
        binding.recyclerViewBreeds.setAdapter(adapter);
        applyLayoutManager(0);

        viewModel.getCatalog().observe(getViewLifecycleOwner(), breeds -> {
            adapter.setItems(breeds);
            boolean empty = breeds == null || breeds.isEmpty();
            boolean loadingNow = Boolean.TRUE.equals(viewModel.getLoading().getValue());
            binding.textViewError.setVisibility(empty && !loadingNow ? View.VISIBLE : View.GONE);
        });
        viewModel.getLayoutMode().observe(getViewLifecycleOwner(), this::applyLayoutManager);
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
            if (Boolean.TRUE.equals(loading)) {
                binding.textViewError.setVisibility(View.GONE);
            }
        });
        viewModel.getSession().observe(getViewLifecycleOwner(), this::renderSession);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshSession();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void applyLayoutManager(Integer mode) {
        int value = mode == null ? 0 : mode;
        RecyclerView.LayoutManager layoutManager;
        if (value == 1) {
            layoutManager = new GridLayoutManager(requireContext(), 2);
        } else if (value == 2) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        }
        binding.recyclerViewBreeds.setLayoutManager(layoutManager);
        while (binding.recyclerViewBreeds.getItemDecorationCount() > 0) {
            binding.recyclerViewBreeds.removeItemDecorationAt(0);
        }
        if (value == 0) {
            binding.recyclerViewBreeds.addItemDecoration(
                    new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            );
        }
    }

    private void renderSession(User user) {
        if (user == null) {
            binding.textViewSession.setText("Режим: Гость");
        } else {
            binding.textViewSession.setText("Режим: Пользователь (" + user.getLogin() + ")");
        }
    }
}
