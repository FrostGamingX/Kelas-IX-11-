package com.example.adminapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.adminapp.fragments.OrdersFragment;
import com.example.adminapp.fragments.ProductsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminDashboardActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new ProductsFragment();
                else return new OrdersFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Produk" : "Pesanan");
        }).attach();
    }
}