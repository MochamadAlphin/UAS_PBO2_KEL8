package com.mycompany.uas_pbo2_inventaris;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Memuat file FXML untuk tampilan login sebagai tampilan awal
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Login - Aplikasi Inventaris");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
