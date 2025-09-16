package com.example.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogapp.activities.AddPostActivity;
import com.example.blogapp.activities.ProfileActivity;
import com.example.blogapp.activities.WelcomeActivity;
import com.example.blogapp.adapters.PostAdapter;
import com.example.blogapp.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView rvPosts;
    private PostAdapter adapter;
    private List<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("posts");

        rvPosts = findViewById(R.id.rv_posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(postList, this, false);  // false untuk mode home (tanpa edit/hapus)
        rvPosts.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, AddPostActivity.class)));

        ImageView ivProfile = findViewById(R.id.iv_profile);
        ivProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        loadPosts();
    }

    private void loadPosts() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    post.postId = snapshot.getKey();
                    postList.add(post);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                Post updatedPost = snapshot.getValue(Post.class);
                if (updatedPost != null) {
                    updatedPost.postId = snapshot.getKey();
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i).postId.equals(updatedPost.postId)) {
                            postList.set(i, updatedPost);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                String removedId = snapshot.getKey();
                for (int i = 0; i < postList.size(); i++) {
                    if (postList.get(i).postId.equals(removedId)) {
                        postList.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}