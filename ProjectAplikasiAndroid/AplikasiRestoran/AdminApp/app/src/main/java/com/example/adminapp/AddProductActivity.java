package com.example.adminapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.adminapp.models.Product;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private EditText etName, etDesc, etPrice, etCategory, etStock;
    private Button btnUpload, btnSave;
    private ImageView ivPreview;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private Uri imageUri;
    private String productId;
    private String existingImageUrl;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivPreview.setVisibility(View.VISIBLE);
                    Glide.with(this).load(imageUri).into(ivPreview);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etName = findViewById(R.id.et_product_name);
        etDesc = findViewById(R.id.et_product_desc);
        etPrice = findViewById(R.id.et_product_price);
        etCategory = findViewById(R.id.et_product_category);
        etStock = findViewById(R.id.et_product_stock);
        btnUpload = findViewById(R.id.btn_upload_image);
        btnSave = findViewById(R.id.btn_save_product);
        ivPreview = findViewById(R.id.iv_preview);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // Jika edit
        productId = getIntent().getStringExtra("product_id");
        if (productId != null) {
            etName.setText(getIntent().getStringExtra("name"));
            etDesc.setText(getIntent().getStringExtra("desc"));
            etPrice.setText(String.valueOf(getIntent().getDoubleExtra("price", 0)));
            existingImageUrl = getIntent().getStringExtra("image_url");
            if (existingImageUrl != null) {
                ivPreview.setVisibility(View.VISIBLE);
                Glide.with(this).load(existingImageUrl).into(ivPreview);
            }
            // Fetch category dan stock jika ada extra
        }

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();
        double price = Double.parseDouble(etPrice.getText().toString());
        String category = etCategory.getText().toString();
        int stock = Integer.parseInt(etStock.getText().toString());

        if (name.isEmpty() || desc.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productId == null) {
            productId = database.getReference("products").push().getKey();
        }

        if (imageUri != null) {
            StorageReference ref = storage.getReference("product_images/" + UUID.randomUUID().toString());
            ref.putFile(imageUri).addOnSuccessListener(task -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                saveToDatabase(name, desc, price, category, stock, uri.toString());
            }));
        } else {
            saveToDatabase(name, desc, price, category, stock, existingImageUrl);
        }
    }

    private void saveToDatabase(String name, String desc, double price, String category, int stock, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(desc);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);
        product.setImageUrl(imageUrl);

        database.getReference("products").child(productId).setValue(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Produk disimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}