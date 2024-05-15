package com.example.gestionetudiant;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class admin extends Application {
    private Connection connection;
    private List<String> niveauxClasse = Arrays.asList("TIC1A", "TIC1B", "TIC1C","TIC1D");

    public static void main(String[] args) {
        launch(args);
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


    @Override
    public void start(Stage primaryStage) {
        connectToDatabase(); // Establish database connection
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Create UI components
        Label titleLabel = new Label("Gestion des étudiants");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        gridPane.add(titleLabel, 0, 0, 2, 1);

        Button addButton = new Button("Ajouter un étudiant");
        addButton.setOnAction(e -> ajouterEtudiantMenu());
        gridPane.add(addButton, 0, 1);

        Button modifyButton = new Button("Modifier un étudiant");
        modifyButton.setOnAction(e -> modifierEtudiantMenu());
        gridPane.add(modifyButton, 1, 1);

        Button deleteButton = new Button("Supprimer un étudiant");
        deleteButton.setOnAction(e -> supprimerEtudiantMenu());
        gridPane.add(deleteButton, 0, 2);

        Button enterGradesButton = new Button("Saisir les notes");
        enterGradesButton.setOnAction(e -> saisirNotesHorsAjoutEtModif());
        gridPane.add(enterGradesButton, 0, 3, 2, 1); // Ajouter le bouton sur toute la largeur de la grille

        Button showButton = new Button("Afficher la liste des étudiants");
        showButton.setOnAction(e -> afficherEtudiants());
        gridPane.add(showButton, 1, 2);

        Scene scene = new Scene(gridPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des étudiants");
        primaryStage.show();

        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        modifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        deleteButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        enterGradesButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        showButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        gridPane.setStyle("-fx-padding: 20px; -fx-hgap: 15px; -fx-vgap: 10px;");
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ajouterEtudiantMenu() {
        Stage stage = new Stage();
        stage.setTitle("Ajouter un étudiant");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label prenomLabel = new Label("Prénom :");
        TextField prenomField = new TextField();
        gridPane.add(prenomLabel, 0, 0);
        gridPane.add(prenomField, 1, 0);

        Label nomLabel = new Label("Nom :");
        TextField nomField = new TextField();
        gridPane.add(nomLabel, 0, 1);
        gridPane.add(nomField, 1, 1);

        Label ageLabel = new Label("Âge :");
        TextField ageField = new TextField();
        gridPane.add(ageLabel, 0, 2);
        gridPane.add(ageField, 1, 2);

        Label numCinLabel = new Label("Numéro CIN :");
        TextField numCinField = new TextField();
        gridPane.add(numCinLabel, 0, 3);
        gridPane.add(numCinField, 1, 3);

        Label niveauLabel = new Label("Niveau de classe :");
        ChoiceBox<String> niveauChoiceBox = new ChoiceBox<>();
        niveauChoiceBox.getItems().addAll(niveauxClasse);
        niveauChoiceBox.setValue(niveauxClasse.get(0));
        gridPane.add(niveauLabel, 0, 4);
        gridPane.add(niveauChoiceBox, 1, 4);

        Button addButton = new Button("Ajouter");
        addButton.setOnAction(e -> {
            String prenom = prenomField.getText();
            String nom = nomField.getText();
            int age = Integer.parseInt(ageField.getText());
            String numCin = numCinField.getText();
            String niveauClasse = niveauChoiceBox.getValue();

            try {
                ajouterEtudiant(connection, prenom, nom, age, numCin, niveauClasse);
                stage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        gridPane.add(addButton, 0, 5);

        Scene scene = new Scene(gridPane, 300, 250);
        stage.setScene(scene);
        stage.show();
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
    }

    private void modifierEtudiantMenu() {
        Stage stage = new Stage();
        stage.setTitle("Modifier un étudiant");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label idLabel = new Label("ID de l'étudiant :");
        TextField idField = new TextField();
        gridPane.add(idLabel, 0, 0);
        gridPane.add(idField, 1, 0);

        Label prenomLabel = new Label("Nouveau prénom :");
        TextField prenomField = new TextField();
        gridPane.add(prenomLabel, 0, 1);
        gridPane.add(prenomField, 1, 1);

        Label nomLabel = new Label("Nouveau nom :");
        TextField nomField = new TextField();
        gridPane.add(nomLabel, 0, 2);
        gridPane.add(nomField, 1, 2);

        Label ageLabel = new Label("Nouvel âge :");
        TextField ageField = new TextField();
        gridPane.add(ageLabel, 0, 3);
        gridPane.add(ageField, 1, 3);

        Label numCinLabel = new Label("Nouveau numéro CIN :");
        TextField numCinField = new TextField();
        gridPane.add(numCinLabel, 0, 4);
        gridPane.add(numCinField, 1, 4);

        Button modifyButton = new Button("Modifier");
        modifyButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            String prenom = prenomField.getText();
            String nom = nomField.getText();
            int age = Integer.parseInt(ageField.getText());
            String numCin = numCinField.getText();

            try {
                modifierEtudiant(connection, id, prenom, nom, age, numCin);
                stage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        gridPane.add(modifyButton, 0, 5);

        Scene scene = new Scene(gridPane, 300, 250);
        stage.setScene(scene);
        stage.show();
        modifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
    }

    private void supprimerEtudiantMenu() {
        Stage stage = new Stage();
        stage.setTitle("Supprimer un étudiant");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label idLabel = new Label("ID de l'étudiant à supprimer :");
        TextField idField = new TextField();
        gridPane.add(idLabel, 0, 0);
        gridPane.add(idField, 1, 0);

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            try {
                supprimerEtudiant(connection, id);
                stage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        gridPane.add(deleteButton, 0, 1);

        Scene scene = new Scene(gridPane, 300, 150);
        stage.setScene(scene);
        stage.show();
        deleteButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
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
        String query = "SELECT nom FROM etudiants";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                studentsComboBox.getItems().add(nom);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return studentsComboBox;
    }
    private void saisirNotesHorsAjoutEtModif() {
        Stage enterGradesStage = new Stage();
        enterGradesStage.setTitle("Saisir les notes");


        ComboBox<String> studentsComboBox = createStudentsComboBox();


        TextField mathField = new TextField();
        TextField javaField = new TextField();
        TextField pythonField = new TextField();
        TextField databaseField = new TextField();
        TextField networksField = new TextField();
        TextField phpField = new TextField();


        Button submitButton = new Button("Calcule moyenne");

        submitButton.setOnAction(e -> {

            String selectedStudentName = studentsComboBox.getValue();
            double mathGrade = Double.parseDouble(mathField.getText());
            double javaGrade = Double.parseDouble(javaField.getText());
            double pythonGrade = Double.parseDouble(pythonField.getText());
            double databaseGrade = Double.parseDouble(databaseField.getText());
            double networksGrade = Double.parseDouble(networksField.getText());
            double phpGrade = Double.parseDouble(phpField.getText());
            double moyenne = (mathGrade + javaGrade + pythonGrade + databaseGrade + networksGrade + phpGrade) / 6;
            String updateQuery = "UPDATE etudiants SET moyenne = ? WHERE nom = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, moyenne);
                updateStatement.setString(2, selectedStudentName);
                updateStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            enterGradesStage.close();
        });


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        gridPane.add(new Label("Étudiant :"), 0, 0);
        gridPane.add(studentsComboBox, 1, 0);
        gridPane.add(new Label("Mathématiques :"), 0, 1);
        gridPane.add(mathField, 1, 1);
        gridPane.add(new Label("Java :"), 0, 2);
        gridPane.add(javaField, 1, 2);
        gridPane.add(new Label("Python :"), 0, 3);
        gridPane.add(pythonField, 1, 3);
        gridPane.add(new Label("Base de données :"), 0, 4);
        gridPane.add(databaseField, 1, 4);
        gridPane.add(new Label("Réseaux :"), 0, 5);
        gridPane.add(networksField, 1, 5);
        gridPane.add(new Label("PHP :"), 0, 6);
        gridPane.add(phpField, 1, 6);
        gridPane.add(submitButton, 0, 7, 2, 1);

        Scene enterGradesScene = new Scene(gridPane, 400, 300);
        enterGradesStage.setScene(enterGradesScene);


        enterGradesStage.show();
    }

    private Etudiant getStudentDetails(int studentId) {
        String query = "SELECT id, nom, prenom, num_cin, age, niveau_classe FROM etudiants WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
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
