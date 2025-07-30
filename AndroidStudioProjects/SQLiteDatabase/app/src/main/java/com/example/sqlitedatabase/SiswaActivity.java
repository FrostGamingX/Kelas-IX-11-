package com.example.sqlitedatabase;

import static com.example.sqlitedatabase.R.id.fabTambahSiswa;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils; // Untuk mengecek string kosong
import android.util.Log;
import android.view.LayoutInflater; // Untuk inflate custom dialog
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Untuk EditText di dialog
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog; // Untuk dialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton; // Jika menggunakan FAB

import java.util.ArrayList;
import java.util.List;

public class SiswaActivity extends AppCompatActivity {

    private static final String TAG = "SiswaActivity";

    RecyclerView rcvSiswa;
    SiswaAdapter siswaAdapter;
    List<Siswa> daftarSiswa = new ArrayList<>();
    Database db;
    Button btnKembaliKeMain;
    FloatingActionButton fabTambahSiswa; // Atau Button btnDialogTambahSiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Daftar Siswa");
        } else {
            Log.w(TAG, "ActionBar tidak ditemukan.");
        }

        db = new Database(this);

        rcvSiswa = findViewById(R.id.rcvSiswa);
        btnKembaliKeMain = findViewById(R.id.btnKembaliKeMain);
         // Inisialisasi FAB
        // Jika menggunakan Button biasa:
        fabTambahSiswa = findViewById(R.id.fabTambahSiswa);

        if (rcvSiswa != null) {
            rcvSiswa.setLayoutManager(new LinearLayoutManager(this));
            rcvSiswa.setHasFixedSize(true);
            siswaAdapter = new SiswaAdapter(this, daftarSiswa);
            rcvSiswa.setAdapter(siswaAdapter);
            loadDataSiswa();
        } else {
            pesan("RecyclerView Siswa tidak ditemukan di layout.");
            Log.e(TAG, "onCreate: RecyclerView dengan ID rcvSiswa tidak ditemukan.");
        }

        if (btnKembaliKeMain != null) {
            btnKembaliKeMain.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "onCreate: Button dengan ID btnKembaliKeMain tidak ditemukan di layout.");
        }

        // --- TAMBAHKAN ONCLICKLISTENER UNTUK TOMBOL TAMBAH SISWA ---
        if (fabTambahSiswa != null) {
            fabTambahSiswa.setOnClickListener(v -> showDialogTambahSiswa());
        } else {
            // Jika Anda menggunakan Button biasa, periksa ID yang sesuai:
            // Button btnDialogTambahSiswaRef = findViewById(R.id.btnDialogTambahSiswa);
            // if (btnDialogTambahSiswaRef != null) {
            //     btnDialogTambahSiswaRef.setOnClickListener(v -> showDialogTambahSiswa());
            // } else {
            Log.e(TAG, "onCreate: Tombol untuk menambah siswa tidak ditemukan di layout.");
            // }
        }
    }

    private void showDialogTambahSiswa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data Siswa Baru");

        // Inflate custom layout untuk dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_siswa, null);
        builder.setView(dialogView);

        final EditText etIdSiswa = dialogView.findViewById(R.id.etIdSiswaDialog);
        final EditText etNamaSiswa = dialogView.findViewById(R.id.etNamaSiswaDialog);
        final EditText etAlamatSiswa = dialogView.findViewById(R.id.etAlamatSiswaDialog);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String idSiswa = etIdSiswa.getText().toString().trim();
            String namaSiswa = etNamaSiswa.getText().toString().trim();
            String alamatSiswa = etAlamatSiswa.getText().toString().trim();

            if (TextUtils.isEmpty(idSiswa) || TextUtils.isEmpty(namaSiswa) || TextUtils.isEmpty(alamatSiswa)) {
                pesan("Semua field harus diisi!");
                // Agar dialog tidak otomatis tertutup jika validasi gagal, kita handle di bawah
                // Namun, untuk positive button default, dialog akan tertutup.
                // Untuk mencegah penutupan, Anda perlu implementasi yang lebih advanced
                // dengan mengambil button dari dialog dan set onClickListener-nya secara manual.
                // Untuk kesederhanaan, kita biarkan dialog tertutup dan tampilkan pesan.
                return;
            }

            // Panggil metode untuk insert ke database (pastikan metode ini ada di Database.java)
            long result = db.insertSiswa(idSiswa, namaSiswa, alamatSiswa);

            if (result != -1) { // -1 biasanya menandakan error
                pesan("Data siswa berhasil disimpan.");
                loadDataSiswa(); // Muat ulang data untuk memperbarui RecyclerView
            } else {
                pesan("Gagal menyimpan data siswa. ID Siswa mungkin sudah ada atau terjadi error lain.");
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadDataSiswa() {
        Log.d(TAG, "loadDataSiswa: Memuat data siswa...");
        if (db == null) {
            pesan("Database helper belum diinisialisasi.");
            Log.e(TAG, "loadDataSiswa: Database helper (db) adalah null.");
            return;
        }
        daftarSiswa.clear();
        String sql = "SELECT * FROM " + Database.TABLE_SISWA +
                " ORDER BY " + Database.KOLOM_NAMA_SISWA + " ASC";
        Cursor cursor = null;
        try {
            cursor = db.select(sql);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    int idCol = cursor.getColumnIndexOrThrow(Database.KOLOM_ID_SISWA);
                    int namaCol = cursor.getColumnIndexOrThrow(Database.KOLOM_NAMA_SISWA);
                    int alamatCol = cursor.getColumnIndexOrThrow(Database.KOLOM_ALAMAT_SISWA);
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(idCol);
                        String nama = cursor.getString(namaCol);
                        String alamat = cursor.getString(alamatCol);
                        daftarSiswa.add(new Siswa(id, nama, alamat));
                    }
                } else {
                    // pesan("Data Siswa Kosong"); // Bisa di-skip jika sering reload
                }
            } else {
                pesan("Gagal mengambil data siswa (cursor null)");
                Log.w(TAG, "loadDataSiswa: Cursor null setelah query SELECT.");
            }
        } catch (IllegalArgumentException e) {
            pesan("Error: Nama kolom tidak ditemukan di database siswa. Periksa Database.java.");
            Log.e(TAG, "loadDataSiswa: IllegalArgumentException - Nama kolom salah.", e);
        } catch (Exception e) {
            pesan("Error memuat data siswa: " + e.getMessage());
            Log.e(TAG, "loadDataSiswa: Exception saat memuat data.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (siswaAdapter != null) {
            siswaAdapter.notifyDataSetChanged();
        } else {
            Log.w(TAG, "loadDataSiswa: SiswaAdapter adalah null, tidak dapat mengupdate UI.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // loadDataSiswa(); // Pertimbangkan apakah perlu reload data di onResume
        Log.d(TAG, "onResume: SiswaActivity di-resume.");
    }

    private void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}
