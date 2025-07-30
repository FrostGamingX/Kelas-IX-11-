// Siswa.java
package com.example.sqlitedatabase; // Sesuaikan dengan package Anda

public class Siswa {
    private String idSiswa; // Jika Anda menggunakan ID dari database
    private String namaSiswa;
    private String alamatSiswa;

    // Konstruktor
    public Siswa(String idSiswa, String namaSiswa, String alamatSiswa) {
        this.idSiswa = idSiswa;
        this.namaSiswa = namaSiswa;
        this.alamatSiswa = alamatSiswa;
    }

    // Overload konstruktor jika ID di-generate otomatis dan tidak perlu saat pembuatan objek awal
    public Siswa(String namaSiswa, String alamatSiswa) {
        this.namaSiswa = namaSiswa;
        this.alamatSiswa = alamatSiswa;
    }


    // Getter
    public String getIdSiswa() {
        return idSiswa;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public String getAlamatSiswa() {
        return alamatSiswa;
    }

    // Setter (opsional, tergantung kebutuhan)
    public void setIdSiswa(String idSiswa) {
        this.idSiswa = idSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setAlamatSiswa(String alamatSiswa) {
        this.alamatSiswa = alamatSiswa;
    }
}