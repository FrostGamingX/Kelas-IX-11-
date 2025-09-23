package com.example.socialapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;
import com.example.socialapp.models.Post;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.storage.UploadTask;

public class NewPostActivity extends AppCompatActivity {
    private ImageView ivSelectedImage;
    private EditText etCaption;
    private Button btnSelectImage, btnPost;
    private Uri selectedImageUri;
    private String currentUid;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this).load(selectedImageUri).into(ivSelectedImage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        ivSelectedImage = findViewById(R.id.iv_selected_image);
        etCaption = findViewById(R.id.et_caption);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnPost = findViewById(R.id.btn_post);

        currentUid = FirebaseHelper.getCurrentUserUid();

        btnSelectImage.setOnClickListener(v -> openGallery());

        btnPost.setOnClickListener(v -> createPost());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void createPost() {
        String caption = etCaption.getText().toString().trim();
        if (selectedImageUri == null && caption.isEmpty()) {
            Toast.makeText(this, "Add image or caption", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image if selected
        if (selectedImageUri != null) {
            String path = "posts/" + currentUid + "/" + System.currentTimeMillis() + ".jpg";
            UploadTask uploadTask = FirebaseHelper.getStorage().getReference(path).putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    savePostToDb(uri.toString(), caption);
                });
            }).addOnFailureListener(e -> Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show());
        } else {
            savePostToDb(null, caption);
        }
    }

    private void savePostToDb(String imageUrl, String caption) {
        String postId = FirebaseHelper.getDatabase().getReference("posts").push().getKey();
        Post post = new Post(currentUid, caption, imageUrl, System.currentTimeMillis());
        FirebaseHelper.getDatabase().getReference("posts/" + postId).setValue(post)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Posted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}