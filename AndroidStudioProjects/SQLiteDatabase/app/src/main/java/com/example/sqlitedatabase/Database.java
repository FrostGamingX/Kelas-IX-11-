package com.example.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "dbsiak.db";
    // Naikkan versi jika Anda mengubah skema
    private static final int DATABASE_VERSION = 2; // Sesuaikan jika ada perubahan skema

    // --- Konstanta untuk Tabel Barang (Produk) ---
    public static final String TABLE_BARANG = "tblbarang";
    public static final String KOLOM_ID_BARANG = "idbarang";
    public static final String KOLOM_BARANG = "barang";
    public static final String KOLOM_STOK = "stok";
    public static final String KOLOM_HARGA = "harga";

    // --- Konstanta untuk Tabel Siswa ---
    public static final String TABLE_SISWA = "tblsiswa";
    public static final String KOLOM_ID_SISWA = "idsiswa";
    public static final String KOLOM_NAMA_SISWA = "nama";
    public static final String KOLOM_ALAMAT_SISWA = "alamat";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Membuat tabel baru.");
        String sqlBarang = "CREATE TABLE " + TABLE_BARANG + " (" +
                KOLOM_ID_BARANG + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KOLOM_BARANG + " TEXT NOT NULL," +
                KOLOM_STOK + " INTEGER DEFAULT 0," +
                KOLOM_HARGA + " REAL DEFAULT 0.0" +
                ");";
        db.execSQL(sqlBarang);
        Log.i(TAG, "Tabel " + TABLE_BARANG + " berhasil dibuat.");

        String sqlSiswa = "CREATE TABLE " + TABLE_SISWA + " (" +
                KOLOM_ID_SISWA + " TEXT PRIMARY KEY," + // Atau INTEGER PRIMARY KEY AUTOINCREMENT
                KOLOM_NAMA_SISWA + " TEXT NOT NULL," +
                KOLOM_ALAMAT_SISWA + " TEXT" +
                ");";
        db.execSQL(sqlSiswa);
        Log.i(TAG, "Tabel " + TABLE_SISWA + " berhasil dibuat.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "onUpgrade: Mengupgrade database dari versi " + oldVersion + " ke " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SISWA);
        onCreate(db);
    }

    public Cursor select(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.rawQuery(sql, null);
        } catch (Exception e) {
            Log.e(TAG, "Error menjalankan query SELECT: " + sql, e);
            return null; // Kembalikan null jika ada error
        }
    }

    // --- Metode CRUD Barang ---
    public long insertBarang(String barang, String stokStr, String hargaStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_BARANG, barang);
        try {
            values.put(KOLOM_STOK, Integer.parseInt(stokStr));
            values.put(KOLOM_HARGA, Double.parseDouble(hargaStr));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing stok atau harga saat insertBarang: stok='" + stokStr + "', harga='" + hargaStr + "'", e);
            return -1;
        }

        try {
            long newRowId = db.insertOrThrow(TABLE_BARANG, null, values); // Gunakan insertOrThrow untuk exception yang lebih jelas
            Log.i(TAG, "insertBarang: Baris baru ditambahkan dengan ID: " + newRowId);
            return newRowId;
        } catch (Exception e) {
            Log.e(TAG, "Error saat insertBarang ke database", e);
            return -1;
        }
    }

    public boolean updateBarang(String id, String barang, String stokStr, String hargaStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_BARANG, barang);
        try {
            values.put(KOLOM_STOK, Integer.parseInt(stokStr));
            values.put(KOLOM_HARGA, Double.parseDouble(hargaStr));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing stok atau harga saat updateBarang: stok='" + stokStr + "', harga='" + hargaStr + "'", e);
            return false;
        }

        String selection = KOLOM_ID_BARANG + " = ?";
        String[] selectionArgs = { id };
        try {
            int count = db.update(TABLE_BARANG, values, selection, selectionArgs);
            Log.i(TAG, "updateBarang: Jumlah baris terupdate: " + count);
            return count > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error saat updateBarang di database", e);
            return false;
        }
    }

    public boolean deleteBarang(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = KOLOM_ID_BARANG + " = ?";
        String[] selectionArgs = { id };
        try {
            int count = db.delete(TABLE_BARANG, selection, selectionArgs);
            Log.i(TAG, "deleteBarang: Jumlah baris terhapus: " + count);
            return count > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error saat deleteBarang dari database", e);
            return false;
        }
    }

    // --- Metode CRUD Siswa ---
