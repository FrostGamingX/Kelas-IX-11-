package com.example.adminapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminapp.R;
import com.example.adminapp.adapters.AdminOrderAdapter;
import com.example.adminapp.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private RecyclerView rvOrders;
    private AdminOrderAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        rvOrders = view.findViewById(R.id.rv_admin_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminOrderAdapter(getContext(), orderList);
        rvOrders.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();

        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        database.getReference("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    order.setId(data.getKey());
                    orderList.add(order);
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