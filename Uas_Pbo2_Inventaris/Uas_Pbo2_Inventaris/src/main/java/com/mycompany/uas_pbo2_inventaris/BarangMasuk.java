/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas_pbo2_inventaris;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class BarangMasuk {
    private final SimpleIntegerProperty idBarangMasuk;
    private final SimpleIntegerProperty idBarang;
    private final SimpleStringProperty namaBarang; // Untuk tampilan di tabel
    private final SimpleIntegerProperty jumlahMasuk;
    private final SimpleStringProperty tanggalMasuk;

    public BarangMasuk(int idMasuk, int idBrg, String namaBrg, int jumlah, String tanggal) {
        this.idBarangMasuk = new SimpleIntegerProperty(idMasuk);
        this.idBarang = new SimpleIntegerProperty(idBrg);
        this.namaBarang = new SimpleStringProperty(namaBrg);
        this.jumlahMasuk = new SimpleIntegerProperty(jumlah);
        this.tanggalMasuk = new SimpleStringProperty(tanggal);
    }

    // Getters
    public int getIdBarangMasuk() { return idBarangMasuk.get(); }
    public int getIdBarang() { return idBarang.get(); }
    public String getNamaBarang() { return namaBarang.get(); }
    public int getJumlahMasuk() { return jumlahMasuk.get(); }
    public String getTanggalMasuk() { return tanggalMasuk.get(); }
}
