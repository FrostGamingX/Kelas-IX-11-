package com.example.socialapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;
import com.example.socialapp.models.Story;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {
    private Context context;
    private List<Story> storyList;

    public StoriesAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storyList.get(position);
        Glide.with(context).load(story.getImageUrl()).into(holder.ivStoryImage);
        holder.tvTimestamp.setText(getTimeAgo(story.getTimestamp()));

        // Load username
        FirebaseHelper.getDatabase().getReference("users/" + story.getUserUid() + "/username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.tvStoryUsername.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    private String getTimeAgo(long timestamp) {
        // Implement logic to convert timestamp to "2h ago" etc.
        return "Time ago"; // Placeholder
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStoryImage;
        TextView tvStoryUsername, tvTimestamp;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStoryImage = itemView.findViewById(R.id.iv_story_image);
            tvStoryUsername = itemView.findViewById(R.id.tv_story_username);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}