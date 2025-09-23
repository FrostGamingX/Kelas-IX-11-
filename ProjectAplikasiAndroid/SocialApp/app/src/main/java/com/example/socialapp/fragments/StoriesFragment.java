package com.example.socialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;
import com.example.socialapp.adapters.StoriesAdapter;
import com.example.socialapp.customviews.CircularImageView;
import com.example.socialapp.models.Story;
import com.example.socialapp.models.User;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoriesFragment extends Fragment {
    private LinearLayout llStoriesContainer;
    private RecyclerView rvStoriesDetails;
    private StoriesAdapter adapter;
    private List<Story> storyList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories, container, false);
        llStoriesContainer = view.findViewById(R.id.ll_stories_container);
        rvStoriesDetails = view.findViewById(R.id.rv_stories_details);
        rvStoriesDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StoriesAdapter(getContext(), storyList);
        rvStoriesDetails.setAdapter(adapter);

        // Load users for stories horizontal scroll
        FirebaseHelper.getDatabase().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                llStoriesContainer.removeAllViews();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && user.getProfilePicUrl() != null) {
                        CircularImageView iv = new CircularImageView(getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
                        params.setMargins(8, 0, 8, 0);
                        iv.setLayoutParams(params);
                        Glide.with(getContext()).load(user.getProfilePicUrl()).circleCrop().into(iv);
                        iv.setOnClickListener(v -> loadStoriesForUser(userSnapshot.getKey()));
                        llStoriesContainer.addView(iv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }

    private void loadStoriesForUser(String userId) {
        FirebaseHelper.getDatabase().getReference("stories/" + userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyList.clear();
                for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                    Story story = storySnapshot.getValue(Story.class);
                    if (story != null && System.currentTimeMillis() - story.getTimestamp() < 86400000) { // 24 hours
                        storyList.add(story);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}