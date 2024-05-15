// GestionEtudiant.java
package com.example.gestionetudiant;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class GestionEtudiant {
    private Connection connection;
    private List<String> niveauxClasse = Arrays.asList("TIC1A", "TIC1B", "TIC1C");

    public GestionEtudiant(Connection connection) {
        this.connection = connection;
    }

    private Etudiant getEtudiantFromDatabase(String studentName) {
        Etudiant etudiant = null;
        try {
            // Exécutez une requête SQL pour récupérer les données de l'étudiant en fonction de son nom
            String query = "SELECT * FROM etudiants WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentName);
            ResultSet resultSet = statement.executeQuery();

            // Vérifiez si l'étudiant a été trouvé
            if (resultSet.next()) {
                // Récupérez les données de l'étudiant à partir du résultat de la requête
                int id = resultSet.getInt("id");
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                int age = resultSet.getInt("age");
                String numCin = resultSet.getString("num_cin");
                String niveauClasse = resultSet.getString("niveau_classe");
                double moyenne = resultSet.getDouble("moyenne");
                // Créez un objet Etudiant avec les données récupérées
                etudiant = new Etudiant(id, numCin, prenom, nom, age, Arrays.asList(niveauClasse),moyenne);
            }
            // Fermez les ressources
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return etudiant;
    }
    private void ajouterEtudiant(Connection connection, String prenom, String nom, int age, String numCin, String niveauClasse) throws SQLException {
        String query = "INSERT INTO etudiants (prenom, nom, age, num_cin, niveau_classe) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setInt(3, age);
            statement.setString(4, numCin);
            statement.setString(5, niveauClasse);
            statement.executeUpdate();
        }
    }
    private void modifierEtudiant(Connection connection, int id, String prenom, String nom, int age, String numCin) throws SQLException {
        String query = "UPDATE etudiants SET prenom = ?, nom = ?, age = ?, num_cin = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setInt(3, age);
            statement.setString(4, numCin);
            statement.setInt(5, id);
            statement.executeUpdate();
        }
    }
    private void supprimerEtudiant(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM etudiants WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private void afficherEtudiants() {
        Stage stage = new Stage();
        stage.setTitle("Liste des étudiants");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        try {
            String query = "SELECT * FROM etudiants";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Liste des étudiants :\n");
                while (resultSet.next()) {
                    stringBuilder.append("ID : ").append(resultSet.getInt("id"))
                            .append(", Prénom : ").append(resultSet.getString("prenom"))
                            .append(", Nom : ").append(resultSet.getString("nom"))
                            .append(", Age : ").append(resultSet.getInt("age"))
                            .append(", Numéro CIN : ").append(resultSet.getString("num_cin"))
                            .append(", Niveau de classe : ").append(resultSet.getString("niveau_classe"))
                            .append(", Moyenne : ").append(resultSet.getDouble("moyenne"))
                            .append("\n");
                }
                textArea.setText(stringBuilder.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(textArea, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
    private ComboBox<String> createStudentsComboBox() {
        ComboBox<String> studentsComboBox = new ComboBox<>();

        // Exécutez une requête SQL pour sélectionner les noms des étudiants à partir de la base de données
        String query = "SELECT nom FROM etudiants";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Parcourez les résultats de la requête et ajoutez chaque nom à la ComboBox
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                studentsComboBox.getItems().add(nom);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return studentsComboBox;
    }
    private Etudiant getStudentDetails(int studentId) {
        // Exécution d'une requête SQL pour récupérer les détails de l'étudiant à partir de la base de données
        String query = "SELECT id, nom, prenom, num_cin, age, niveau_classe FROM etudiants WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Récupération des données depuis le ResultSet
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String numCin = resultSet.getString("num_cin");
                    int age = resultSet.getInt("age");
                    String niveauClasse = resultSet.getString("niveau_classe");
                    double moyenne = resultSet.getDouble("moyenne");
                    // Création de l'objet Etudiant avec les données récupérées
                    return new Etudiant(id, nom, prenom, numCin, age,niveauxClasse,moyenne);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
