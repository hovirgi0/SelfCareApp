package com.example.selfcareapp.ui.journal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.JournalEntryEntity;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {
    private List<JournalEntryEntity> entries;

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
    }

    @Override
    public int getItemCount() {
        return entries == null ? 0 : entries.size();
    }

    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        View viewMoodColor;

        public JournalViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvEntryDate);
            viewMoodColor = itemView.findViewById(R.id.viewMoodColor);
        }
    }
}
