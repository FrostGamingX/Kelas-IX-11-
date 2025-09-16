package com.example.adminapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register);  // Opsional, jika admin bisa register sendiri

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                checkAdminRole();
                            } else {
                                Toast.makeText(this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Jika ada register, handle seperti App User, tapi set role "admin" manual di Firebase console.
    }

    private void checkAdminRole() {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                if ("admin".equals(role)) {
                    startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    mAuth.signOut();
                    Toast.makeText(LoginActivity.this, "Anda bukan admin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}