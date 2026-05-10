package com.example.selfcareapp.ui.journal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.JournalEntryEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for the Journal RecyclerView.
 * Manages the binding of journal entry data to UI components, including
 * date formatting, mood-to-emoji mapping, and navigation to the edit screen.
 */
public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<JournalEntryEntity> entries;

    /**
     * Updates the underlying data source for the adapter.
     */
    public void setEntries(List<JournalEntryEntity> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journal_entry, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntryEntity entry = entries.get(position);

        holder.tvContent.setText(entry.content);

        // Format and display the entry date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd.", Locale.getDefault());
        String dateString = sdf.format(entry.date);
        holder.tvDate.setText(dateString);

        // Map the stored mood string to its corresponding drawable resource
        // Initialize with a default value to satisfy the compiler
        int displayMoodIcon = R.drawable.sentiment_neutral_24px;

        // Use entry.mood (the actual field name in your Entity class)
        String selectedMoodIcon = (entry.mood != null) ? entry.mood : "Neutral";

        switch (selectedMoodIcon) {
            case "Sad":
                displayMoodIcon = R.drawable.sentiment_sad_24px;
                break;
            case "Neutral":
                displayMoodIcon = R.drawable.sentiment_neutral_24px;
                break;
            case "Happy":
                displayMoodIcon = R.drawable.sentiment_satisfied_24px;
                break;
            case "Great":
                displayMoodIcon = R.drawable.sentiment_very_satisfied_24px;
                break;
        }

        holder.ivMoodIcon.setImageResource(displayMoodIcon);

        // Navigates to the entry details/edit screen when an item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), JournalAddEntryActivity.class);
            intent.putExtra("ENTRY_ID", entry.id);
            intent.putExtra("ENTRY_CONTENT", entry.content);
            intent.putExtra("ENTRY_MOOD", entry.mood);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return entries == null ? 0 : entries.size();
    }

    /**
     * Retrieves a specific entry from the current list based on its position.
     * Useful for swipe-to-delete operations.
     */
    public JournalEntryEntity getEntryAt(int position) {
        return entries.get(position);
    }

    /**
     * ViewHolder class for caching UI references to improve scrolling performance.
     */
    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        ImageView ivMoodIcon;
        TextView tvContent;

        public JournalViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvEntryDate);
            ivMoodIcon = itemView.findViewById(R.id.ivEntryMood);
            tvContent = itemView.findViewById(R.id.tvEntryContent);
        }
    }
}