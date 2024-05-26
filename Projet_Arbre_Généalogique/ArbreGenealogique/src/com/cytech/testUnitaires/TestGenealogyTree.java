package com.cytech.testUnitaires;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import com.cytech.base.GenealogyTree;
import com.cytech.base.Personne;
import com.mxgraph.swing.mxGraphComponent;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TestGenealogyTree {

    public static void main(String[] args) {
        TestGenealogyTree test = new TestGenealogyTree();
        test.testGenealogyTreeCreation();
    }

    void testGenealogyTreeCreation() {
        System.out.println("Running testGenealogyTreeCreation...");

        try {
            List<Personne> personnes = new ArrayList<>();
            personnes.add(new Personne("1", "Doe", "John", "01/01/1980", "American", "", "", "Type 1", 25, true));
            personnes.add(new Personne("2", "Doe", "Jane", "02/02/1985", "American", "", "", "Type 2", 30, true));
            personnes.add(new Personne("3", "Doe", "Junior", "03/03/2010", "American", "1", "2", null, 0, false));

            GenealogyTree tree = new GenealogyTree("Genealogy Tree Test", personnes);
            tree.setSize(800, 600);
            tree.setVisible(true);

            Graph<String, DefaultEdge> graph = tree.createGraph(personnes);
            if (graph != null && graph.vertexSet().size() == 3 && graph.edgeSet().size() == 2) {
                System.out.println("testGenealogyTreeCreation passed.");
            } else {
                System.out.println("testGenealogyTreeCreation failed.");
            }

            SwingUtilities.invokeLater(() -> tree.setVisible(false));

        } catch (Exception e) {
            System.out.println("testGenealogyTreeCreation failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
