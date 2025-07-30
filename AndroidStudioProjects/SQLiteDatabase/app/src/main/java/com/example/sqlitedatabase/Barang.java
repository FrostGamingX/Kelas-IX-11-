// Barang.java
package com.example.sqlitedatabase;

public class Barang {
    private String idbarang;
    private String barang;
    private int stok;       // Diubah menjadi int
    private double harga;   // Diubah menjadi double

    // Konstruktor jika ID ada
    public Barang(String idbarang, String barang, int stok, double harga) {
        this.idbarang = idbarang;
        this.barang = barang;
        this.stok = stok;
        this.harga = harga;
    }

    // Konstruktor lain jika dibutuhkan (misalnya, tanpa ID saat membuat barang baru)
    public Barang(String barang, int stok, double harga) {
        this.barang = barang;
        this.stok = stok;
        this.harga = harga;
    }

    // Getters
    public String getIdbarang() {
        return idbarang;
    }

    public String getBarang() {
        return barang;
    }

    public int getStok() { // Getter untuk int
        return stok;
    }

    public double getHarga() { // Getter untuk double
        return harga;
    }

    // Setters jika diperlukan
    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public void setStok(int stok) { // Setter untuk int
        this.stok = stok;
    }

    public void setHarga(double harga) { // Setter untuk double
        this.harga = harga;
    }
}