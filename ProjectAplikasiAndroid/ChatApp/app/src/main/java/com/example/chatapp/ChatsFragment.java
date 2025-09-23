package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView rvChats;
    private UsersAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_users, container, false);

        rvChats = view.findViewById(R.id.rv_users);
        rvChats.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersAdapter(getContext(), userList, user -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("receiverUid", user.uid);
            intent.putExtra("receiverName", user.name);
            startActivity(intent);
        });
        rvChats.setAdapter(adapter);

        loadUsers();

        return view;
    }

    private void loadUsers() {
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    user.uid = ds.getKey();
                    if (!user.email.equals(mAuth.getCurrentUser().getEmail())) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}