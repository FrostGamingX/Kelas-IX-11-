package com.example.sharedprefference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView etbarang, etstok;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        load();
    }

    public void load() {
        etbarang = findViewById(R.id.etbarang);
        etstok = findViewById(R.id.etstok);
        sharedPreferences = getSharedPreferences("barang", MODE_PRIVATE);
    }

    public void isisharedpreferences(String barang, float stok) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("barang", barang);
        editor.putFloat("stok", stok);
        editor.apply();
    }

    public void tampil(View view) {

        String barang = sharedPreferences.getString("barang", "");
        float stok = sharedPreferences.getFloat("stok", 0);

        etbarang.setText(barang);
        etstok.setText(String.valueOf(stok));

    }

    public void simpan(View view) {

        String barang = etbarang.getText().toString().trim();
        String stokStr = etstok.getText().toString().trim();

        if (barang.isEmpty() || stokStr.isEmpty()) {
            Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        float stok;

        try {
            stok = Float.parseFloat(stokStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Stok harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        isisharedpreferences(barang, stok);
        Toast.makeText(this, "Data berhasil disimpan ke SharedPreferences", Toast.LENGTH_SHORT).show();

        etbarang.setText("");
        etstok.setText("");
    }
}
