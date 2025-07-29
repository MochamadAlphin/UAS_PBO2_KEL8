/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas_pbo2_inventaris;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LaporanItem {
    private final SimpleStringProperty tanggal;
    private final SimpleStringProperty namaBarang;
    private final SimpleStringProperty jenis;
    private final SimpleIntegerProperty jumlah;

    public LaporanItem(String tanggal, String namaBarang, String jenis, int jumlah) {
        this.tanggal = new SimpleStringProperty(tanggal);
        this.namaBarang = new SimpleStringProperty(namaBarang);
        this.jenis = new SimpleStringProperty(jenis);
        this.jumlah = new SimpleIntegerProperty(jumlah);
    }

    public String getTanggal() { return tanggal.get(); }
    public String getNamaBarang() { return namaBarang.get(); }
    public String getJenis() { return jenis.get(); }
    public int getJumlah() { return jumlah.get(); }
}
