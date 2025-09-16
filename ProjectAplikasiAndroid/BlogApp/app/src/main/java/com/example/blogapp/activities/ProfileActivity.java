package com.example.blogapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.blogapp.R;
import com.example.blogapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    CircleImageView ivPhoto = findViewById(R.id.iv_photo);
                    Glide.with(ProfileActivity.this).load(user.photoUrl).into(ivPhoto);
                    TextView tvUsername = findViewById(R.id.tv_username);
                    tvUsername.setText(user.username);
                    TextView tvEmail = findViewById(R.id.tv_email);
                    tvEmail.setText(user.email);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        Button btnMyArticles = findViewById(R.id.btn_my_articles);
        btnMyArticles.setOnClickListener(v -> startActivity(new Intent(this, MyPostsActivity.class)));

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, WelcomeActivity.class));
            finishAffinity();
        });
    }
}