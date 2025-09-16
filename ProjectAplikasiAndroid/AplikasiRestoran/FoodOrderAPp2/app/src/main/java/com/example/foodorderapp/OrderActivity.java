package com.example.foodorderapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    private TextView tvTotal;
    private EditText etAddress;
    private Spinner spinnerPayment;
    private Button btnConfirm;
    private FirebaseDatabase database;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tvTotal = findViewById(R.id.tv_total_order);
        etAddress = findViewById(R.id.et_address);
        spinnerPayment = findViewById(R.id.spinner_payment);
        btnConfirm = findViewById(R.id.btn_confirm_order);

        database = FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Asumsi total dari cart, fetch di sini atau pass via intent
        tvTotal.setText("Total: Rp 100000");  // Replace with real calculation

        // Payment options
        String[] payments = {"Cash on Delivery", "Transfer Bank"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, payments);
        spinnerPayment.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            String address = etAddress.getText().toString();
            String payment = spinnerPayment.getSelectedItem().toString();

            if (!address.isEmpty()) {
                // Buat order baru
                String orderId = database.getReference("orders").push().getKey();
                Map<String, Object> order = new HashMap<>();
                order.put("user_id", uid);
                order.put("total_price", 100000.0);  // Real value
                order.put("address", address);
                order.put("status", "Pending");
                order.put("payment_method", payment);
                order.put("timestamp", System.currentTimeMillis());

                // Tambah products dari cart
                // Asumsi copy dari carts ke orders/products

                database.getReference("orders").child(orderId).setValue(order);
                // Kosongkan cart
                database.getReference("carts").child(uid).removeValue();

                // Kirim notif ke admin (gunakan FCM, lihat service di bawah)
                Toast.makeText(this, "Pesanan dikonfirmasi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}