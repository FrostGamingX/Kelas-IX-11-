package com.example.socialapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.activities.NewPostActivity;
import com.example.socialapp.adapters.PostsAdapter;
import com.example.socialapp.models.Post;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private DatabaseReference postsRef;
    private Button btnNewPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvPosts = view.findViewById(R.id.rv_posts);
        btnNewPost = view.findViewById(R.id.btn_new_post);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(getContext(), postList);
        rvPosts.setAdapter(adapter);

        postsRef = FirebaseHelper.getDatabase().getReference("posts");
        postsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    post.setPostId(snapshot.getKey());
                    postList.add(0, post); // Add to top for newest first
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Update post if changed (e.g., likes/comments updated)
                Post updatedPost = snapshot.getValue(Post.class);
                if (updatedPost != null) {
                    updatedPost.setPostId(snapshot.getKey());
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i).getPostId().equals(updatedPost.getPostId())) {
                            postList.set(i, updatedPost);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle removal if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnNewPost.setOnClickListener(v -> startActivity(new Intent(getContext(), NewPostActivity.class)));

        return view;
    }
}