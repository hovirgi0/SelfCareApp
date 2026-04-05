package com.example.selfcareapp.ui.chat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_bubble, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        holder.tvBubble.setText(msg.getText());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tvBubble.getLayoutParams();

        // Felhasználó üzenete jobbra, bot üzenete balra
        if (msg.getType() == ChatMessage.TYPE_USER) {
            params.gravity = Gravity.END;
            holder.llBubble.setGravity(Gravity.END);
        } else {
            params.gravity = Gravity.START;
            holder.llBubble.setGravity(Gravity.START);
        }
        holder.tvBubble.setLayoutParams(params);
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvBubble;
        LinearLayout llBubble;

        ChatViewHolder(View itemView) {
            super(itemView);
            tvBubble = itemView.findViewById(R.id.tvChatBubble);
            llBubble = itemView.findViewById(R.id.llChatBubble);
        }
    }
}

