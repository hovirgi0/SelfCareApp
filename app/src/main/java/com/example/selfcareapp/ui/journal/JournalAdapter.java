package com.example.selfcareapp.ui.journal;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.ui.todo.ToDoAddEditActivity;

import java.text.SimpleDateFormat;
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

        //Szerkesztés gomb kezelése
        holder.tvContent.setText(entry.content);

        //Adatok formázása és beállítása
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(entry.date);
        holder.tvDate.setText(dateString);

        //Mood beállítása: a Stringként elmentett mood konvertálása emojivá
        String displayMoodEmoji = "\uD83D\uDE10";
        String selectedMoodEmoji = (entry.mood != null) ? entry.mood : "Neutral"; //default mood

        switch (selectedMoodEmoji) {
            case "Sad":
                displayMoodEmoji = "\uD83D\uDE22";
                break;
            case "Neutral":
                displayMoodEmoji = "\uD83D\uDE10";
                break;
            case "Happy":
                displayMoodEmoji = "\uD83D\uDE0A";
                break;
            case "Great":
                displayMoodEmoji = "\uD83D\uDE03";
                break;
        }

        holder.tvMoodEmoji.setText(displayMoodEmoji);
        //Szerkesztés gomb kezelése - itt nincs szerkesztés gomb mint a todo-nál így a bejegyzés kártyára tesszük rá tesszük rá a kattintást
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), JournalAddEntryActivity.class);

            //Feladat átküldéséhez
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

    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvMoodEmoji;
        TextView tvContent;


        public JournalViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvEntryDate);
            tvMoodEmoji = itemView.findViewById(R.id.tvEntryMood); //puts the correct emoji in the preview of an entry
            tvContent = itemView.findViewById(R.id.tvEntryContent);
        }
    }

    //Swipe-to-Delete törlés (alternatív megoldás - egyszerű, modern)
    public JournalEntryEntity getEntryAt(int position) {
        return entries.get(position);
    }

}
