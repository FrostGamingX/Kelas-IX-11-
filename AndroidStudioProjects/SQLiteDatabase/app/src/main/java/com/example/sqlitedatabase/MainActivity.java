package com.example.sqlitedatabase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Implementasikan listener untuk BarangAdapter
public class MainActivity extends AppCompatActivity implements BarangAdapter.OnItemActionClickListener {

    private static final String TAG = "MainActivity"; // Untuk logging

    Database db;
    EditText etBarang, etStok, etHarga;
    TextView txPilihan;
    List<Barang> databarang = new ArrayList<>();
    BarangAdapter adapter;
    RecyclerView rcvBarang;

    String idbarang; // Untuk menyimpan ID barang yang akan diupdate

    Button btnLihatSiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Pastikan R.layout.activity_main ada

        // Inisialisasi Database Helper
        db = new Database(this);

        // Panggil loadViews() untuk menginisialisasi semua View
        loadViews();
        setupRecyclerView();

        // Muat data awal
        selectData();

        btnLihatSiswa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SiswaActivity.class);
            startActivity(intent);
        });

        View mainView = findViewById(R.id.main); // Pastikan R.id.main ada di activity_main.xml
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Log.e(TAG, "View dengan ID 'main' tidak ditemukan di layout activity_main.xml");
        }
    }

    // Metode untuk inisialisasi semua View
    private void loadViews() {
        Log.d(TAG, "loadViews: Inisialisasi Views");
        etBarang = findViewById(R.id.etBarang);
        etStok = findViewById(R.id.etStok);
        etHarga = findViewById(R.id.etHarga);
        txPilihan = findViewById(R.id.txPilihan);
        rcvBarang = findViewById(R.id.rcvBarang);
        btnLihatSiswa = findViewById(R.id.btnLihatSiswa);

        // Validasi apakah View ditemukan
        if (etBarang == null) Log.e(TAG, "EditText etBarang tidak ditemukan!");
        if (etStok == null) Log.e(TAG, "EditText etStok tidak ditemukan!");
        if (etHarga == null) Log.e(TAG, "EditText etHarga tidak ditemukan!");
        if (txPilihan == null) Log.e(TAG, "TextView txPilihan tidak ditemukan!");
        if (rcvBarang == null) Log.e(TAG, "RecyclerView rcvBarang tidak ditemukan!");
        if (btnLihatSiswa == null) Log.e(TAG, "Button btnLihatSiswa tidak ditemukan!");

        // Set default text untuk txPilihan jika belum diset dari selectUpdate
        if (txPilihan != null && txPilihan.getText().toString().isEmpty()) {
            txPilihan.setText("insert");
        }
    }

    private void setupRecyclerView() {
        if (rcvBarang != null) {
            rcvBarang.setLayoutManager(new LinearLayoutManager(this));
            rcvBarang.setHasFixedSize(true);
            // Inisialisasi adapter dengan list kosong dan listener
            adapter = new BarangAdapter(this, databarang, this);
            rcvBarang.setAdapter(adapter);
        } else {
            pesan("RecyclerView tidak ditemukan, tidak dapat menampilkan data.");
            Log.e(TAG, "setupRecyclerView: RecyclerView rcvBarang adalah null.");
        }
    }

    public void simpan(View v) {
        // Pastikan view sudah diinisialisasi
        if (etBarang == null || etStok == null || etHarga == null || txPilihan == null) {
            pesan("Komponen input belum siap.");
            Log.e(TAG, "simpan: Salah satu komponen EditText atau TextView adalah null.");
            return;
        }

        String barang = etBarang.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String pilihan = txPilihan.getText().toString();

        if (barang.isEmpty() || stokStr.isEmpty() || hargaStr.isEmpty()) {
            pesan("Data Tidak Boleh Kosong");
            return;
        }

        // Validasi tipe data sebelum memanggil metode database
        try {
            Integer.parseInt(stokStr); // Coba parse, tidak perlu simpan jika hanya untuk validasi
            Double.parseDouble(hargaStr);
        } catch (NumberFormatException e) {
            pesan("Stok dan Harga harus berupa angka yang valid.");
            Log.e(TAG, "simpan: NumberFormatException - Stok: " + stokStr + ", Harga: " + hargaStr, e);
            return;
        }

        if (pilihan.equals("insert")) {
            long result = db.insertBarang(barang, stokStr, hargaStr);
            if (result != -1) {
                pesan("Insert Data Berhasil");
                selectData(); // Refresh data
                bersihkanInput();
            } else {
                pesan("Insert Data Gagal");
            }
        } else { // Proses Update
            if (this.idbarang == null || this.idbarang.isEmpty()) {
                pesan("ID Barang tidak valid untuk update. Silakan pilih barang terlebih dahulu.");
                Log.w(TAG, "simpan: Mencoba update tanpa idbarang yang valid.");
                bersihkanInput(); // Reset ke mode insert jika ID tidak valid
                return;
            }
            if (db.updateBarang(this.idbarang, barang, stokStr, hargaStr)) {
                pesan("Update Data Berhasil");
                selectData(); // Refresh data
                bersihkanInput();
            } else {
                pesan("Update Data Gagal");
            }
        }
    }

    private void bersihkanInput() {
        if (etBarang != null) etBarang.setText("");
        if (etStok != null) etStok.setText("");
        if (etHarga != null) etHarga.setText("");
        if (txPilihan != null) txPilihan.setText("insert");
        this.idbarang = null;
        if (etBarang != null) etBarang.requestFocus();
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selectData() {
        Log.d(TAG, "selectData: Memuat data barang...");
        String sql = "SELECT * FROM " + Database.TABLE_BARANG + " ORDER BY " + Database.KOLOM_BARANG + " ASC";
        Cursor cursor = null;
        try {
            cursor = db.select(sql);
            databarang.clear(); // Bersihkan list sebelum memuat data baru

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    int idCol = cursor.getColumnIndexOrThrow(Database.KOLOM_ID_BARANG);
                    int barangCol = cursor.getColumnIndexOrThrow(Database.KOLOM_BARANG);
                    int stokCol = cursor.getColumnIndexOrThrow(Database.KOLOM_STOK);
                    int hargaCol = cursor.getColumnIndexOrThrow(Database.KOLOM_HARGA);

                    while (cursor.moveToNext()) {
                        String idBarangDb = cursor.getString(idCol);
                        String namaBarangDb = cursor.getString(barangCol);
                        int stokDb = cursor.getInt(stokCol);
                        double hargaDb = cursor.getDouble(hargaCol);
                        databarang.add(new Barang(idBarangDb, namaBarangDb, stokDb, hargaDb));
                    }
                } else {
                    pesan("Data Barang Kosong");
                }
            } else {
                pesan("Gagal mengambil data barang (cursor null)");
                Log.w(TAG, "selectData: Cursor null setelah query SELECT.");
            }
        } catch (Exception e) {
            pesan("Error memuat data barang: " + e.getMessage());
            Log.e(TAG, "selectData: Exception saat memuat data.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Update adapter
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Cukup ini jika menggunakan list yang sama
            // atau jika Anda mengganti instance list di adapter:
            // adapter.updateData(databarang); // Jika ada metode updateData di adapter
        } else {
            Log.e(TAG, "selectData: Adapter adalah null, tidak dapat mengupdate UI.");
        }
    }

    // Implementasi dari BarangAdapter.OnItemActionClickListener
    @Override
    public void onUpdateClick(String idBarang) {
        this.idbarang = idBarang;
        String sql = "SELECT * FROM " + Database.TABLE_BARANG + " WHERE " + Database.KOLOM_ID_BARANG + " = ?";
        Cursor cursor = null;
        try {
            // Gunakan ReadableDatabase untuk query SELECT
            cursor = db.getReadableDatabase().rawQuery(sql, new String[]{idBarang});

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    if (etBarang != null) etBarang.setText(cursor.getString(cursor.getColumnIndexOrThrow(Database.KOLOM_BARANG)));
                    if (etStok != null) etStok.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Database.KOLOM_STOK))));
                    if (etHarga != null) etHarga.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(Database.KOLOM_HARGA))));
                    if (txPilihan != null) txPilihan.setText("update");
                } else {
                    pesan("Barang tidak ditemukan untuk diupdate");
                    if (txPilihan != null) txPilihan.setText("insert"); // Kembali ke mode insert jika tidak ditemukan
                }
            } else {
                pesan("Gagal mencari barang untuk diupdate (cursor null)");
                if (txPilihan != null) txPilihan.setText("insert");
                Log.w(TAG, "onUpdateClick (selectUpdate): Cursor null setelah query.");
            }
        } catch (Exception e) {
            pesan("Error mencari barang: " + e.getMessage());
            if (txPilihan != null) txPilihan.setText("insert");
            Log.e(TAG, "onUpdateClick (selectUpdate): Exception.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onDeleteClick(String idBarang) {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("PERINGATAN");
        al.setMessage("Anda yakin ingin menghapus data barang ini?");
        al.setPositiveButton("Ya", (dialog, which) -> {
            if (db.deleteBarang(idBarang)) {
                pesan("Delete Data Barang Berhasil");
                selectData(); // Refresh data
                bersihkanInput(); // Bersihkan input jika barang yang di-edit terhapus
            } else {
                pesan("Delete Data Barang Gagal");
            }
        });
        al.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
        al.show();
    }

    // Metode lama selectUpdate dan deleteData sekarang menjadi bagian dari implementasi listener
    // public void selectUpdate(String id) { ... } // Sudah digantikan oleh onUpdateClick
    // public void deleteData(String id) { ... } // Sudah digantikan oleh onDeleteClick

    @Override
    protected void onResume() {
        super.onResume();
        // Pertimbangkan untuk memuat ulang data jika ada kemungkinan perubahan dari activity lain
        // atau jika ada operasi yang mungkin tidak langsung me-refresh list.
        // selectData(); // Hati-hati, ini bisa menyebabkan pemuatan berulang jika tidak diperlukan.
    }
}
