package com.example.chatapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView ivBack, ivVideoCall, ivVoiceCall, ivMore, ivSend;
    private TextView tvContactName;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private MessagesAdapter adapter; // Deklarasikan sebagai field
    private List<Message> messageList = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference messagesRef;
    private String groupId;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivBack = findViewById(R.id.iv_back);
        tvContactName = findViewById(R.id.tv_contact_name);
        ivVideoCall = findViewById(R.id.iv_video_call);
        ivVoiceCall = findViewById(R.id.iv_voice_call);
        ivMore = findViewById(R.id.iv_more);
        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        ivSend = findViewById(R.id.iv_send);

        groupId = getIntent().getStringExtra("groupId");
        currentUid = mAuth.getCurrentUser().getUid();

        messagesRef = FirebaseDatabase.getInstance().getReference("groups").child(groupId).child("messages");

        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessagesAdapter(this, messageList, currentUid, (message, position) -> {
            new AlertDialog.Builder(this)
                    .setMessage("Delete message?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        messagesRef.child(message.messageId).removeValue();
                        messageList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, messageList.size());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        rvMessages.setAdapter(adapter);

        loadMessages();
        loadGroupInfo();

        ivSend.setOnClickListener(v -> sendMessage());
        ivBack.setOnClickListener(v -> finish());
        ivVideoCall.setOnClickListener(v -> Toast.makeText(this, "Video Call", Toast.LENGTH_SHORT).show());
        ivVoiceCall.setOnClickListener(v -> Toast.makeText(this, "Voice Call", Toast.LENGTH_SHORT).show());
        ivMore.setOnClickListener(v -> Toast.makeText(this, "More Options", Toast.LENGTH_SHORT).show());
    }

    private void loadMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                if (msg != null) {
                    msg.messageId = snapshot.getKey();
                    messageList.add(msg);
                    adapter.notifyDataSetChanged();
                    rvMessages.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(GroupChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (!text.isEmpty()) {
            String messageId = messagesRef.push().getKey();
            Message msg = new Message(currentUid, text, System.currentTimeMillis());
            messagesRef.child(messageId).setValue(msg);
            etMessage.setText("");
        }
    }

    private void loadGroupInfo() {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("groups").child(groupId);
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                if (group != null) {
                    tvContactName.setText(group.name);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(GroupChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}