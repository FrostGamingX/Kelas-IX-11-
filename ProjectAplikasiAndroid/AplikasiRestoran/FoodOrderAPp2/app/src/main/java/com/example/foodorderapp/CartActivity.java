package com.example.foodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.adapters.CartAdapter;
import com.example.foodorderapp.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView tvTotal;
    private Button btnOrder;
    private FirebaseDatabase database;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCart = findViewById(R.id.rv_cart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartItems);
        rvCart.setAdapter(adapter);

        tvTotal = findViewById(R.id.tv_total);
        btnOrder = findViewById(R.id.btn_order);

        database = FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch cart
        database.getReference("carts").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cartItems.clear();
                double total = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    CartItem item = new CartItem();
                    item.setProductId(data.getKey());
                    item.setQuantity(data.child("quantity").getValue(Integer.class));
                    item.setPrice(data.child("price").getValue(Double.class));
                    // Fetch name dan image dari products (atau simpan di cart)
                    // Untuk sederhana, asumsikan fetch tambahan atau simpan di model
                    cartItems.add(item);
                    total += item.getPrice() * item.getQuantity();
                }
                tvTotal.setText("Total: Rp " + total);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        btnOrder.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, OrderActivity.class)));
    }
}