
package com.mycompany.uas_pbo2_inventaris;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username dan Password tidak boleh kosong!");
            return;
        }

        // PERUBAHAN DI SINI: Menggunakan getConnection() sesuai file baru Anda
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM user WHERE Username = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nama = rs.getString("Nama");
                String role = rs.getString("Role");
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                Parent root = loader.load();
                
                DashboardController dashboardController = loader.getController();
                dashboardController.initData(nama, role);

                Stage dashboardStage = new Stage();
                dashboardStage.setTitle("Dashboard - Aplikasi Inventaris Gudang");
                dashboardStage.setScene(new Scene(root));
                dashboardStage.show();

            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau Password salah!");
            }

        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat login: " + e.getMessage());
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
