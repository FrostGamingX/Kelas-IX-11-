package com.example.blogapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blogapp.R;
import com.example.blogapp.models.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPostActivity extends AppCompatActivity {
    private DatabaseReference postRef;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postId = getIntent().getStringExtra("postId");
        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        TextInputEditText etTitle = findViewById(R.id.et_title);
        TextInputEditText etDescription = findViewById(R.id.et_description);
        Button btnSave = findViewById(R.id.btn_save);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    etTitle.setText(post.title);
                    etDescription.setText(post.description);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            if (!title.isEmpty() && !description.isEmpty()) {
                postRef.child("title").setValue(title);
                postRef.child("description").setValue(description);
                Toast.makeText(this, "Perubahan disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lengkapi judul dan deskripsi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}