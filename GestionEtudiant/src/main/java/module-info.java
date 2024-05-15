module com.example.gestionetudiant {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.gestionetudiant to javafx.fxml;
    exports com.example.gestionetudiant;
}