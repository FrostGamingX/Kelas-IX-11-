package com.example.blogapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blogapp.R;
import com.example.blogapp.adapters.PostAdapter;
import com.example.blogapp.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {
    private RecyclerView rvPosts;
    private PostAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rvPosts = findViewById(R.id.rv_posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(postList, this, true);  // true untuk mode my posts
        rvPosts.setAdapter(adapter);

        loadMyPosts();
    }

    private void loadMyPosts() {
        FirebaseDatabase.getInstance().getReference("user_posts").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postIdSnap : snapshot.getChildren()) {
                    String postId = postIdSnap.getKey();
                    FirebaseDatabase.getInstance().getReference("posts").child(postId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot postSnap) {
                            Post post = postSnap.getValue(Post.class);
                            if (post != null) {
                                post.postId = postId;
                                // Tambah jika belum ada, update jika ada
                                boolean exists = false;
                                for (int i = 0; i < postList.size(); i++) {
                                    if (postList.get(i).postId.equals(postId)) {
                                        postList.set(i, post);
                                        adapter.notifyItemChanged(i);
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    postList.add(post);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {}
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}