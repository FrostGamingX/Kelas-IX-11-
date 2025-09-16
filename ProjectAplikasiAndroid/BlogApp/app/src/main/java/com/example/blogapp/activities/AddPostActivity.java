package com.example.blogapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blogapp.R;
import com.example.blogapp.models.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPostActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TextInputEditText etTitle = findViewById(R.id.et_title);
        TextInputEditText etDescription = findViewById(R.id.et_description);
        Button btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            if (!title.isEmpty() && !description.isEmpty()) {
                Post post = new Post(title, description, currentUid);
                DatabaseReference newPostRef = mDatabase.child("posts").push();
                String postId = newPostRef.getKey();
                newPostRef.setValue(post);
                mDatabase.child("user_posts").child(currentUid).child(postId).setValue(true);
                Toast.makeText(this, "Artikel disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lengkapi judul dan deskripsi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}