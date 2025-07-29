module com.mycompany.uas_pbo2_inventaris {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.uas_pbo2_inventaris to javafx.fxml;
    exports com.mycompany.uas_pbo2_inventaris;
}
