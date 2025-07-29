/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas_pbo2_inventaris;

/**
 *
 * @author LENOVO
 */
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BarangKeluar {
    

    private final SimpleIntegerProperty idKeluar;
    private final SimpleIntegerProperty idBarang;
    private final SimpleStringProperty namaBarang; // Untuk tampilan di tabel
    private final SimpleIntegerProperty jumlahKeluar;
    private final SimpleStringProperty tanggalKeluar;

    public BarangKeluar(int idKeluar, int idBrg, String namaBrg, int jumlah, String tanggal) {
        this.idKeluar = new SimpleIntegerProperty(idKeluar);
        this.idBarang = new SimpleIntegerProperty(idBrg);
        this.namaBarang = new SimpleStringProperty(namaBrg);
        this.jumlahKeluar = new SimpleIntegerProperty(jumlah);
        this.tanggalKeluar = new SimpleStringProperty(tanggal);
    }

    // Getters
    public int getIdKeluar() { return idKeluar.get(); }
    public int getIdBarang() { return idBarang.get(); }
    public String getNamaBarang() { return namaBarang.get(); }
    public int getJumlahKeluar() { return jumlahKeluar.get(); }
    public String getTanggalKeluar() { return tanggalKeluar.get(); }
}
