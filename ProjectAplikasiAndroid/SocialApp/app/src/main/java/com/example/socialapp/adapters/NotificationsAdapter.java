package com.example.socialapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.fragments.HomeFragment;
import com.example.socialapp.fragments.UserProfileFragment;
import com.example.socialapp.models.Notification;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotifViewHolder> {
    private Context context;
    private List<Notification> notifList;
    private FragmentManager fragmentManager; // Tambahkan untuk navigation

    // Update constructor untuk menerima FragmentManager
    public NotificationsAdapter(Context context, List<Notification> notifList, FragmentManager fragmentManager) {
        this.context = context;
        this.notifList = notifList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        Notification notif = notifList.get(position);
        holder.tvTimestamp.setText(getTimeAgo(notif.getTimestamp()));

        if (!notif.isRead()) {
            holder.tvNotifText.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvNotifText.setTypeface(null, Typeface.NORMAL);
        }

        // Handle different types
        if ("follow".equals(notif.getType())) {
            loadFromUsername(notif, holder);
            // TAMBAHKAN KODE INI DI SINI - untuk navigation ke UserProfileFragment
            holder.itemView.setOnClickListener(v -> {
                if (!notif.isRead()) {
                    notif.setRead(true);
                    FirebaseHelper.getDatabase()
                            .getReference("notifications/" + FirebaseHelper.getCurrentUserUid() + "/" + notif.getNotifId() + "/read")
                            .setValue(true);
                    notifyItemChanged(position);
                }

                // Navigate to UserProfileFragment
                UserProfileFragment fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("target_uid", notif.getFromUid());
                fragment.setArguments(args);

                // Gunakan fragmentManager yang dipass dari constructor
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        } else if ("like".equals(notif.getType()) || "comment".equals(notif.getType())) {
            loadFromUsername(notif, holder);
            holder.itemView.setOnClickListener(v -> {
                if (!notif.isRead()) {
                    notif.setRead(true);
                    FirebaseHelper.getDatabase()
                            .getReference("notifications/" + FirebaseHelper.getCurrentUserUid() + "/" + notif.getNotifId() + "/read")
                            .setValue(true);
                    notifyItemChanged(position);
                }

                // Navigate to specific post (optional - bisa buat PostDetailFragment)
                if (notif.getPostId() != null) {
                    // TODO: Implementasi untuk buka PostDetailFragment dengan postId
                    // Untuk sementara, bisa navigate ke HomeFragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            // Default click handler untuk notifikasi lain
            holder.itemView.setOnClickListener(v -> {
                if (!notif.isRead()) {
                    notif.setRead(true);
                    FirebaseHelper.getDatabase()
                            .getReference("notifications/" + FirebaseHelper.getCurrentUserUid() + "/" + notif.getNotifId() + "/read")
                            .setValue(true);
                    notifyItemChanged(position);
                }
            });
            holder.tvNotifText.setText(notif.getText());
        }
    }

    private void loadFromUsername(Notification notif, NotifViewHolder holder) {
        FirebaseHelper.getDatabase()
                .getReference("users/" + notif.getFromUid() + "/username")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fromUsername = snapshot.getValue(String.class);
                        String displayText = (fromUsername != null ? fromUsername : "Someone") + " " + notif.getText();
                        holder.tvNotifText.setText(displayText);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.tvNotifText.setText("Someone " + notif.getText());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    private String getTimeAgo(long timestamp) {
        // Implement time ago logic (simple placeholder)
        long diff = System.currentTimeMillis() - timestamp;
        if (diff < 60000) return "Just now";
        if (diff < 3600000) return (diff / 60000) + "m ago";
        if (diff < 86400000) return (diff / 3600000) + "h ago";
        return (diff / 86400000) + "d ago";
    }

    static class NotifViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotifIcon;
        TextView tvNotifText, tvTimestamp;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotifIcon = itemView.findViewById(R.id.iv_notif_icon);
            tvNotifText = itemView.findViewById(R.id.tv_notif_text);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}