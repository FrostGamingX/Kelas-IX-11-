package com.example.socialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.adapters.NotificationsAdapter;
import com.example.socialapp.models.Notification;
import com.example.socialapp.utils.FirebaseHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView rvNotifications;
    private NotificationsAdapter adapter;
    private List<Notification> notifList = new ArrayList<>();
    private String currentUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        rvNotifications = view.findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pass FragmentManager dari current fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        adapter = new NotificationsAdapter(getContext(), notifList, fragmentManager);
        rvNotifications.setAdapter(adapter);

        currentUid = FirebaseHelper.getCurrentUserUid();
        FirebaseHelper.getDatabase().getReference("notifications/" + currentUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Notification notif = snapshot.getValue(Notification.class);
                if (notif != null) {
                    notif.setNotifId(snapshot.getKey());
                    notifList.add(0, notif);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Update notification if changed
                Notification updatedNotif = snapshot.getValue(Notification.class);
                if (updatedNotif != null) {
                    updatedNotif.setNotifId(snapshot.getKey());
                    for (int i = 0; i < notifList.size(); i++) {
                        if (notifList.get(i).getNotifId().equals(updatedNotif.getNotifId())) {
                            notifList.set(i, updatedNotif);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Remove notification if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }
}