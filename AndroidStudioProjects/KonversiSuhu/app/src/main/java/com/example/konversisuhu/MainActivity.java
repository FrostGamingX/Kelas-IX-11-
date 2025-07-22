package com.example.konversisuhu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter; // Import ArrayAdapter
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;      // Import Spinner
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText etSuhuAwal;
    private Spinner spinnerSatuanAwal;
    private Spinner spinnerSatuanTujuan;
    private Button btnKonversi;
    private TextView tvHasil;

    @SuppressLint("MissingInflatedId")
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

        etSuhuAwal = findViewById(R.id.etSuhuAwal);
        spinnerSatuanAwal = findViewById(R.id.spinnerSatuanAwal);
        spinnerSatuanTujuan = findViewById(R.id.spinnerSatuanTujuan);
        btnKonversi = findViewById(R.id.btnKonversi);
        tvHasil = findViewById(R.id.tvHasil);

        // Setup ArrayAdapter untuk Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.satuan_suhu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSatuanAwal.setAdapter(adapter);
        spinnerSatuanTujuan.setAdapter(adapter);

        // Set default selection (opsional, misalnya Celsius ke Fahrenheit)
        spinnerSatuanAwal.setSelection(0); // Celsius
        spinnerSatuanTujuan.setSelection(1); // Fahrenheit

        btnKonversi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konversiSuhu(); // Metode ini sudah public void secara implisit karena akses defaultnya adalah package-private
                // dan dapat diakses dari inner class. Kita bisa eksplisit membuatnya public.
            }
        });
    }

    // Dibuat public dan void. Logika dipindahkan ke sini.
    @SuppressLint("StringFormatInvalid")
    public void konversiSuhu() {
        String suhuAwalString = etSuhuAwal.getText().toString();
        if (suhuAwalString.isEmpty()) {
            tvHasil.setText(getString(R.string.hasil, "-"));
            Toast.makeText(this, getString(R.string.input_tidak_valid), Toast.LENGTH_SHORT).show();
            return;
        }

        Double suhuAwal;
        try {
            suhuAwal = Double.parseDouble(suhuAwalString);
        } catch (NumberFormatException e) {
            tvHasil.setText(getString(R.string.hasil, "-"));
            Toast.makeText(this, getString(R.string.input_tidak_valid), Toast.LENGTH_SHORT).show();
            return;
        }

        // Mendapatkan pilihan dari Spinner
        String satuanAwal = spinnerSatuanAwal.getSelectedItem().toString();
        String satuanTujuan = spinnerSatuanTujuan.getSelectedItem().toString();

        if (satuanAwal.isEmpty() || satuanTujuan.isEmpty()) { // Pengecekan sederhana, Spinner biasanya selalu punya pilihan
            Toast.makeText(this, getString(R.string.pilih_satuan_awal_dan_tujuan), Toast.LENGTH_SHORT).show();
            return;
        }

        double hasilKonversi = hitungKonversiSederhana(suhuAwal, satuanAwal, satuanTujuan);
        DecimalFormat df = new DecimalFormat("#.##");

        tvHasil.setText(getString(R.string.hasil, df.format(hasilKonversi) + " °" + satuanTujuan));
    }

    public double hitungKonversiSederhana(double nilai, String dari, String ke) {
        if (dari.equals(ke)) return nilai;

        // Mendapatkan referensi ke string satuan dari resources
        String celsiusStr = getString(R.string.celsius);
        String fahrenheitStr = getString(R.string.fahrenheit);
        String kelvinStr = getString(R.string.kelvin);
        String reamurStr = getString(R.string.reamur);

        // Konversi dulu ke Celsius sebagai basis
        double suhuDalamCelsius;
        if (dari.equals(fahrenheitStr)) {
            suhuDalamCelsius = (nilai - 32) * 5 / 9;
        } else if (dari.equals(kelvinStr)) {
            suhuDalamCelsius = nilai - 273.15;
        } else if (dari.equals(reamurStr)) {
            suhuDalamCelsius = nilai * 5 / 4;
        } else { // Asumsi sudah Celsius jika tidak cocok dengan yang lain
            suhuDalamCelsius = nilai;
        }

        // Kemudian konversi dari Celsius ke satuan tujuan
        if (ke.equals(fahrenheitStr)) {
            return (suhuDalamCelsius * 9 / 5) + 32;
        } else if (ke.equals(kelvinStr)) {
            return suhuDalamCelsius + 273.15;
        } else if (ke.equals(reamurStr)) {
            return suhuDalamCelsius * 4 / 5;
        } else { // Tujuan adalah Celsius
            return suhuDalamCelsius;
        }
    }
}
