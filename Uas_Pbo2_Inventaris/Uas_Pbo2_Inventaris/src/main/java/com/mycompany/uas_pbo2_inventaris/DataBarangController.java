package com.mycompany.uas_pbo2_inventaris;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class DataBarangController implements Initializable {

    // Deklarasi komponen FXML sesuai dengan DataBarang.fxml (Versi PT KAI)
    @FXML private TextField txtIdBarang, txtNamaBarang, txtStok;
    @FXML private ComboBox<String> comboKategori;
    @FXML private ComboBox<String> comboSatuan;
    @FXML private DatePicker dateTanggalPengiriman;
    @FXML private TableView<Barang> tabelBarang;
    @FXML private TableColumn<Barang, Integer> colId, colStok;
    @FXML private TableColumn<Barang, String> colNama, colKategori, colSatuan, colTanggalPengiriman;
    @FXML private Button btnTambah, btnUbah, btnHapus;

    private final ObservableList<Barang> listBarang = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String userRole;

    public void initData(String role) {
        this.userRole = role;
        aturHakAkses();
    }
    
    private void aturHakAkses() {
        if ("Admin".equals(this.userRole)) {
            btnTambah.setDisable(true);
            btnUbah.setDisable(true);
            btnHapus.setDisable(true);
            
            txtNamaBarang.setEditable(false);
            comboKategori.setDisable(true);
            comboSatuan.setDisable(true);
            txtStok.setEditable(false);
            dateTanggalPengiriman.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        muatDataDariDB();
        isiPilihanComboBox();
        handleBersihkan(null);
    }

    private void isiPilihanComboBox() {
        // Pilihan untuk Kategori PT KAI
        ObservableList<String> kategoriList = FXCollections.observableArrayList(
            "Kebutuhan Interior Gerbong", "Perkakas & Alat Berat", "Barang Pecah Belah", 
            "Elektronik & Suku Cadang", "Material Konstruksi Rel", "Alat Kebersihan", "Lainnya"
        );
        comboKategori.setItems(kategoriList);

        // Pilihan untuk Satuan
        ObservableList<String> satuanList = FXCollections.observableArrayList(
            "Pcs", "Unit", "Set", "Kg", "Liter", "Meter", "Box", "Roll"
        );
        comboSatuan.setItems(satuanList);
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idBarang"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategoriBarang"));
        colSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colTanggalPengiriman.setCellValueFactory(new PropertyValueFactory<>("tanggalPengiriman"));
    }

    private void muatDataDariDB() {
        listBarang.clear();
        String query = "SELECT * FROM barang ORDER BY Id_Barang DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                listBarang.add(new Barang(
                    rs.getInt("Id_Barang"),
                    rs.getString("Nama_Barang"),
                    rs.getString("Kategori_Barang"),
                    rs.getString("Satuan"),
                    rs.getInt("Stok"),
                    rs.getString("Tanggal_Pengiriman")
                ));
            }
            tabelBarang.setItems(listBarang);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data barang: " + e.getMessage());
        }
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        if (!validasiInput()) return;

        String query = "INSERT INTO barang (Nama_Barang, Kategori_Barang, Satuan, Stok, Tanggal_Pengiriman) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, txtNamaBarang.getText());
            ps.setString(2, comboKategori.getValue());
            ps.setString(3, comboSatuan.getValue());
            ps.setInt(4, Integer.parseInt(txtStok.getText()));
            ps.setString(5, dateTanggalPengiriman.getValue().format(formatter));
            
            ps.executeUpdate();
            muatDataDariDB();
            handleBersihkan(null);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data barang berhasil ditambahkan!");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan data: " + e.getMessage());
        }
    }

    @FXML
    private void handleUbah(ActionEvent event) {
        if (txtIdBarang.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang akan diubah dari tabel.");
            return;
        }
        
        if (!validasiInput()) return;

        String query = "UPDATE barang SET Nama_Barang=?, Kategori_Barang=?, Satuan=?, Stok=?, Tanggal_Pengiriman=? WHERE Id_Barang=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, txtNamaBarang.getText());
            ps.setString(2, comboKategori.getValue());
            ps.setString(3, comboSatuan.getValue());
            ps.setInt(4, Integer.parseInt(txtStok.getText()));
            ps.setString(5, dateTanggalPengiriman.getValue().format(formatter));
            ps.setInt(6, Integer.parseInt(txtIdBarang.getText()));
            
            ps.executeUpdate();
            muatDataDariDB();
            handleBersihkan(null);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data barang berhasil diubah!");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengubah data: " + e.getMessage());
        }
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        if (txtIdBarang.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang akan dihapus dari tabel.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Anda akan menghapus: " + txtNamaBarang.getText());
        alert.setContentText("Tindakan ini tidak dapat dibatalkan. Lanjutkan?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            String query = "DELETE FROM barang WHERE Id_Barang=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, Integer.parseInt(txtIdBarang.getText()));
                ps.executeUpdate();
                muatDataDariDB();
                handleBersihkan(null);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data barang berhasil dihapus!");
            } catch (SQLException | NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBersihkan(ActionEvent event) {
        txtIdBarang.clear();
        txtNamaBarang.clear();
        comboKategori.getSelectionModel().clearSelection();
        comboSatuan.getSelectionModel().clearSelection();
        txtStok.setText("0");
        dateTanggalPengiriman.setValue(LocalDate.now());
        tabelBarang.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        Barang barang = tabelBarang.getSelectionModel().getSelectedItem();
        if (barang != null) {
            txtIdBarang.setText(String.valueOf(barang.getIdBarang()));
            txtNamaBarang.setText(barang.getNamaBarang());
            comboKategori.setValue(barang.getKategoriBarang());
            comboSatuan.setValue(barang.getSatuan());
            txtStok.setText(String.valueOf(barang.getStok()));
            dateTanggalPengiriman.setValue(LocalDate.parse(barang.getTanggalPengiriman(), formatter));
        }
    }
    
    private boolean validasiInput() {
        String nama = txtNamaBarang.getText();
        String kategori = comboKategori.getValue();
        String satuan = comboSatuan.getValue();
        String stok = txtStok.getText();

        if (nama.trim().isEmpty() || kategori == null || satuan == null || 
            stok.trim().isEmpty() || dateTanggalPengiriman.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validasi Gagal", "Semua kolom harus diisi!");
            return false;
        }

        if (nama.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Validasi Gagal", "Nama Barang tidak boleh lebih dari 100 karakter.");
            return false;
        }

        try {
            int stokValue = Integer.parseInt(stok);
            if (stokValue < 0) {
                showAlert(Alert.AlertType.WARNING, "Validasi Gagal", "Stok tidak boleh bernilai negatif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validasi Gagal", "Stok harus berupa angka yang valid.");
            return false;
        }

        return true;
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
