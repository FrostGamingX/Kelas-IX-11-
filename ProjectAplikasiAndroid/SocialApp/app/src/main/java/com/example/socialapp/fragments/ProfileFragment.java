package com.example.socialapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;
import com.example.socialapp.adapters.PostsAdapter;
import com.example.socialapp.models.Notification;
import com.example.socialapp.models.Post;
import com.example.socialapp.models.User;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private ImageView ivProfilePic, ivCover;
    private TextView tvUsername;
    private Button btnUploadProfile, btnUploadCover, btnNotifications, btnFollow;
    private RecyclerView rvMyPosts;
    private PostsAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private String currentUid;
    private Uri selectedImageUri;
    private boolean isProfilePic = true;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    uploadImage();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivCover = view.findViewById(R.id.iv_cover);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        tvUsername = view.findViewById(R.id.tv_username);
        btnUploadProfile = view.findViewById(R.id.btn_upload_profile);
        btnUploadCover = view.findViewById(R.id.btn_upload_cover);
        rvMyPosts = view.findViewById(R.id.rv_my_posts);
        btnNotifications = view.findViewById(R.id.btn_notifications);
        btnFollow = view.findViewById(R.id.btn_follow);

        currentUid = FirebaseHelper.getCurrentUserUid();
        rvMyPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PostsAdapter(getContext(), postList);
        rvMyPosts.setAdapter(adapter);

        loadUserData();
        loadMyPosts();

        btnUploadProfile.setOnClickListener(v -> {
            isProfilePic = true;
            openGallery();
        });
        btnUploadCover.setOnClickListener(v -> {
            isProfilePic = false;
            openGallery();
        });
        btnNotifications.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new NotificationsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        btnFollow.setOnClickListener(v -> showFollowDialog());

        return view;
    }

    private void loadUserData() {
        FirebaseHelper.getDatabase().getReference("users/" + currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvUsername.setText(user.getUsername());
                    Glide.with(getContext()).load(user.getProfilePicUrl()).circleCrop().into(ivProfilePic);
                    Glide.with(getContext()).load(user.getCoverPicUrl()).into(ivCover);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadMyPosts() {
        FirebaseHelper.getDatabase().getReference("posts").orderByChild("userUid").equalTo(currentUid).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    post.setPostId(snapshot.getKey());
                    postList.add(post);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void uploadImage() {
        if (selectedImageUri != null) {
            String path = isProfilePic ? "profiles/" + currentUid + "/profile.jpg" : "profiles/" + currentUid + "/cover.jpg";
            UploadTask uploadTask = FirebaseHelper.getStorage().getReference(path).putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    String field = isProfilePic ? "profilePicUrl" : "coverPicUrl";
                    FirebaseHelper.getDatabase().getReference("users/" + currentUid + "/" + field).setValue(url);
                });
            });
        }
    }

    private void showFollowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Follow User");

        final EditText input = new EditText(getContext());
        input.setHint("Enter username");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Follow", (dialog, which) -> {
            String username = input.getText().toString().trim();
            if (!username.isEmpty()) {
                followUserByUsername(username);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void followUserByUsername(String username) {
        FirebaseHelper.getDatabase().getReference("users").orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String targetUid = child.getKey();
                                if (!targetUid.equals(currentUid)) {
                                    // Update following
                                    FirebaseHelper.getDatabase().getReference("users/" + currentUid + "/following/" + targetUid).setValue(true);
                                    // Update followers
                                    FirebaseHelper.getDatabase().getReference("users/" + targetUid + "/followers/" + currentUid).setValue(true);
                                    // Send notification
                                    Notification notif = new Notification("follow", currentUid, null, "followed you", System.currentTimeMillis());
                                    FirebaseHelper.getDatabase().getReference("notifications/" + targetUid).push().setValue(notif);
                                    Toast.makeText(getContext(), "Followed " + username, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}