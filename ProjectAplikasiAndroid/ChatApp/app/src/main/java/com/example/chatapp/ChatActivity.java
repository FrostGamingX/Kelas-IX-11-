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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView ivContactPhoto, ivBack, ivVideoCall, ivVoiceCall, ivMore, ivSend;
    private TextView tvContactName;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private MessagesAdapter adapter; // Deklarasikan sebagai field
    private List<Message> messageList = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference messagesRef;
    private String receiverUid;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivContactPhoto = findViewById(R.id.iv_contact_photo);
        tvContactName = findViewById(R.id.tv_contact_name);
        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        ivSend = findViewById(R.id.iv_send);
        ivBack = findViewById(R.id.iv_back);
        ivVideoCall = findViewById(R.id.iv_video_call);
        ivVoiceCall = findViewById(R.id.iv_voice_call);
        ivMore = findViewById(R.id.iv_more);

        receiverUid = getIntent().getStringExtra("receiverUid");
        currentUid = mAuth.getCurrentUser().getUid();

        String chatId = getChatId(currentUid, receiverUid);
        messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");

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
        loadContactInfo();

        ivSend.setOnClickListener(v -> sendMessage());
        ivBack.setOnClickListener(v -> finish());
        ivVideoCall.setOnClickListener(v -> Toast.makeText(this, "Video Call", Toast.LENGTH_SHORT).show());
        ivVoiceCall.setOnClickListener(v -> Toast.makeText(this, "Voice Call", Toast.LENGTH_SHORT).show());
        ivMore.setOnClickListener(v -> Toast.makeText(this, "More Options", Toast.LENGTH_SHORT).show());
    }

    private String getChatId(String uid1, String uid2) {
        List<String> uids = new ArrayList<>();
        uids.add(uid1);
        uids.add(uid2);
        Collections.sort(uids);
        return uids.get(0) + "_" + uids.get(1);
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
            public void onChildRemoved(DataSnapshot snapshot) {
                // Opsional: Tangani penghapusan di sini jika Firebase otomatis menghapus
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loadContactInfo() {
        tvContactName.setText(getIntent().getStringExtra("receiverName"));
        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("users").child(receiverUid);
        receiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null && user.profilePic != null) {
                    Picasso.get().load(user.profilePic).into(ivContactPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}