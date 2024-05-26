package com.cytech.base;

import java.util.ArrayList;

public class Personne {
    private String id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String nationalite;
    private String idPere;
    private String idMere;
    private String typeDiabete; // Type de diabète (type 1, type 2, gestationnel, etc.)
    private int ageDiagnostic; // Âge au moment du diagnostic du diabète
    private boolean antecedentsDiabete; // Indique s'il y a des antécédents familiaux de diabète
    
    
    
    public Personne() {
    }

    public Personne(String id, String nom, String prenom, String dateNaissance, String nationalite, String idPere,
			String idMere, String typeDiabete, int ageDiagnostic, boolean antecedentsDiabete) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		this.nationalite = nationalite;
		this.idPere = idPere;
		this.idMere = idMere;
		this.typeDiabete = typeDiabete;
		this.ageDiagnostic = ageDiagnostic;
		this.antecedentsDiabete = antecedentsDiabete;
	}

	// Getters et setters pour chaque attribut
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getIdPere() {
        return idPere;
    }

    public String getIdMere() {
        return idMere;
    }

    public void setId(String id) {
		this.id = id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}

	public void setIdPere(String idPere) {
		this.idPere = idPere;
	}

	public void setIdMere(String idMere) {
		this.idMere = idMere;
	}

	@Override
    public String toString() {
        return "Personne{" +
               "id='" + id + '\'' +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", dateNaissance='" + dateNaissance + '\'' +
               ", nationalite='" + nationalite + '\'' +
               ", idPere='" + idPere + '\'' +
               ", idMere='" + idMere + '\'' +
               '}';
    }
    public String getTypeDiabete() {
        return typeDiabete;
    }

    

    public int getAgeDiagnostic() {
        return ageDiagnostic;
    }

    public void setAgeDiagnostic(int ageDiagnostic) {
        this.ageDiagnostic = ageDiagnostic;
    }

    public boolean hasAntecedentsFamiliaux() {
        return antecedentsDiabete;
    }

    public void setAntecedentsFamiliaux(boolean antecedentsDiabete) {
        this.antecedentsDiabete = antecedentsDiabete;
    }

 // Getters et setters pour les nouveaux attributs
    public boolean isAntecedentsDiabete() {
        return antecedentsDiabete;
    }

    public void setAntecedentsDiabete(boolean antecedentsDiabete) {
        this.antecedentsDiabete = antecedentsDiabete;
    }

    

    public void setTypeDiabete(String typeDiabete) {
        this.typeDiabete = typeDiabete;
    }


    public void setAgeDiagnostic(Integer ageDiagnostic) {
        this.ageDiagnostic = ageDiagnostic;
    }

}

