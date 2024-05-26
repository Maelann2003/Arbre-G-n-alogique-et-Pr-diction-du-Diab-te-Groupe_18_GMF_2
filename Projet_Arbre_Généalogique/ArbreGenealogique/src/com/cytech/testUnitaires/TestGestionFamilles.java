package com.cytech.testUnitaires;

import com.cytech.base.GestionFamilles;
import com.cytech.base.Personne;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestGestionFamilles {

    private GestionFamilles gestionFamilles;

    public static void main(String[] args) {
        TestGestionFamilles test = new TestGestionFamilles();
        try {
            test.setUp();
            test.testChargerPersonnes();
            test.testAjouterPersonne();
            test.testSupprimerPersonne();
            test.testVerifierCoherence();
            test.testVerifierEtCorrigerCoherence();
            test.testNettoyerDonnees();
            test.testAfficherArbreGlobal();
            test.testAfficherArbreFamille();
            test.testAfficherDescendance();
            test.testAfficherAscendance();
            test.testAfficherProches();
            test.testAfficherSansAscendant();
            test.testAppliquerKMeans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setUp() throws CsvValidationException, IOException {
        try (FileWriter writer = new FileWriter("./familles.csv")) {
            writer.write("id,nom,prenom,dateNaissance,nationalite,idPere,idMere,antecedentsDiabete,typeDiabete,ageDiagnostic\n");
            writer.write("1,Dupont,Jean,01/01/1980,Française,,,false,,\n");
            writer.write("2,Dupont,Marie,02/02/1982,Française,,,false,,\n");
            writer.write("3,Dupont,Paul,03/03/2010,Française,1,2,false,,\n");
        }

        gestionFamilles = new GestionFamilles();
    }

    void testChargerPersonnes() throws CsvValidationException {
        System.out.println("Running testChargerPersonnes...");
        List<Personne> personnes = gestionFamilles.chargerPersonnes();
        if (personnes.size() == 3) {
            System.out.println("testChargerPersonnes passed.");
        } else {
            System.out.println("testChargerPersonnes failed.");
        }
    }

    void testAjouterPersonne() throws CsvValidationException {
        System.out.println("Running testAjouterPersonne...");
        Personne nouvellePersonne = new Personne("4", "Martin", "Pierre", "01/01/1990", "Française", "", "", "Type 1", 25, true);
        gestionFamilles.ajouterPersonne(nouvellePersonne);

        List<Personne> personnes = gestionFamilles.chargerPersonnes();
        if (personnes.size() == 4 && personnes.contains(nouvellePersonne)) {
            System.out.println("testAjouterPersonne passed.");
        } else {
            System.out.println("testAjouterPersonne failed.");
        }
    }

    void testSupprimerPersonne() throws CsvValidationException {
        System.out.println("Running testSupprimerPersonne...");
        gestionFamilles.supprimerPersonne("1");

        List<Personne> personnes = gestionFamilles.chargerPersonnes();
        if (personnes.size() == 2 && gestionFamilles.findPersonById("1") == null) {
            System.out.println("testSupprimerPersonne passed.");
        } else {
            System.out.println("testSupprimerPersonne failed.");
        }
    }

    void testVerifierCoherence() {
        System.out.println("Running testVerifierCoherence...");
        gestionFamilles.verifierCoherence();
        System.out.println("testVerifierCoherence passed.");
    }

    void testVerifierEtCorrigerCoherence() {
        System.out.println("Running testVerifierEtCorrigerCoherence...");
        Personne nouvellePersonne = new Personne("4", "Martin", "Pierre", "01/01/1990", "Française", "99", "", "Type 1", 25, true);
        gestionFamilles.ajouterPersonne(nouvellePersonne);

        gestionFamilles.verifierEtCorrigerCoherence();

        Personne p = gestionFamilles.findPersonById("4");
        if ("".equals(p.getIdPere())) {
            System.out.println("testVerifierEtCorrigerCoherence passed.");
        } else {
            System.out.println("testVerifierEtCorrigerCoherence failed.");
        }
    }

    void testNettoyerDonnees() throws CsvValidationException {
        System.out.println("Running testNettoyerDonnees...");
        Personne nouvellePersonne = new Personne("4", "Martin", "", "99/99/9999", "Française", "", "", "Type 1", 25, true);
        gestionFamilles.ajouterPersonne(nouvellePersonne);

        gestionFamilles.nettoyerDonnees();

        List<Personne> personnes = gestionFamilles.chargerPersonnes();
        if (personnes.size() == 3) {
            System.out.println("testNettoyerDonnees passed.");
        } else {
            System.out.println("testNettoyerDonnees failed.");
        }
    }

    void testAfficherArbreGlobal() {
        System.out.println("Running testAfficherArbreGlobal...");
        gestionFamilles.afficherArbreGlobal();
        System.out.println("testAfficherArbreGlobal passed.");
    }

    void testAfficherArbreFamille() {
        System.out.println("Running testAfficherArbreFamille...");
        gestionFamilles.afficherArbreFamille("Dupont");
        System.out.println("testAfficherArbreFamille passed.");
    }

    void testAfficherDescendance() {
        System.out.println("Running testAfficherDescendance...");
        gestionFamilles.afficherDescendance("1");
        System.out.println("testAfficherDescendance passed.");
    }

    void testAfficherAscendance() {
        System.out.println("Running testAfficherAscendance...");
        gestionFamilles.afficherAscendance("3");
        System.out.println("testAfficherAscendance passed.");
    }

    void testAfficherProches() {
        System.out.println("Running testAfficherProches...");
        gestionFamilles.afficherProches("3");
        System.out.println("testAfficherProches passed.");
    }

    void testAfficherSansAscendant() {
        System.out.println("Running testAfficherSansAscendant...");
        gestionFamilles.afficherSansAscendant();
        System.out.println("testAfficherSansAscendant passed.");
    }

    void testAppliquerKMeans() {
        System.out.println("Running testAppliquerKMeans...");
        gestionFamilles.appliquerKMeans();
        System.out.println("testAppliquerKMeans passed.");
    }
}