// Di dalam kelas Database.java

// ... (kode lainnya) ...

    public long insertSiswa(String idSiswa, String nama, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Validasi dasar, ID Siswa tidak boleh kosong karena PRIMARY KEY
        if (idSiswa == null || idSiswa.trim().isEmpty()) {
            Log.e(TAG, "insertSiswa: ID Siswa tidak boleh kosong.");
            return -1; // Indikasi error
        }
        // Jika nama juga tidak boleh kosong
        if (nama == null || nama.trim().isEmpty()) {
            Log.e(TAG, "insertSiswa: Nama Siswa tidak boleh kosong.");
            return -2; // Indikasi error yang berbeda jika perlu
        }


        values.put(KOLOM_ID_SISWA, idSiswa.trim());
        values.put(KOLOM_NAMA_SISWA, nama.trim());
        values.put(KOLOM_ALAMAT_SISWA, alamat.trim()); // Alamat boleh kosong atau tidak, tergantung kebutuhan

        try {
            // Menggunakan insertOrThrow akan memberikan SQLiteConstraintException jika ID sudah ada (jika ID adalah PRIMARY KEY)
            long newRowId = db.insertOrThrow(TABLE_SISWA, null, values);
            Log.i(TAG, "insertSiswa: Baris baru ditambahkan dengan ID (atau rowid): " + newRowId);
            return newRowId; // Mengembalikan row ID dari baris yang baru dimasukkan, atau -1 jika terjadi error
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e(TAG, "Error saat insertSiswa ke database - ConstraintViolation (Mungkin ID sudah ada): " + idSiswa, e);
            return -3; // Indikasi error spesifik untuk constraint violation
        } catch (Exception e) {
            Log.e(TAG, "Error umum saat insertSiswa ke database", e);
            return -1; // Indikasi error umum
        }
        // Jangan tutup database di sini jika instance db digunakan oleh bagian lain dari Database.java
        // SQLiteOpenHelper akan menangani pembukaan dan penutupan koneksi.
    }

// ... (kode lainnya) ...


    public boolean updateSiswa(String idSiswa, String nama, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_NAMA_SISWA, nama);
        values.put(KOLOM_ALAMAT_SISWA, alamat);

        String selection = KOLOM_ID_SISWA + " = ?";
        String[] selectionArgs = { idSiswa };
        try {
            int count = db.update(TABLE_SISWA, values, selection, selectionArgs);
            Log.i(TAG, "updateSiswa: Jumlah baris terupdate: " + count);
            return count > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error saat updateSiswa di database", e);
            return false;
        }
    }

    public boolean deleteSiswa(String idSiswa) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = KOLOM_ID_SISWA + " = ?";
        String[] selectionArgs = { idSiswa };
        try {
            int count = db.delete(TABLE_SISWA, selection, selectionArgs);
            Log.i(TAG, "deleteSiswa: Jumlah baris terhapus: " + count);
            return count > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error saat deleteSiswa dari database", e);
            return false;
        }
    }

    public Cursor getAllSiswa() {
        String sql = "SELECT * FROM " + TABLE_SISWA + " ORDER BY " + KOLOM_NAMA_SISWA + " ASC";
        return select(sql);
    }

    public Cursor getAllBarang() {
        String sql = "SELECT * FROM " + TABLE_BARANG + " ORDER BY " + KOLOM_BARANG + " ASC";
        return select(sql);
    }
}
