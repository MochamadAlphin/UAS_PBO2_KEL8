package com.mycompany.uas_pbo2_inventaris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 * Kelas utilitas untuk koneksi database yang aman.
 * Metode getConnection() akan selalu membuat koneksi BARU.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_pbo2_inventarisgudang";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Metode ini membuat dan mengembalikan KONEKSI BARU setiap kali dipanggil.
     * Ini adalah cara yang benar untuk mencegah error "connection closed".
     * @return Connection object yang baru jika berhasil.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Driver Error", "MySQL JDBC Driver tidak ditemukan. Pastikan dependensi ada di file pom.xml.");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Gagal terhubung ke database. Pastikan XAMPP/MySQL berjalan. Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return connection;
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
