package com.example.youtube_java.ui.theme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_java.R;

import java.util.List;

public class CategoryTagAdapter extends RecyclerView.Adapter<CategoryTagAdapter.TagViewHolder> {

    private final Context context;
    private final List<String> tagList;
    private int selectedPosition = 1; // "All" is selected by default

    public CategoryTagAdapter(Context context, List<String> tagList) {
        this.context = context;
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        String tagName = tagList.get(position);
        holder.tagText.setText(tagName);

        // Set the selected state
        if (position == selectedPosition) {
            holder.tagText.setBackground(ContextCompat.getDrawable(context, R.drawable.category_tag_selected_background));
            holder.tagText.setTextColor(ContextCompat.getColor(context, R.color.youtube_tag_selected_text));
        } else {
            holder.tagText.setBackground(ContextCompat.getDrawable(context, R.drawable.category_tag_background));
            holder.tagText.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        // Handle click
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Update the UI
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagText;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tag_text);
        }
    }
}