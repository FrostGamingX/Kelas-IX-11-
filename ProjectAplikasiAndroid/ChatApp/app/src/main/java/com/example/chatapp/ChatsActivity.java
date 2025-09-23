package com.example.chatapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        if (tabLayout == null) {
            Log.e("ChatsActivity", "tabLayout is null");
            return;
        }
        if (viewPager == null) {
            Log.e("ChatsActivity", "viewPager is null");
            return;
        }

        viewPager.setAdapter(new ChatsPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_camera);
                    break;
                case 1:
                    tab.setText("Chats");
                    break;
                case 2:
                    tab.setText("Status");
                    break;
                case 3:
                    tab.setText("Calls");
                    break;
            }
        }).attach();
    }

    private static class ChatsPagerAdapter extends FragmentStateAdapter {
        public ChatsPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CameraFragment();
                case 1:
                    return new ChatsFragment();
                case 2:
                    return new StatusFragment();
                case 3:
                    return new CallsFragment();
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}