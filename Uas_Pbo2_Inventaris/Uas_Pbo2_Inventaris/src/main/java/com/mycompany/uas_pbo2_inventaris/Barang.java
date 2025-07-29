package com.mycompany.uas_pbo2_inventaris;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Barang {
    private final SimpleIntegerProperty idBarang;
    private final SimpleStringProperty namaBarang;
    private final SimpleStringProperty kategoriBarang;
    private final SimpleStringProperty satuan;
    private final SimpleIntegerProperty stok;
    private final SimpleStringProperty tanggalPengiriman;

    public Barang(int id, String nama, String kategori, String satuan, int stok, String tanggal) {
        this.idBarang = new SimpleIntegerProperty(id);
        this.namaBarang = new SimpleStringProperty(nama);
        this.kategoriBarang = new SimpleStringProperty(kategori);
        this.satuan = new SimpleStringProperty(satuan);
        this.stok = new SimpleIntegerProperty(stok);
        this.tanggalPengiriman = new SimpleStringProperty(tanggal);
    }

    // Getters
    public int getIdBarang() { return idBarang.get(); }
    public String getNamaBarang() { return namaBarang.get(); }
    public String getKategoriBarang() { return kategoriBarang.get(); }
    public String getSatuan() { return satuan.get(); }
    public int getStok() { return stok.get(); }
    public String getTanggalPengiriman() { return tanggalPengiriman.get(); }

    
    @Override
    public String toString() {
        return getNamaBarang();
    }
}
