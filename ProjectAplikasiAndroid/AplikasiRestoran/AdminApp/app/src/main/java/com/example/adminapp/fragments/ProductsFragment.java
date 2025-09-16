package com.example.adminapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminapp.AddProductActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapters.AdminProductAdapter;
import com.example.adminapp.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private RecyclerView rvProducts;
    private AdminProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private FirebaseDatabase database;
    private FloatingActionButton fabAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        rvProducts = view.findViewById(R.id.rv_admin_products);
        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminProductAdapter(getContext(), productList);
        rvProducts.setAdapter(adapter);

        fabAdd = view.findViewById(R.id.fab_add_product);
        database = FirebaseDatabase.getInstance();

        fetchProducts();

        fabAdd.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddProductActivity.class)));

        return view;
    }

    private void fetchProducts() {
        database.getReference("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    product.setId(data.getKey());
                    productList.add(product);
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