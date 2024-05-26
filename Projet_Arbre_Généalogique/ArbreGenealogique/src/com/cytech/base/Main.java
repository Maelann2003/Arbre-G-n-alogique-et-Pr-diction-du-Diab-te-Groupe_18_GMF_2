package com.cytech.base;

import java.util.List;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import com.opencsv.exceptions.CsvValidationException;

import smile.clustering.KMeans;
import smile.math.MathEx;

public class Main {
    public static void main(String[] args) throws CsvValidationException {
        GestionFamilles gestion = new GestionFamilles();

        List<Personne> personnes = gestion.chargerPersonnes();
        System.out.println("Personnes chargées initialement:");
        personnes.forEach(System.out::println);

        gestion.verifierEtCorrigerCoherence();

        System.out.println("\nPersonnes après correction des incohérences:");
        gestion.chargerPersonnes().forEach(System.out::println);

        gestion.nettoyerDonnees();
        System.out.println("\nPersonnes après nettoyage:");
        gestion.chargerPersonnes().forEach(System.out::println);

        System.out.println("\nArbre généalogique global:");
        gestion.afficherArbreGlobal();

        System.out.println("\nArbre généalogique pour la famille Dupont:");
        gestion.afficherArbreFamille("Dupont");

        System.out.println("\nDescendance de Jean Dupont:");
        gestion.afficherDescendance("1");

        System.out.println("\nAscendance de Marie Dupont:");
        gestion.afficherAscendance("3");

        System.out.println("\nProches de Paul Dupont:");
        gestion.afficherProches("6");

        System.out.println("\nPersonnes sans ascendant:");
        gestion.afficherSansAscendant();
        
        
        System.out.println("\nArbre généalogique global:");
        gestion.afficherArbreGenealogiqueGlobal();
        
        gestion.appliquerKMeans();
        
        double[][] data = gestion.prepareDataForClustering(personnes);

        MathEx.setSeed(19650218);

        KMeans kmeans = KMeans.fit(data, 3);

        KMeansChart chart = new KMeansChart("K-Means Clustering", personnes, kmeans);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        
        GenealogyTree treeFrame = new GenealogyTree("Arbre Généalogique", personnes);
        treeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        treeFrame.setSize(800, 600);
        treeFrame.setVisible(true);
    }
}
