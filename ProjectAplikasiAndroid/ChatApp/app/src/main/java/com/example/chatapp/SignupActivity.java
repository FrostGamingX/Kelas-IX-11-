package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button btnSignup = findViewById(R.id.btn_signup);
        TextView tvLogin = findViewById(R.id.tv_login);

        btnSignup.setOnClickListener(v -> signupUser());
        tvLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void signupUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }
        if (!validateInput(email, password)) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(name, email, "Hey there!", null);
                        FirebaseDatabase.getInstance().getReference("users").child(uid)
                                .setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Database error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }
}