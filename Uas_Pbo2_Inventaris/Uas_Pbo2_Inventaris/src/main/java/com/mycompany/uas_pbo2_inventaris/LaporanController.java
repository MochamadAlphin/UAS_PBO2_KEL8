/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas_pbo2_inventaris;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LaporanController implements Initializable {

    @FXML private DatePicker dateMulai;
    @FXML private DatePicker dateSelesai;
    @FXML private TableView<LaporanItem> tabelLaporan;
    @FXML private TableColumn<LaporanItem, String> colTanggal, colNamaBarang, colJenis;
    @FXML private TableColumn<LaporanItem, Integer> colJumlah;
    
    private final ObservableList<LaporanItem> listLaporan = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        // Set tanggal default ke bulan ini
        dateMulai.setValue(LocalDate.now().withDayOfMonth(1));
        dateSelesai.setValue(LocalDate.now());
    }
    
    private void setupTableColumns() {
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colNamaBarang.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    }

    @FXML
    private void handleTampilkan(ActionEvent event) {
        LocalDate mulai = dateMulai.getValue();
        LocalDate selesai = dateSelesai.getValue();

        if (mulai == null || selesai == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih rentang tanggal terlebih dahulu.");
            return;
        }
        if (mulai.isAfter(selesai)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Tanggal mulai tidak boleh setelah tanggal selesai.");
            return;
        }

        listLaporan.clear();
        String query = 
            "(SELECT Tanggal_Masuk as Tanggal, b.Nama_Barang, 'Masuk' as Jenis, Jumlah_Masuk as Jumlah " +
            "FROM barang_masuk bm JOIN barang b ON bm.Id_Barang = b.Id_Barang " +
            "WHERE Tanggal_Masuk BETWEEN ? AND ?) " +
            "UNION ALL " +
            "(SELECT Tanggal_Keluar as Tanggal, b.Nama_Barang, 'Keluar' as Jenis, Jumlah_Keluar as Jumlah " +
            "FROM barang_keluar bk JOIN barang b ON bk.Id_Barang = b.Id_Barang " +
            "WHERE Tanggal_Keluar BETWEEN ? AND ?) " +
            "ORDER BY Tanggal ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, mulai.format(formatter));
            ps.setString(2, selesai.format(formatter));
            ps.setString(3, mulai.format(formatter));
            ps.setString(4, selesai.format(formatter));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listLaporan.add(new LaporanItem(
                    rs.getString("Tanggal"),
                    rs.getString("Nama_Barang"),
                    rs.getString("Jenis"),
                    rs.getInt("Jumlah")
                ));
            }
            tabelLaporan.setItems(listLaporan);
            if (listLaporan.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Informasi", "Tidak ada data transaksi pada rentang tanggal yang dipilih.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengambil data laporan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
