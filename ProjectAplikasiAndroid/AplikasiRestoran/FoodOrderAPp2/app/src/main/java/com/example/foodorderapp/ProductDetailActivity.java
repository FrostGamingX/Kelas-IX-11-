package com.example.foodorderapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.foodorderapp.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvDesc, tvPrice;
    private ImageView ivImage;
    private RatingBar ratingBar;
    private Button btnAddCart;
    private String productId;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tv_detail_name);
        tvDesc = findViewById(R.id.tv_detail_desc);
        tvPrice = findViewById(R.id.tv_detail_price);
        ivImage = findViewById(R.id.iv_detail_image);
        ratingBar = findViewById(R.id.rating_bar);
        btnAddCart = findViewById(R.id.btn_add_to_cart);

        productId = getIntent().getStringExtra("product_id");
        database = FirebaseDatabase.getInstance();

        database.getReference("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    tvName.setText(product.getName());
                    tvDesc.setText(product.getDescription());
                    tvPrice.setText("Rp " + product.getPrice());
                    Glide.with(ProductDetailActivity.this).load(product.getImageUrl()).into(ivImage);
                    // Asumsi rating dari database, atau hardcode
                    ratingBar.setRating(4.5f);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProductDetailActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnAddCart.setOnClickListener(v -> {
            // Sama seperti di adapter, tambah ke cart
            Toast.makeText(this, "Ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
        });
    }
}