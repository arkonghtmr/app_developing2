package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.User;

public class CatalogFragment extends Fragment {
    private BreedListViewModel viewModel;
    private TextView textViewSession;
    private TextView textViewError;
    private ProgressBar progressBar;
    private BreedAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity(),
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(BreedListViewModel.class);

        textViewSession = view.findViewById(R.id.textViewSession);
        textViewError = view.findViewById(R.id.textViewError);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerViewBreeds);
        adapter = new BreedAdapter(breed -> {
            if (requireActivity() instanceof BreedNavigator) {
                ((BreedNavigator) requireActivity()).openBreedDetails(breed.getId());
            }
        });
        recyclerView.setAdapter(adapter);
        applyLayoutManager(0);

        viewModel.getCatalog().observe(getViewLifecycleOwner(), breeds -> {
            adapter.setItems(breeds);
            boolean empty = breeds == null || breeds.isEmpty();
            boolean loadingNow = Boolean.TRUE.equals(viewModel.getLoading().getValue());
            textViewError.setVisibility(empty && !loadingNow ? View.VISIBLE : View.GONE);
        });
        viewModel.getLayoutMode().observe(getViewLifecycleOwner(), this::applyLayoutManager);
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
            if (Boolean.TRUE.equals(loading)) {
                textViewError.setVisibility(View.GONE);
            }
        });
        viewModel.getSession().observe(getViewLifecycleOwner(), this::renderSession);
    }

    public void setLayoutMode(int mode) {
        if (viewModel != null) {
            viewModel.setLayoutMode(mode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshSession();
        }
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
        recyclerView.setLayoutManager(layoutManager);
        while (recyclerView.getItemDecorationCount() > 0) {
            recyclerView.removeItemDecorationAt(0);
        }
        if (value == 0) {
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            );
        }
    }

    private void renderSession(User user) {
        if (user == null) {
            textViewSession.setText("Режим: Гость");
        } else {
            textViewSession.setText("Режим: Пользователь (" + user.getLogin() + ")");
        }
    }
}
