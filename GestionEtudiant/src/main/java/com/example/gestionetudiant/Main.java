package com.example.gestionetudiant;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion", "root", "");
            GestionEtudiant gestionEtudiant = new GestionEtudiant(connection);
           // gestionEtudiant.showMenu(primaryStage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
