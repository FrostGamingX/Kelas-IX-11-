package com.example.socialapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;
import com.example.socialapp.models.Comment;
import com.example.socialapp.models.Notification;
import com.example.socialapp.models.Post;
import com.example.socialapp.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostsAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvCaption.setText(post.getCaption() != null ? post.getCaption() : "");

        // Load post image
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            Glide.with(context).load(post.getImageUrl()).into(holder.ivPostImage);
        }

        // Load username
        FirebaseHelper.getDatabase().getReference("users/" + post.getUserUid() + "/username")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                        String username = snapshot.getValue(String.class);
                        if (username != null) {
                            holder.tvUsername.setText(username);
                        } else {
                            holder.tvUsername.setText("Unknown User");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {}
                });

        // Load profile image
        FirebaseHelper.getDatabase().getReference("users/" + post.getUserUid() + "/profilePicUrl")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                        String profilePicUrl = snapshot.getValue(String.class);
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            Glide.with(context).load(profilePicUrl).circleCrop().into(holder.ivProfile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {}
                });

        // Update like button text
        updateLikeButton(holder, post);

        // Like button click listener
        holder.btnLike.setOnClickListener(v -> {
            String currentUid = FirebaseHelper.getCurrentUserUid();
            if (currentUid != null) {
                boolean isLiked = post.getLikes() != null && post.getLikes().containsKey(currentUid) && post.getLikes().get(currentUid);

                // Update local data
                if (post.getLikes() == null) {
                    post.setLikes(new HashMap<>());
                }
                post.getLikes().put(currentUid, !isLiked);

                // Update database
                FirebaseHelper.getDatabase()
                        .getReference("posts/" + post.getPostId() + "/likes/" + currentUid)
                        .setValue(!isLiked);

                // Send notification if user liked (not unliked)
                if (!isLiked) {
                    String notifText = "liked your post";
                    Notification notif = new Notification("like", currentUid, post.getPostId(), notifText, System.currentTimeMillis());
                    FirebaseHelper.getDatabase()
                            .getReference("notifications/" + post.getUserUid())
                            .push()
                            .setValue(notif);
                }

                // Update UI
                updateLikeButton(holder, post);
            }
        });

        // Comment button click listener
        holder.btnComment.setOnClickListener(v -> showCommentDialog(post, holder));

        // Load comments
        loadComments(post, holder);
    }

    private void updateLikeButton(PostViewHolder holder, Post post) {
        Map<String, Boolean> likes = post.getLikes();
        int likeCount = (likes != null) ? likes.size() : 0;
        String currentUid = FirebaseHelper.getCurrentUserUid();
        boolean isLiked = (likes != null && currentUid != null) ?
                likes.containsKey(currentUid) && likes.get(currentUid) : false;

        holder.btnLike.setText(isLiked ? "Unlike (" + likeCount + ")" : "Like (" + likeCount + ")");
    }

    private void showCommentDialog(Post post, PostViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Comment");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Post", (dialog, which) -> {
            String commentText = input.getText().toString().trim();
            if (!commentText.isEmpty()) {
                String currentUid = FirebaseHelper.getCurrentUserUid();
                if (currentUid != null) {
                    // Generate comment ID
                    String commentId = FirebaseHelper.getDatabase().getReference("comments").push().getKey();

                    // Create comment object
                    Comment comment = new Comment();
                    comment.setUserUid(currentUid);
                    comment.setText(commentText);
                    comment.setTimestamp(System.currentTimeMillis());

                    // Initialize comments map if null
                    if (post.getComments() == null) {
                        post.setComments(new HashMap<>());
                    }

                    // Add comment to local post data
                    post.getComments().put(commentId, comment);

                    // Save comment to database
                    FirebaseHelper.getDatabase()
                            .getReference("posts/" + post.getPostId() + "/comments/" + commentId)
                            .setValue(comment);

                    // Send notification
                    String notifText = "commented on your post: " + commentText;
                    Notification notif = new Notification("comment", currentUid, post.getPostId(), notifText, System.currentTimeMillis());
                    FirebaseHelper.getDatabase()
                            .getReference("notifications/" + post.getUserUid())
                            .push()
                            .setValue(notif);

                    // Refresh comments display
                    loadComments(post, holder);
                    Toast.makeText(context, "Comment posted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void loadComments(Post post, PostViewHolder holder) {
        Map<String, Comment> commentsMap = post.getComments();

        if (commentsMap != null && !commentsMap.isEmpty()) {
            // Convert Map to List safely
            List<Comment> commentList = new ArrayList<>();
            for (Map.Entry<String, Comment> entry : commentsMap.entrySet()) {
                if (entry.getValue() != null) {
                    commentList.add(entry.getValue());
                }
            }

            CommentsAdapter commentsAdapter = new CommentsAdapter(context, commentList);
            holder.rvComments.setLayoutManager(new LinearLayoutManager(context));
            holder.rvComments.setAdapter(commentsAdapter);
            holder.rvComments.setVisibility(View.VISIBLE);
        } else {
            holder.rvComments.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    // ViewHolder class
    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile, ivPostImage;
        TextView tvUsername, tvCaption;
        Button btnLike, btnComment;
        RecyclerView rvComments;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvUsername = itemView.findViewById(R.id.tv_username);
            ivPostImage = itemView.findViewById(R.id.iv_post_image);
            tvCaption = itemView.findViewById(R.id.tv_caption);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnComment = itemView.findViewById(R.id.btn_comment);
            rvComments = itemView.findViewById(R.id.rv_comments);
        }
    }
}