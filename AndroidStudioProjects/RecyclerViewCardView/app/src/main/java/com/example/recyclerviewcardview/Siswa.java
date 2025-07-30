package com.example.recyclerviewcardview;

public class Siswa {

    private String nama;
    private String Alamat;

    public Siswa(String nama, String alamat) {
        this.nama = nama;
        Alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }
}
