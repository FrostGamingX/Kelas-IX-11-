package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private static final int TYPE_SENDER = 0;
    private static final int TYPE_RECEIVER = 1;

    private Context context;
    private List<Message> messageList;
    private String currentUid;
    private OnMessageLongClickListener listener;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(Message message, int position);
    }

    public MessagesAdapter(Context context, List<Message> messageList, String currentUid, OnMessageLongClickListener listener) {
        this.context = context;
        this.messageList = messageList;
        this.currentUid = currentUid;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = messageList.get(position);
        return msg.senderId.equals(currentUid) ? TYPE_SENDER : TYPE_RECEIVER;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == TYPE_SENDER ? R.layout.item_message_sender : R.layout.item_message_receiver;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = messageList.get(position);
        holder.tvMessage.setText(msg.text);
        // Format timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.tvTimestamp.setText(sdf.format(msg.timestamp));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onMessageLongClick(msg, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}