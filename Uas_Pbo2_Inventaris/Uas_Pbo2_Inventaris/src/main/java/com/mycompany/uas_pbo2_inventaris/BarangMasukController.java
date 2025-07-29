package com.mycompany.uas_pbo2_inventaris;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

public class BarangMasukController implements Initializable {

    @FXML private ComboBox<BarangKeluarItem> comboBarang; // Menggunakan model baru
    @FXML private TextField txtJumlah;
    @FXML private DatePicker dateTanggal;
    @FXML private Button btnTambah;
    @FXML private Label lblSisa;
    @FXML private TableView<BarangMasuk> tabelBarangMasuk;
    @FXML private TableColumn<BarangMasuk, Integer> colIdMasuk, colJumlah;
    @FXML private TableColumn<BarangMasuk, String> colNamaBarang, colTanggalMasuk;
    
    private final ObservableList<BarangKeluarItem> listBarangDipinjam = FXCollections.observableArrayList();
    private final ObservableList<BarangMasuk> listBarangMasuk = FXCollections.observableArrayList();
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
        muatDataBarangDipinjam();
        muatRiwayatPengembalian();
        dateTanggal.setValue(LocalDate.now());
    }
    
    private void setupTableColumns() {
        colIdMasuk.setCellValueFactory(new PropertyValueFactory<>("idBarangMasuk"));
        colNamaBarang.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlahMasuk"));
        colTanggalMasuk.setCellValueFactory(new PropertyValueFactory<>("tanggalMasuk"));
    }

    private void muatDataBarangDipinjam() {
        listBarangDipinjam.clear();
        // Query ini menghitung selisih antara total keluar dan total masuk untuk setiap barang
        String query = "SELECT b.Id_Barang, b.Nama_Barang, " +
                       "((SELECT COALESCE(SUM(Jumlah_Keluar), 0) FROM barang_keluar WHERE Id_Barang = b.Id_Barang) - " +
                       "(SELECT COALESCE(SUM(Jumlah_Masuk), 0) FROM barang_masuk WHERE Id_Barang = b.Id_Barang)) as Sisa " +
                       "FROM barang b HAVING Sisa > 0";
                       
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                listBarangDipinjam.add(new BarangKeluarItem(
                    rs.getInt("Id_Barang"), 
                    rs.getString("Nama_Barang"), 
                    rs.getInt("Sisa")
                ));
            }
            comboBarang.setItems(listBarangDipinjam);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat daftar barang yang dipinjam: " + e.getMessage());
        }
    }
    
    private void muatRiwayatPengembalian() {
        listBarangMasuk.clear();
        String query = "SELECT bm.Id_BarangMasuk, b.Nama_Barang, bm.Jumlah_Masuk, bm.Tanggal_Masuk " +
                       "FROM barang_masuk bm JOIN barang b ON bm.Id_Barang = b.Id_Barang ORDER BY bm.Tanggal_Masuk DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                listBarangMasuk.add(new BarangMasuk(
                    rs.getInt("Id_BarangMasuk"), 0, rs.getString("Nama_Barang"), 
                    rs.getInt("Jumlah_Masuk"), rs.getString("Tanggal_Masuk")
                ));
            }
            tabelBarangMasuk.setItems(listBarangMasuk);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat riwayat pengembalian: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBarangDipilih(ActionEvent event) {
        BarangKeluarItem selected = comboBarang.getValue();
        if (selected != null) {
            lblSisa.setText("Jumlah dipinjam: " + selected.getSisaDipinjam());
        } else {
            lblSisa.setText("Jumlah dipinjam: -");
        }
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        BarangKeluarItem selectedBarang = comboBarang.getValue();
        if (selectedBarang == null || txtJumlah.getText().isEmpty() || dateTanggal.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua field harus diisi!");
            return;
        }
        
        int jumlahKembali;
        try {
            jumlahKembali = Integer.parseInt(txtJumlah.getText());
            if (jumlahKembali <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Jumlah harus angka positif!");
            return;
        }

        if (jumlahKembali > selectedBarang.getSisaDipinjam()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Jumlah pengembalian melebihi jumlah yang dipinjam (" + selectedBarang.getSisaDipinjam() + ")!");
            return;
        }
        
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            
            String sqlMasuk = "INSERT INTO barang_masuk (Id_Barang, Jumlah_Masuk, Tanggal_Masuk) VALUES (?, ?, ?)";
            PreparedStatement psMasuk = conn.prepareStatement(sqlMasuk);
            psMasuk.setInt(1, selectedBarang.getIdBarang());
            psMasuk.setInt(2, jumlahKembali);
            psMasuk.setDate(3, java.sql.Date.valueOf(dateTanggal.getValue()));
            psMasuk.executeUpdate();

            String sqlUpdateStok = "UPDATE barang SET Stok = Stok + ? WHERE Id_Barang = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStok);
            psUpdate.setInt(1, jumlahKembali);
            psUpdate.setInt(2, selectedBarang.getIdBarang());
            psUpdate.executeUpdate();
            
            conn.commit();
            
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pengembalian barang berhasil dicatat!");
            muatRiwayatPengembalian();
            muatDataBarangDipinjam(); // Muat ulang data agar sisa pinjaman terupdate
            bersihkanForm();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mencatat pengembalian barang: " + e.getMessage());
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
        lblSisa.setText("Jumlah dipinjam: -");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
