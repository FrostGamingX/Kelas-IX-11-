package com.example.blogapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blogapp.R;
import com.example.blogapp.models.Post;
import com.example.blogapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String postId, currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postId = getIntent().getStringExtra("postId");
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvAuthor = findViewById(R.id.tv_author);
        TextView tvDescription = findViewById(R.id.tv_description);
        ImageButton ibLike = findViewById(R.id.ib_like);
        ImageButton ibSave = findViewById(R.id.ib_save);
        Button btnBack = findViewById(R.id.btn_back);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    tvTitle.setText(post.title);
                    tvDescription.setText(post.description);

                    FirebaseDatabase.getInstance().getReference("users").child(post.authorUid).get().addOnSuccessListener(userSnap -> {
                        User author = userSnap.getValue(User.class);
                        if (author != null) {
                            tvAuthor.setText("Oleh: " + author.username);
                        }
                    });

                    boolean isLiked = post.likes != null && post.likes.containsKey(currentUid);
                    ibLike.setSelected(isLiked);
                    boolean isSaved = post.saves != null && post.saves.containsKey(currentUid);
                    ibSave.setSelected(isSaved);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        ibLike.setOnClickListener(v -> {
            mDatabase.child("likes").child(currentUid).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    mDatabase.child("likes").child(currentUid).removeValue();
                } else {
                    mDatabase.child("likes").child(currentUid).setValue(true);
                }
            });
        });

        ibSave.setOnClickListener(v -> {
            mDatabase.child("saves").child(currentUid).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    mDatabase.child("saves").child(currentUid).removeValue();
                } else {
                    mDatabase.child("saves").child(currentUid).setValue(true);
                }
            });
        });

        btnBack.setOnClickListener(v -> finish());
    }
}