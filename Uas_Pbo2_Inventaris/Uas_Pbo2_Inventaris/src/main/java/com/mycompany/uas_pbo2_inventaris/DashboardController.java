package com.mycompany.uas_pbo2_inventaris;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private Label lblNamaUser;
    @FXML private Label lblRoleUser;
    @FXML private Button btnDataBarang;
    @FXML private Button btnBarangMasuk;
    @FXML private Button btnBarangKeluar;
    @FXML private Button btnLaporan;
    
    private String userRole;

    public void initData(String nama, String role) {
        lblNamaUser.setText(nama);
        lblRoleUser.setText("(" + role + ")");
        this.userRole = role;
        
        aturHakAksesDashboard();
    }
    
    /**
     * Mengatur tombol mana yang terlihat di Dashboard berdasarkan peran.
     */
    private void aturHakAksesDashboard() {
        // Logika untuk Admin: Hanya tombol Data Barang yang terlihat
        if ("Admin".equals(userRole)) {
            btnBarangMasuk.setVisible(false);
            btnBarangMasuk.setManaged(false); // Hapus dari layout
            btnBarangKeluar.setVisible(false);
            btnBarangKeluar.setManaged(false);
            btnLaporan.setVisible(false);
            btnLaporan.setManaged(false);
        }
        // Logika untuk Petugas Gudang: Tombol Laporan disembunyikan
        else if ("Petugas Gudang".equals(userRole)) {
            btnLaporan.setVisible(false);
            btnLaporan.setManaged(false);
        }
        // Manajer bisa melihat semuanya (tidak perlu aksi)
    }

    @FXML
    private void openDataBarang(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataBarang.fxml"));
            Parent root = loader.load();
            
            // Mengirim peran ke controller tujuan
            DataBarangController controller = loader.getController();
            controller.initData(this.userRole);
            
            // Menampilkan jendela baru
            Stage stage = new Stage();
            stage.setTitle("Manajemen Data Barang");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error Membuka Jendela", "Gagal memuat DataBarang.fxml. Pastikan file ada dan benar. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openBarangMasuk(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BarangMasuk.fxml"));
            Parent root = loader.load();
            
            // Mengirim peran ke controller tujuan
            BarangMasukController controller = loader.getController();
            controller.initData(this.userRole);
            
            // Menampilkan jendela baru
            Stage stage = new Stage();
            stage.setTitle("Manajemen Barang Masuk");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error Membuka Jendela", "Gagal memuat BarangMasuk.fxml. Pastikan file ada dan benar. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openBarangKeluar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BarangKeluar.fxml"));
            Parent root = loader.load();
            
            // Mengirim peran ke controller tujuan
            BarangKeluarController controller = loader.getController();
            controller.initData(this.userRole);
            
            // Menampilkan jendela baru
            Stage stage = new Stage();
            stage.setTitle("Manajemen Barang Keluar");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error Membuka Jendela", "Gagal memuat BarangKeluar.fxml. Pastikan file ada dan benar. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openLaporan(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Laporan.fxml"));
            Parent root = loader.load();
            
            // LaporanController tidak perlu initData karena hanya bisa diakses Manajer
            
            Stage stage = new Stage();
            stage.setTitle("Laporan Inventaris");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error Membuka Jendela", "Gagal memuat Laporan.fxml. Pastikan file ada dan benar. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Login - Aplikasi Inventaris");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
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
