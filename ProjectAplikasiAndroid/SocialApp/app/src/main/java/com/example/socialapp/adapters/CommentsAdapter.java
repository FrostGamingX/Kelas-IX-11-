package com.example.socialapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.models.Comment;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> commentList;

    // Constructor yang menerima List<Comment>
    public CommentsAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = (commentList != null) ? commentList : new ArrayList<>();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (position < commentList.size()) {
            Comment comment = commentList.get(position);
            if (comment != null) {
                holder.tvCommentText.setText(comment.getText() != null ? comment.getText() : "");

                // Load username
                if (comment.getUserUid() != null && !comment.getUserUid().isEmpty()) {
                    FirebaseHelper.getDatabase()
                            .getReference("users/" + comment.getUserUid() + "/username")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String username = snapshot.getValue(String.class);
                                    if (username != null && !username.isEmpty()) {
                                        holder.tvCommentUsername.setText(username + ": ");
                                    } else {
                                        holder.tvCommentUsername.setText("Unknown: ");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    holder.tvCommentUsername.setText("Unknown: ");
                                }
                            });
                } else {
                    holder.tvCommentUsername.setText("Unknown: ");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    // Method untuk update data
    public void updateComments(List<Comment> newComments) {
        this.commentList.clear();
        if (newComments != null) {
            this.commentList.addAll(newComments);
        }
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentUsername, tvCommentText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentUsername = itemView.findViewById(R.id.tv_comment_username);
            tvCommentText = itemView.findViewById(R.id.tv_comment_text);
        }
    }
}