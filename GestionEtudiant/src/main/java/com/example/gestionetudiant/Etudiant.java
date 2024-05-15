package com.example.gestionetudiant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Etudiant {
    private int id;
    private String numCin; // Numéro CIN
    private String prenom;
    private String nom;
    private int age;
    private double moyenne;
    private List<String> niveauxClasse; // Liste des niveaux de classe
    private Map<String, Double> notes;
    // Constructeur
    public Etudiant(int id, String numCin, String prenom, String nom, int age, List<String> niveauxClasse,double moyenne) {
        this.id = id;
        this.numCin = numCin;
        this.prenom = prenom;
        this.nom = nom;
        this.age = age;
        this.niveauxClasse = niveauxClasse;
        this.moyenne = moyenne;
        notes = new HashMap<>();
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumCin() {
        return numCin;
    }

    public void setNumCin(String numCin) {
        this.numCin = numCin;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getNiveauxClasse() {
        return niveauxClasse;
    }

    public void setNiveauxClasse(List<String> niveauxClasse) {
        this.niveauxClasse = niveauxClasse;
    }
    public void ajouterNote(String matiere, double note) {
        notes.put(matiere, note);
    }
   public double getMoyenne() {
     return moyenne;
    }

    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }


  //  public double getMoyenne() {
    //    if (notes.isEmpty()) {
      //      return 0.0; // Retourner 0 si aucune note n'est disponible
        //}
        //double total = 0.0;
        //for (double note : notes.values()) {
          //  total += note;
        //}
        //return total / notes.size(); // Calculer la moyenne
    //}
}

