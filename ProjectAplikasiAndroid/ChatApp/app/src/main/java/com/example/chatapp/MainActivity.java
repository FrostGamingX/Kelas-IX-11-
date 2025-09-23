package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnUsers = findViewById(R.id.btn_users);
        Button btnCreateGroup = findViewById(R.id.btn_create_group);

        btnUsers.setOnClickListener(v -> startActivity(new Intent(this, ChatsActivity.class)));

        btnCreateGroup.setOnClickListener(v -> {
            // Contoh create group sederhana: Tambah logic untuk input nama group dan members
            String groupId = FirebaseDatabase.getInstance().getReference("groups").push().getKey();
            Group group = new Group("New Group", new String[]{mAuth.getCurrentUser().getUid()}); // Tambah members
            FirebaseDatabase.getInstance().getReference("groups").child(groupId).setValue(group);

            Intent intent = new Intent(this, GroupChatActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}