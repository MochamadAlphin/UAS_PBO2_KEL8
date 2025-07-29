package com.mycompany.uas_pbo2_inventaris;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Import yang diperlukan
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BarangKeluarController implements Initializable {

    @FXML private ComboBox<Barang> comboBarang;
    @FXML private TextField txtJumlah;
    @FXML private DatePicker dateTanggal;
    @FXML private Button btnTambah;
    @FXML private Label lblStokTersedia;
    @FXML private TableView<BarangKeluar> tabelBarangKeluar;
    @FXML private TableColumn<BarangKeluar, Integer> colIdKeluar, colJumlah;
    @FXML private TableColumn<BarangKeluar, String> colNamaBarang, colTanggalKeluar;
    
    private final ObservableList<Barang> listBarang = FXCollections.observableArrayList();
    private final ObservableList<BarangKeluar> listBarangKeluar = FXCollections.observableArrayList();
    // PERBAIKAN: Menambahkan formatter untuk memastikan format tanggal yang benar
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String userRole;

    public void initData(String role) {
        this.userRole = role;
        aturHakAkses();
    }
    
    private void aturHakAkses() {
        if ("Admin".equals(this.userRole)) {
            comboBarang.setDisable(true);
            txtJumlah.setEditable(false);
            dateTanggal.setDisable(true);
            btnTambah.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        muatDataBarang();
        muatDataBarangKeluar();
        dateTanggal.setValue(LocalDate.now());
    }
    
    private void setupTableColumns() {
        colIdKeluar.setCellValueFactory(new PropertyValueFactory<>("idKeluar"));
        colNamaBarang.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlahKeluar"));
        colTanggalKeluar.setCellValueFactory(new PropertyValueFactory<>("tanggalKeluar"));
    }

    private void muatDataBarang() {
        listBarang.clear();
        String query = "SELECT Id_Barang, Nama_Barang, Stok FROM barang WHERE Stok > 0 ORDER BY Nama_Barang ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                listBarang.add(new Barang(
                    rs.getInt("Id_Barang"), 
                    rs.getString("Nama_Barang"), 
                    null, null, rs.getInt("Stok"), null
                ));
            }
            comboBarang.setItems(listBarang);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat daftar barang: " + e.getMessage());
        }
    }
    
    private void muatDataBarangKeluar() {
        listBarangKeluar.clear();
        String query = "SELECT bk.Id_Keluar, b.Nama_Barang, bk.Jumlah_Keluar, bk.Tanggal_Keluar " +
                       "FROM barang_keluar bk JOIN barang b ON bk.Id_Barang = b.Id_Barang ORDER BY bk.Tanggal_Keluar DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                listBarangKeluar.add(new BarangKeluar(
                    rs.getInt("Id_Keluar"), 0, rs.getString("Nama_Barang"), 
                    rs.getInt("Jumlah_Keluar"), rs.getString("Tanggal_Keluar")
                ));
            }
            tabelBarangKeluar.setItems(listBarangKeluar);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat riwayat barang keluar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBarangDipilih(ActionEvent event) {
        Barang selected = comboBarang.getValue();
        if (selected != null) {
            lblStokTersedia.setText("Stok tersedia: " + selected.getStok());
        } else {
            lblStokTersedia.setText("Stok tersedia: -");
        }
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        Barang selectedBarang = comboBarang.getValue();
        if (selectedBarang == null || txtJumlah.getText().isEmpty() || dateTanggal.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua field harus diisi!");
            return;
        }
        
        int jumlahKeluar;
        try {
            jumlahKeluar = Integer.parseInt(txtJumlah.getText());
            if (jumlahKeluar <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Jumlah harus angka positif!");
            return;
        }

        if (jumlahKeluar > selectedBarang.getStok()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Stok tidak mencukupi! Stok tersedia hanya " + selectedBarang.getStok());
            return;
        }
        
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            
            String sqlKeluar = "INSERT INTO barang_keluar (Id_Barang, Jumlah_Keluar, Tanggal_Keluar) VALUES (?, ?, ?)";
            PreparedStatement psKeluar = conn.prepareStatement(sqlKeluar);
            psKeluar.setInt(1, selectedBarang.getIdBarang());
            psKeluar.setInt(2, jumlahKeluar);
            
            // PERBAIKAN: Mengirim tanggal sebagai String dengan format YYYY-MM-DD
            psKeluar.setString(3, dateTanggal.getValue().format(formatter));
            
            psKeluar.executeUpdate();

            String sqlUpdateStok = "UPDATE barang SET Stok = Stok - ? WHERE Id_Barang = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStok);
            psUpdate.setInt(1, jumlahKeluar);
            psUpdate.setInt(2, selectedBarang.getIdBarang());
            psUpdate.executeUpdate();
            
            conn.commit();
            
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pemakaian barang berhasil dicatat!");
            muatDataBarangKeluar();
            muatDataBarang();
            bersihkanForm();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mencatat pemakaian barang: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void bersihkanForm() {
        comboBarang.getSelectionModel().clearSelection();
        txtJumlah.clear();
        dateTanggal.setValue(LocalDate.now());
        lblStokTersedia.setText("Stok tersedia: -");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
