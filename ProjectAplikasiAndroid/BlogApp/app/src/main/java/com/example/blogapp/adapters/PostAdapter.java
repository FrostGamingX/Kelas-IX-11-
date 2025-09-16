package com.example.blogapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.blogapp.R;
import com.example.blogapp.activities.EditPostActivity;
import com.example.blogapp.activities.PostDetailActivity;
import com.example.blogapp.models.Post;
import com.example.blogapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable {
    private List<Post> postList;
    private List<Post> postListFull;
    private Context context;
    private DatabaseReference mDatabase;
    private String currentUid;
    private boolean isMyPostsMode;  // True untuk tampilkan edit/hapus

    public PostAdapter(List<Post> postList, Context context, boolean isMyPostsMode) {
        this.postList = postList;
        this.postListFull = new ArrayList<>(postList);
        this.context = context;
        this.isMyPostsMode = isMyPostsMode;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvTitle.setText(post.title);
        String snippet = post.description.length() > 100 ? post.description.substring(0, 100) + "..." : post.description;
        holder.tvDescriptionSnippet.setText(snippet);

        // Load author
        mDatabase.child("users").child(post.authorUid).get().addOnSuccessListener(snapshot -> {
            User author = snapshot.getValue(User.class);
            if (author != null) {
                holder.tvAuthor.setText(author.username);
                Glide.with(context).load(author.photoUrl).into(holder.ivAuthorPhoto);
            }
        });

        // Like dan Save
        boolean isLiked = post.likes != null && post.likes.containsKey(currentUid);
        holder.ibLike.setSelected(isLiked);
        boolean isSaved = post.saves != null && post.saves.containsKey(currentUid);
        holder.ibSave.setSelected(isSaved);

        holder.ibLike.setOnClickListener(v -> toggleLike(post.postId, isLiked));
        holder.ibSave.setOnClickListener(v -> toggleSave(post.postId, isSaved));

        // Jika my posts mode, tampilkan edit/delete
        if (isMyPostsMode && post.authorUid.equals(currentUid)) {
            holder.ibEdit.setVisibility(View.VISIBLE);
            holder.ibDelete.setVisibility(View.VISIBLE);
            holder.ibEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.putExtra("postId", post.postId);
                context.startActivity(intent);
            });
            holder.ibDelete.setOnClickListener(v -> deletePost(post.postId));
        } else {
            holder.ibEdit.setVisibility(View.GONE);
            holder.ibDelete.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("postId", post.postId);
            context.startActivity(intent);
        });
    }

    private void toggleLike(String postId, boolean isLiked) {
        DatabaseReference likeRef = mDatabase.child("posts").child(postId).child("likes").child(currentUid);
        if (isLiked) {
            likeRef.removeValue();
        } else {
            likeRef.setValue(true);
        }
    }

    private void toggleSave(String postId, boolean isSaved) {
        DatabaseReference saveRef = mDatabase.child("posts").child(postId).child("saves").child(currentUid);
        if (isSaved) {
            saveRef.removeValue();
        } else {
            saveRef.setValue(true);
        }
    }

    private void deletePost(String postId) {
        mDatabase.child("posts").child(postId).removeValue();
        mDatabase.child("user_posts").child(currentUid).child(postId).removeValue();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Post> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(postListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Post post : postListFull) {
                        if (post.title.toLowerCase().contains(filterPattern)) {
                            filteredList.add(post);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                postList.clear();
                postList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivAuthorPhoto;
        TextView tvAuthor, tvTitle, tvDescriptionSnippet;
        ImageButton ibLike, ibSave, ibEdit, ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAuthorPhoto = itemView.findViewById(R.id.iv_author_photo);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescriptionSnippet = itemView.findViewById(R.id.tv_description_snippet);
            ibLike = itemView.findViewById(R.id.ib_like);
            ibSave = itemView.findViewById(R.id.ib_save);
            ibEdit = itemView.findViewById(R.id.ib_edit);
            ibDelete = itemView.findViewById(R.id.ib_delete);
        }
    }
}