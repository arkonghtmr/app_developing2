package ru.mirea.fedorov.listviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<HistoricalEvent> {
    public EventAdapter(@NonNull Context context, @NonNull List<HistoricalEvent> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageViewEvent);
            holder.titleView = convertView.findViewById(R.id.textViewTitle);
            holder.descriptionView = convertView.findViewById(R.id.textViewDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoricalEvent event = getItem(position);
        if (event != null) {
            holder.imageView.setImageResource(event.getImageResId());
            holder.titleView.setText(event.getTitle());
            holder.descriptionView.setText(event.getDescription());
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
    }
}
