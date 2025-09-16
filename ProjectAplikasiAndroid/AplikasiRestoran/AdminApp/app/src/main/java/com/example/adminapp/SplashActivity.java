package com.example.adminapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            if (mAuth.getCurrentUser() != null) {
                checkAdminRole();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }

    private void checkAdminRole() {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                if ("admin".equals(role)) {
                    startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
                } else {
                    mAuth.signOut();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}