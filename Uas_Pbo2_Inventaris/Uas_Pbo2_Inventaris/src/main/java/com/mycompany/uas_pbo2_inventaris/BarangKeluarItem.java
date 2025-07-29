/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas_pbo2_inventaris;

/**
 *
 * @author LENOVO
 */


public class BarangKeluarItem {
    private int idBarang;
    private String namaBarang;
    private int sisaDipinjam; // Jumlah yang masih di luar dan belum dikembalikan

    public BarangKeluarItem(int idBarang, String namaBarang, int sisaDipinjam) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.sisaDipinjam = sisaDipinjam;
    }

    // Getters
    public int getIdBarang() {
        return idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getSisaDipinjam() {
        return sisaDipinjam;
    }

    // Ini akan memastikan nama barang yang benar muncul di ComboBox
    @Override
    public String toString() {
        return namaBarang;
    }
}
