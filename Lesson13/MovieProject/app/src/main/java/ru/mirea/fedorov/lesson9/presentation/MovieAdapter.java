package ru.mirea.fedorov.lesson9.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.fedorov.lesson9.R;
import ru.mirea.fedorov.lesson9.domain.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.SimpleMovieViewHolder> {
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private List<Movie> itemList = new ArrayList<>();
    private final OnMovieClickListener listener;

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Movie> items) {
        this.itemList = items == null ? new ArrayList<>() : items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimpleMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_view, parent, false);
        return new SimpleMovieViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleMovieViewHolder holder, int position) {
        holder.bind(itemList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class SimpleMovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieNameView;
        private final TextView movieYearView;

        public SimpleMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.movieNameView = itemView.findViewById(R.id.textViewMovieName);
            this.movieYearView = itemView.findViewById(R.id.textViewMovieYear);
        }

        public void bind(Movie item, OnMovieClickListener listener) {
            movieNameView.setText(item.getName());
            movieYearView.setText("Year: " + item.getYear());
            itemView.setOnClickListener(v -> listener.onMovieClick(item));
        }
    }
}
