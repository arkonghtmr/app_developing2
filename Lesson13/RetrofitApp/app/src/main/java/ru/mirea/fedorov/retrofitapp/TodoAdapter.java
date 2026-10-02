package ru.mirea.fedorov.retrofitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    public static final int MODE_CROP = 0;
    public static final int MODE_INSIDE = 1;
    public static final int MODE_RESIZE = 2;

    private static final String TAG = "TodoAdapter";

    private final LayoutInflater layoutInflater;
    private final List<Todo> todos;
    private final ApiService apiService;
    private int picassoMode = MODE_CROP;

    public TodoAdapter(Context context, List<Todo> todoList, ApiService apiService) {
        this.layoutInflater = LayoutInflater.from(context);
        this.todos = todoList;
        this.apiService = apiService;
    }

    public void setPicassoMode(int mode) {
        this.picassoMode = mode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.textViewTitle.setText(todo.getTitle());
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(Boolean.TRUE.equals(todo.getCompleted()));
        holder.checkBoxCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todo.setCompleted(isChecked);
                apiService.updateTodo(todo.getId(), todo).enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "onResponse: " + response.code());
                            Toast.makeText(
                                    buttonView.getContext(),
                                    R.string.update_error,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(
                                buttonView.getContext(),
                                R.string.update_error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }
        });
        applyPicasso(holder.imageViewTodo, todo);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    private void applyPicasso(ImageView imageView, Todo todo) {
        int id = todo.getId() == null ? 0 : todo.getId();
        String url = "https://picsum.photos/seed/todo" + id + "/200/200";
        RequestCreator request = Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image);
        if (picassoMode == MODE_INSIDE) {
            request.fit().centerInside();
        } else if (picassoMode == MODE_RESIZE) {
            request.resize(100, 100).centerCrop();
        } else {
            request.fit().centerCrop();
        }
        request.into(imageView);
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewTodo;
        final TextView textViewTitle;
        final CheckBox checkBoxCompleted;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewTodo = itemView.findViewById(R.id.imageViewTodo);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        }
    }
}
