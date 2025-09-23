package com.example.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private ImageView ivProfilePic;
    private EditText etName, etStatus;
    private Button btnUploadPic, btnUpdate;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfilePic = findViewById(R.id.iv_profile_pic);
        etName = findViewById(R.id.et_name);
        etStatus = findViewById(R.id.et_status);
        btnUploadPic = findViewById(R.id.btn_upload_pic);
        btnUpdate = findViewById(R.id.btn_update);

        String uid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        storageRef = FirebaseStorage.getInstance().getReference("profiles/" + uid + ".jpg");

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                uploadImage(imageUri);
            }
        });

        loadProfile();

        btnUploadPic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void loadProfile() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    etName.setText(user.name);
                    etStatus.setText(user.status);
                    if (user.profilePic != null) {
                        Picasso.get().load(user.profilePic).into(ivProfilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(Uri imageUri) {
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                userRef.child("profilePic").setValue(uri.toString());
                Toast.makeText(this, "Photo uploaded", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        String status = etStatus.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name required");
            return;
        }

        userRef.child("name").setValue(name);
        userRef.child("status").setValue(status);
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    }
}