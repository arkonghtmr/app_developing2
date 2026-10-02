package ru.mirea.fedorov.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nameView = view.findViewById(R.id.textViewCountryName);
        TextView capitalView = view.findViewById(R.id.textViewCapital);
        TextView descriptionView = view.findViewById(R.id.textViewDescription);

        ShareViewModel viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), country -> {
            if (country == null) {
                nameView.setText(R.string.details_placeholder);
                capitalView.setVisibility(View.GONE);
                descriptionView.setText("");
                return;
            }
            nameView.setText(country.getName());
            capitalView.setVisibility(View.VISIBLE);
            capitalView.setText(getString(R.string.country_capital, country.getCapital()));
            descriptionView.setText(country.getDescription());
        });
    }
}
