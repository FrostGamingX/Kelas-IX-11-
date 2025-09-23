package com.example.socialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;
import com.example.socialapp.adapters.PostsAdapter;
import com.example.socialapp.models.Notification;
import com.example.socialapp.models.Post;
import com.example.socialapp.models.User;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {
    private ImageView ivProfilePic, ivCover;
    private TextView tvUsername;
    private Button btnFollowUnfollow;
    private RecyclerView rvUserPosts;
    private PostsAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private String currentUid;
    private String targetUid; // Passed as argument

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ivCover = view.findViewById(R.id.iv_cover);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        tvUsername = view.findViewById(R.id.tv_username);
        btnFollowUnfollow = view.findViewById(R.id.btn_follow_unfollow);
        rvUserPosts = view.findViewById(R.id.rv_my_posts); // Reuse id for simplicity

        currentUid = FirebaseHelper.getCurrentUserUid();
        if (getArguments() != null) {
            targetUid = getArguments().getString("target_uid");
        }
        if (targetUid == null) {
            Toast.makeText(getContext(), "No user ID", Toast.LENGTH_SHORT).show();
            return view;
        }

        rvUserPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PostsAdapter(getContext(), postList);
        rvUserPosts.setAdapter(adapter);

        loadUserData();
        loadUserPosts();
        checkFollowStatus();

        btnFollowUnfollow.setOnClickListener(v -> toggleFollow());

        return view;
    }

    private void loadUserData() {
        FirebaseHelper.getDatabase().getReference("users/" + targetUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvUsername.setText(user.getUsername());
                    Glide.with(getContext()).load(user.getProfilePicUrl()).circleCrop().into(ivProfilePic);
                    Glide.with(getContext()).load(user.getCoverPicUrl()).into(ivCover);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadUserPosts() {
        FirebaseHelper.getDatabase().getReference("posts").orderByChild("userUid").equalTo(targetUid).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    post.setPostId(snapshot.getKey());
                    postList.add(post);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void checkFollowStatus() {
        FirebaseHelper.getDatabase().getReference("users/" + currentUid + "/following/" + targetUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isFollowing = snapshot.getValue(Boolean.class);
                btnFollowUnfollow.setText((isFollowing != null && isFollowing) ? "Unfollow" : "Follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void toggleFollow() {
        FirebaseHelper.getDatabase().getReference("users/" + currentUid + "/following/" + targetUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isFollowing = snapshot.getValue(Boolean.class);
                boolean newStatus = !(isFollowing != null && isFollowing);
                // Update following
                FirebaseHelper.getDatabase().getReference("users/" + currentUid + "/following/" + targetUid).setValue(newStatus);
                // Update followers
                FirebaseHelper.getDatabase().getReference("users/" + targetUid + "/followers/" + currentUid).setValue(newStatus);
                // Send notification if following
                if (newStatus) {
                    Notification notif = new Notification("follow", currentUid, null, "followed you", System.currentTimeMillis());
                    FirebaseHelper.getDatabase().getReference("notifications/" + targetUid).push().setValue(notif);
                }
                // Update button
                btnFollowUnfollow.setText(newStatus ? "Unfollow" : "Follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}