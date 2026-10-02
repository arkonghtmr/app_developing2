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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class HeaderFragment extends Fragment {
    private static final List<Country> COUNTRIES = Arrays.asList(
            new Country(
                    "Россия",
                    "Москва",
                    "Самая большая страна мира. Расположена в Восточной Европе и Северной Азии."
            ),
            new Country(
                    "Франция",
                    "Париж",
                    "Государство в Западной Европе. Известна искусством, кухней и Эйфелевой башней."
            ),
            new Country(
                    "Германия",
                    "Берлин",
                    "Федеративная республика в Центральной Европе. Крупнейшая экономика ЕС."
            ),
            new Country(
                    "Италия",
                    "Рим",
                    "Страна на Апеннинском полуострове. Колыбель Римской империи и Ренессанса."
            ),
            new Country(
                    "Япония",
                    "Токио",
                    "Островное государство в Восточной Азии. Сочетает традиции и высокие технологии."
            ),
            new Country(
                    "Бразилия",
                    "Бразилиа",
                    "Крупнейшая страна Южной Америки. Амазония, карнавал и футбол."
            ),
            new Country(
                    "Канада",
                    "Оттава",
                    "Вторая по площади страна мира. Официальные языки — английский и французский."
            ),
            new Country(
                    "Индия",
                    "Нью-Дели",
                    "Вторая по населению страна мира. Тадж-Махал, Гималаи и разнообразие культур."
            )
    );

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShareViewModel viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCountries);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new CountryAdapter(COUNTRIES, country -> {
            viewModel.selectItem(country);
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_details, DetailsFragment.class, null, "details")
                    .addToBackStack("details")
                    .commit();
        }));
    }

    private static class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.Holder> {
        interface OnCountryClickListener {
            void onCountryClick(Country country);
        }

        private final List<Country> items;
        private final OnCountryClickListener listener;

        CountryAdapter(List<Country> items, OnCountryClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_country, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Country country = items.get(position);
            holder.title.setText(country.getName());
            holder.itemView.setOnClickListener(v -> listener.onCountryClick(country));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final TextView title;

            Holder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.textViewCountry);
            }
        }
    }
}
