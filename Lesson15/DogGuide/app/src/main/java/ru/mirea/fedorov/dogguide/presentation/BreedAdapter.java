package ru.mirea.fedorov.dogguide.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;

public class BreedAdapter extends RecyclerView.Adapter<BreedAdapter.BreedViewHolder> {
    public interface OnBreedClickListener {
        void onBreedClick(Breed breed);
    }

    private final List<Breed> breeds = new ArrayList<>();
    private final OnBreedClickListener listener;

    public BreedAdapter(OnBreedClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Breed> items) {
        setItems(items);
    }

    public void setItems(List<Breed> items) {
        breeds.clear();
        if (items != null) {
            breeds.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BreedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_breed, parent, false);
        return new BreedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreedViewHolder holder, int position) {
        holder.bind(breeds.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return breeds.size();
    }

    static class BreedViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewBreed;
        private final TextView textViewBreedName;

        BreedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBreed = itemView.findViewById(R.id.imageViewBreed);
            textViewBreedName = itemView.findViewById(R.id.textViewBreedName);
        }

        void bind(Breed breed, OnBreedClickListener listener) {
            textViewBreedName.setText(breed.getName());
            Picasso.get()
                    .load(breed.getImageUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imageViewBreed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onBreedClick(breed);
                }
            });
        }
    }
}
